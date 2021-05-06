package lk.wasity_institute.asset.student.controller.mainwindow;


import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.batch_student.service.BatchStudentService;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.student.service.StudentService;
import lk.wasity_institute.asset.time_table.entity.TimeTable;
import lk.wasity_institute.asset.time_table.service.TimeTableService;
import lk.wasity_institute.asset.user_management.entity.User;
import lk.wasity_institute.asset.user_management.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/studentDetail" )
public class StudentDetailController {
  private final StudentService studentService;
  private final UserService userService;
private final TimeTableService timeTableService;
private final BatchStudentService batchStudentService;
private final BatchService batchService;
  public StudentDetailController(StudentService studentService, UserService userService, TimeTableService timeTableService, BatchStudentService batchStudentService,BatchService batchService) {
    this.studentService = studentService;
    this.userService = userService;
    this.timeTableService= timeTableService;
    this.batchStudentService = batchStudentService;
    this.batchService = batchService;
  }


  @GetMapping
  public String studentMainwindow(Model model) {
    User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
    Student student = studentService.findById(user.getStudent().getId());

    model.addAttribute("studentDetail", student);
    return "student/mainWindow";
  }

//  @GetMapping("/timeTable")
//  public String studentTimeTable(Model model){
//    User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
//    Student student = studentService.findById(user.getStudent().getId());
//Batch batch = batchService.findAll();
//List<BatchStudent> batchStudent = batchStudentService.findAll().stream().filter(x->x.getStudent().equals(student)).collect(Collectors.toList());
//
//     model.addAttribute("batchStudents",batchStudent);
//     return "student/studentTimeTable";
//  }

}
