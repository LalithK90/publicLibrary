package lk.wasity_institute.asset.teacher.controller.mainwindow;




import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.batch_exam.entity.BatchExam;
import lk.wasity_institute.asset.batch_exam.service.BatchExamService;
import lk.wasity_institute.asset.common_asset.model.DateTimeTable;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.teacher.entity.Teacher;
import lk.wasity_institute.asset.teacher.service.TeacherService;
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
@RequestMapping( "/teacherDetail" )
public class TeacherDetailController {
  private final UserService userService;
  private final BatchExamService batchExamService;
  private final TimeTableService timeTableService;
  private final BatchService batchService;
  private final TeacherService teacherService;

    public TeacherDetailController(UserService userService, BatchExamService batchExamService, TimeTableService timeTableService, BatchService batchService, TeacherService teacherService) {
        this.userService = userService;
        this.batchExamService = batchExamService;
        this.timeTableService = timeTableService;
        this.batchService = batchService;
        this.teacherService = teacherService;
    }


    @GetMapping
    public String teacherMainwindow(Model model) {
        User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        Teacher teacher = teacherService.findById(user.getTeacher().getId());

        model.addAttribute("TeacherDetail", teacher);
        return "teacher/mainWindow";
    }
//
//    @GetMapping( "/timeTable" )
//    public String teacherTimeTable(Model model) {
//        User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
//       Teacher teacher = teacherService.findById(user.getTeacher().getId());
//        List< TimeTable > timeTables = new ArrayList<>();
//
//        batchStudentService.findByStudent(student).forEach(x -> {
//            timeTables.addAll(timeTableService.findByBatch(x.getBatch()));
//        });
//
//        return common(timeTables, model);
//    }
//
//    private String common(List< TimeTable > timeTables, Model model) {
//        HashSet< LocalDate > classDates = new HashSet<>();
//        timeTables.forEach(x -> classDates.add(x.getStartAt().toLocalDate()));
//
//        List< DateTimeTable > dateTimeTables = new ArrayList<>();
//
//        for ( LocalDate classDate : classDates ) {
//            DateTimeTable dateTimeTable = new DateTimeTable();
//            dateTimeTable.setDate(classDate);
//            dateTimeTable.setTimeTables(timeTables.stream().filter(x -> x.getStartAt().toLocalDate().isEqual(classDate)).collect(Collectors.toList()));
//            dateTimeTables.add(dateTimeTable);
//        }
//        model.addAttribute("attendanceStatus", false);
//        model.addAttribute("timeTableMaps", dateTimeTables);
//        return "timeTable/timeTableView";
//    }
//
//    @GetMapping("/exam")
//    public String studentExam(Model model){
//        User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
//        Student student = studentService.findById(user.getStudent().getId());
//        List<BatchExam> batchExams = new ArrayList<>();
//        batchStudentService.findByStudent(student).forEach(x->{
//            batchExams.addAll(batchExamService.findByBatch(x.getBatch()));
//        });
//        model.addAttribute("batchExams",batchExams);
//        return "batchExam/batchExam";
//    }
}
