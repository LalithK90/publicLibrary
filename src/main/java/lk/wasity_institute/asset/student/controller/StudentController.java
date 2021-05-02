package lk.wasity_institute.asset.student.controller;


import lk.wasity_institute.asset.batch.controller.BatchController;
import lk.wasity_institute.asset.batch.entity.enums.Grade;
/*import lk.wasity_institute.asset.batch.entity.enums.Medium;*/
import lk.wasity_institute.asset.batch_student.service.BatchStudentService;
import lk.wasity_institute.asset.common_asset.model.enums.Gender;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.school.service.SchoolService;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.student.service.StudentService;
import lk.wasity_institute.util.interfaces.AbstractController;
import lk.wasity_institute.util.service.MakeAutoGenerateNumberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController implements AbstractController<Student, Integer> {
    private final StudentService studentService;
    private final BatchStudentService batchStudentService;
    private final SchoolService schoolService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

    public StudentController(StudentService studentService,
                             BatchStudentService batchStudentService, SchoolService schoolService,
                             MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
        this.studentService = studentService;
        this.batchStudentService = batchStudentService;
        this.schoolService = schoolService;
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("students", studentService.findAll()
                .stream()
                .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
                .collect(Collectors.toList()));
        model.addAttribute("studentRemoveBatch", false);
        return "student/student";
    }

    private String commonThing(Model model, Student student, boolean addStatus) {
        model.addAttribute("student", student);
        model.addAttribute("addStatus", addStatus);
        model.addAttribute("grades", Grade.values());
        // model.addAttribute("mediums", Medium.values());
        model.addAttribute("liveDeads", LiveDead.values());
        model.addAttribute("schools", schoolService.findAll().stream()
                .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
                .collect(Collectors.toList()));
        model.addAttribute("gender", Gender.values());
        model.addAttribute("batchUrl", MvcUriComponentsBuilder
                .fromMethodName(BatchController.class, "findByGrade", "")
                .build()
                .toString());
//    model.addAttribute("batchUrlM", MvcUriComponentsBuilder
//            .fromMethodName(BatchController.class, "findByMedium", "")
//            .build()
//            .toString());


        return "student/addStudent";
    }

    @GetMapping("/add")
    public String form(Model model) {

        return commonThing(model, new Student(), true);
    }

    @GetMapping("/view/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("studentDetail", studentService.findById(id));
        return "student/student-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, studentService.findById(id), false);
    }

    @PostMapping("/save")
    public String persist(@Valid @ModelAttribute Student student, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThing(model, student, true);
        }

//there are two different situation
        //1. new Student -> need to generate new number
        //2. update student -> no required to generate number
        if (student.getId() == null) {
            // need to create auto generated registration number
            Student lastStudent = studentService.lastStudentOnDB();
            if (lastStudent != null) {
                String lastNumber = lastStudent.getRegNo().substring(3);
                student.setRegNo("WIS" + makeAutoGenerateNumberService.numberAutoGen(lastNumber));
            } else {
                student.setRegNo("WIS" + makeAutoGenerateNumberService.numberAutoGen(null));
            }
        }
        try {
            studentService.persist(student);
        } catch (Exception e) {
          if (student.getId() == null) {
            ObjectError error = new ObjectError("student",
                    "Please fix following errors which you entered .\n System message -->" + e.getCause().getCause().getMessage());
            bindingResult.addError(error);
            return commonThing(model, student, true);
          }else {
            return commonThing(model, studentService.findById(student.getId()), false);
          }
        }
        student.getBatchStudents().forEach(x -> {
            x.setStudent(student);
            batchStudentService.persist(x);
        });

        return "redirect:/student";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        studentService.delete(id);
        return "redirect:/student";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute Student student, Model model) {
        List<Student> students = studentService.search(student);

        if (students.isEmpty()) {
            model.addAttribute("student", true);
            return "student/studentChooser";
        } else if (students.size() == 1) {
            return "redirect:/payment/add/" + students.get(0).getId();
        } else {
            model.addAttribute("students", students);
            return "student/studentChooser";
        }


    }


    @PostMapping("/search/ByName")
    public String searchByName(@ModelAttribute Student student, Model model) {
        List<Student> students = studentService.searchByName(student);

        if (students.isEmpty()) {
            model.addAttribute("student", true);
            return "student/studentChooser";
        } else if (students.size() == 1) {
            return "redirect:/payment/add/" + students.get(0).getFirstName();
        } else {
            model.addAttribute("students", students);
            return "student/studentChooser";
        }
    }
}
