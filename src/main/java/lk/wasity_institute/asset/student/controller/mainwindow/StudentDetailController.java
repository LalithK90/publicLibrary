package lk.wasity_institute.asset.student.controller.mainwindow;


import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.batch_student.service.BatchStudentService;
import lk.wasity_institute.asset.common_asset.model.DateTimeTable;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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

  public StudentDetailController(StudentService studentService, UserService userService,
                                 TimeTableService timeTableService, BatchStudentService batchStudentService,
                                 BatchService batchService) {
    this.studentService = studentService;
    this.userService = userService;
    this.timeTableService = timeTableService;
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

  @GetMapping( "/timeTable" )
  public String studentTimeTable(Model model) {
    User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
    Student student = studentService.findById(user.getStudent().getId());
    List< TimeTable > timeTables = new ArrayList<>();

    batchStudentService.findByStudent(student).forEach(x -> {
      timeTables.addAll(timeTableService.findByBatch(x.getBatch()));
    });

    return common(timeTables, model);
  }

  private String common(List< TimeTable > timeTables, Model model) {
    HashSet< LocalDate > classDates = new HashSet<>();
    timeTables.forEach(x -> classDates.add(x.getStartAt().toLocalDate()));

    List< DateTimeTable > dateTimeTables = new ArrayList<>();

    for ( LocalDate classDate : classDates ) {
      DateTimeTable dateTimeTable = new DateTimeTable();
      dateTimeTable.setDate(classDate);
      dateTimeTable.setTimeTables(timeTables.stream().filter(x -> x.getStartAt().toLocalDate().isEqual(classDate)).collect(Collectors.toList()));
      dateTimeTables.add(dateTimeTable);
    }
    model.addAttribute("attendanceStatus", false);
    model.addAttribute("timeTableMaps", dateTimeTables);
    return "timeTable/timeTableView";
  }
}
