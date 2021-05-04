package lk.wasity_institute.asset.batch_exam.controller;


import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.batch_exam.entity.BatchExam;
import lk.wasity_institute.asset.batch_exam.entity.enums.ExamStatus;
import lk.wasity_institute.asset.batch_exam.service.BatchExamService;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.student.service.StudentService;
import lk.wasity_institute.asset.teacher.entity.Teacher;
import lk.wasity_institute.asset.teacher.service.TeacherService;
import lk.wasity_institute.asset.user_management.entity.User;
import lk.wasity_institute.asset.user_management.service.UserService;
import lk.wasity_institute.util.service.EmailService;
import lk.wasity_institute.util.service.MakeAutoGenerateNumberService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/batchExam" )
public class BatchExamController {
  private final BatchService batchService;
  private final BatchExamService batchExamService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final TeacherService teacherService;
  private final UserService userService;
  private final StudentService studentService;
  private final EmailService emailService;

  public BatchExamController(BatchService batchService, BatchExamService batchExamService,
                             MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                             TeacherService teacherService, UserService userService, StudentService studentService,
                             EmailService emailService) {
    this.batchService = batchService;
    this.batchExamService = batchExamService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.teacherService = teacherService;
    this.userService = userService;
    this.studentService = studentService;
    this.emailService = emailService;
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("batchExams",
                       batchExamService.findAll().stream().filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList()));
    return "batchExam/batchExam";
  }

  @GetMapping( "/teacher" )
  public String findByTeacher(Model model) {
    User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

    if ( user.getTeacher() != null ) {
      model.addAttribute("batchExams",
                         batchExamService.findAll()
                             .stream()
                             .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE) && x.getBatch().getTeacher().equals(user.getTeacher()))
                             .collect(Collectors.toList()));
    } else {
      model.addAttribute("batchExams",
                         batchExamService.findAll()
                             .stream()
                             .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
                             .collect(Collectors.toList()));
    }
    return "batchExam/batchExam";
  }

  @GetMapping( "/add/{id}" )
  public String addForm(@PathVariable Integer id, Model model) {
    Batch batch = batchService.findById(id);
    model.addAttribute("batchDetail", batch);
    BatchExam batchExam = new BatchExam();
    batchExam.setBatch(batch);
    batchExam.setStartAt(LocalDateTime.now());
    model.addAttribute("batchExams", batch.getBatchExams());
    model.addAttribute("batchExam", batchExam);
    model.addAttribute("examstatus", ExamStatus.values());
    return "batchExam/addBatchExam";
  }

  @GetMapping( "/view/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    BatchExam batchExam = batchExamService.findById(id);
    Teacher teacher = teacherService.findById(batchExam.getBatch().getTeacher().getId());
    model.addAttribute("teacherDetail", teacher);
    model.addAttribute("batchExamDetail", batchExam);
    model.addAttribute("subjectDetail", teacher.getSubject());
    return "batchExam/batchExam-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    BatchExam batchExam = batchExamService.findById(id);
    model.addAttribute("batchExams", batchService.findById(batchExam.getBatch().getId()).getBatchExams());
    model.addAttribute("batchDetail", batchExam.getBatch());
    model.addAttribute("batchExam", batchExam);
    model.addAttribute("examstatus", ExamStatus.values());
    model.addAttribute("addStatus", true);
    return "batchExam/addBatchExam";
  }


  @PostMapping("/save")
  public String save(@ModelAttribute BatchExam batchExam, BindingResult bindingResult) {
    if ( bindingResult.hasErrors() ) {
      return "redirect:/batchExam" + batchExam.getBatch().getId();
    }
    if ( batchExam.getId() == null ) {
      BatchExam lastBatchExam = batchExamService.lastBatchExamDB();
      if ( lastBatchExam != null ) {
        String lastNumber = lastBatchExam.getCode().substring(3);
        batchExam.setCode("WIB" + makeAutoGenerateNumberService.numberAutoGen(lastNumber));
      } else {
        batchExam.setCode("WIB" + makeAutoGenerateNumberService.numberAutoGen(null));
      }
    }

    BatchExam batchExamDb = batchExamService.persist(batchExam);
    batchService.findById(batchExamDb.getBatch().getId()).getBatchStudents().forEach(x -> {
      Student student = studentService.findById(x.getStudent().getId());
      if ( student.getEmail() != null ) {
        String message = "Dear " + student.getFirstName() + "\n Your " + batchService.findById( batchExamDb.getBatch().getId()).getName() + " exam " +
            "would be held from " + batchExamDb.getStartAt() + " to " + batchExamDb.getEndAt() + ".\n Thanks \n " +
            "Success Student";
//        emailService.sendEmail(student.getEmail(), "Exam - Notification", message);
      }
    });
    return "redirect:/batchExam/add/";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id) {
    batchExamService.delete(id);
    return "redirect:/batchExam/teacher";
  }
}
