package lk.wasity_institute.asset.student.controller.mainwindow;


import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.student.service.StudentService;
import lk.wasity_institute.asset.user_management.entity.User;
import lk.wasity_institute.asset.user_management.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/studentDetail" )
public class StudentDetailController {
  private final StudentService studentService;
  private final UserService userService;

  public StudentDetailController(StudentService studentService, UserService userService) {
    this.studentService = studentService;
    this.userService = userService;
  }


  @GetMapping
  public String studentMainwindow(Model model) {
    User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
    Student student = studentService.findById(user.getStudent().getId());

    model.addAttribute("studentDetail", student);
    return "student/student-detail";
  }
}
