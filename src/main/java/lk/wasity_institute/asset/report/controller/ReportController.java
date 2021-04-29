package lk.wasity_institute.asset.report.controller;

import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.batch_exam.entity.BatchExam;
import lk.wasity_institute.asset.batch_exam.service.BatchExamService;
import lk.wasity_institute.asset.batch_student_exam_result.entity.BatchStudentExamResult;
import lk.wasity_institute.asset.common_asset.model.Message;
import lk.wasity_institute.asset.common_asset.model.TwoDate;
import lk.wasity_institute.asset.common_asset.model.enums.AttendanceStatus;
import lk.wasity_institute.asset.common_asset.model.enums.ResultGrade;
import lk.wasity_institute.asset.payment.entity.Payment;
import lk.wasity_institute.asset.payment.service.PaymentService;
import lk.wasity_institute.asset.report.model.BatchAmount;
import lk.wasity_institute.asset.report.model.BatchExamResultStudent;
import lk.wasity_institute.asset.report.model.StudentAmount;
import lk.wasity_institute.asset.school.entity.School;
import lk.wasity_institute.asset.school.service.SchoolService;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.student.service.StudentService;
import lk.wasity_institute.util.service.DateTimeAgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/report" )
public class ReportController {
  private final PaymentService paymentService;
  private final BatchExamService batchExamService;
  private final DateTimeAgeService dateTimeAgeService;
  private final BatchService batchService;
  private final StudentService studentService;
  private final SchoolService schoolService;


  public ReportController(PaymentService paymentService, BatchExamService batchExamService,
                          DateTimeAgeService dateTimeAgeService, BatchService batchService,
                          StudentService studentService,SchoolService schoolService) {
    this.paymentService = paymentService;
    this.batchExamService = batchExamService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.batchService = batchService;
    this.studentService = studentService;
    this.schoolService = schoolService;
  }

  private String commonIncomeReport(Model model, LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate);
//    System.out.println(" astar "+startDateTime + "  end "+endDateTime);
    List< Payment > payments = paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime);

    List< BigDecimal > totalPaymentAmount = new ArrayList<>();
    List< Batch > batches = new ArrayList<>();
    List< Student > students = new ArrayList<>();
    payments.forEach(x -> {
      totalPaymentAmount.add(x.getAmount());
      batches.add(x.getBatchStudent().getBatch());
      students.add(x.getBatchStudent().getStudent());
    });
    List< BatchAmount > batchAmounts = new ArrayList<>();
    batches.stream().distinct().collect(Collectors.toList()).forEach(x -> {
      List< Payment > batchPayments =
          payments.stream().filter(y -> y.getBatchStudent().getBatch().equals(x)).collect(Collectors.toList());
      List< BigDecimal > batchPaymentAmounts = new ArrayList<>();
      batchPayments.forEach(y -> batchPaymentAmounts.add(y.getAmount()));
      BatchAmount batchAmount = new BatchAmount();
      batchAmount.setAmount(batchPaymentAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
      batchAmount.setCount(batchPayments.size());
      batchAmount.setBatch(batchService.findById(x.getId()));
      batchAmounts.add(batchAmount);
    });
    List< StudentAmount > studentAmounts = new ArrayList<>();
    students.stream().distinct().collect(Collectors.toList()).forEach(x -> {
      List< Payment > studentPayments =
          payments.stream().filter(y -> y.getBatchStudent().getStudent().equals(x)).collect(Collectors.toList());
      List< BigDecimal > studentPaymentAmounts = new ArrayList<>();
      studentPayments.forEach(y -> studentPaymentAmounts.add(y.getAmount()));
      StudentAmount studentAmount = new StudentAmount();
      studentAmount.setAmount(studentPaymentAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
      studentAmount.setCount(studentPaymentAmounts.size());
      studentAmount.setStudent(studentService.findById(x.getId()));
      studentAmounts.add(studentAmount);
    });


    model.addAttribute("batchAmounts", batchAmounts);
    model.addAttribute("studentAmounts", studentAmounts);
    model.addAttribute("payments", payments);
    model.addAttribute("paymentCount", payments.size());
    model.addAttribute("paymentAmount", totalPaymentAmount.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
    String message = "This report is belongs from " + startDate + " to " + endDate;
    model.addAttribute("message", message);
    return "report/incomeReport";
  }

  @GetMapping( "/income" )
  public String todayReportIncome(Model model) {
    return commonIncomeReport(model, LocalDate.now(), LocalDate.now());
  }

  @PostMapping( "/income" )
  public String todayReportIncome(@ModelAttribute TwoDate twoDate, Model model) {
    return commonIncomeReport(model, twoDate.getStartDate(), twoDate.getEndDate());
  }

  @GetMapping( "/batchExam" )
  public String batchExam(Model model) {
    LocalDate today = LocalDate.now();

    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDayWithOutNano(today);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDayWithOutNano(today.minusDays(7));
    List< BatchExam > batchExams = batchExamService.findByStartAtBetween(startDateTime, endDateTime);
    String message = "This report is belongs from " + today.minusDays(7) + " to " + today;
    return commonExam(model, batchExams, message);
  }

  @PostMapping( "/batchExam" )
  public String batchExam(@ModelAttribute TwoDate twoDate, Model model) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDayWithOutNano(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDayWithOutNano(twoDate.getEndDate());
    List< BatchExam > batchExams;
    if ( twoDate.getId() == null ) {
      batchExams = batchExamService.findByStartAtBetween(startDateTime,
                                                         endDateTime);
    } else {
      Batch batch = batchService.findById(twoDate.getId());
      batchExams = batchExamService.findByStartAtBetweenAndBatch(startDateTime,
                                                                 endDateTime, batch);
      model.addAttribute("batchDetail", batch);
    }

    String message = "This report is belongs from " + twoDate.getStartDate() + " to " + twoDate.getEndDate();
    return commonExam(model, batchExams, message);
  }

  private String commonExam(Model model, List< BatchExam > batchExams, String message) {
    List< BatchExamResultStudent > batchExamResultStudents = new ArrayList<>();
    for ( BatchExam batchExam : batchExams ) {
      BatchExamResultStudent batchExamResultStudent = new BatchExamResultStudent();
      batchExamResultStudent.setBatchExam(batchExam);
      batchExamResultStudent.setBatch(batchService.findById(batchExam.getBatch().getId()));
      List< BatchStudentExamResult > batchStudentExamResults =
          batchExamService.findById(batchExam.getId()).getBatchStudentExamResults();

      List<BatchStudentExamResult> presentBatchStudentExamResults = batchStudentExamResults.stream().filter(x -> x.getAttendanceStatus().equals(AttendanceStatus.PRE)).collect(Collectors.toList());
      batchExamResultStudent.setAttendCount(presentBatchStudentExamResults.size());
      List< Student > presentStudents = new ArrayList<>();
      presentBatchStudentExamResults.forEach(x->presentStudents.add(studentService.findById(x.getBatchStudent().getStudent().getId())));
      batchExamResultStudent.setAttendStudents(presentStudents);
      List< Student > absentStudents = new ArrayList<>();
      List<BatchStudentExamResult> absentBatchStudentExamResults = batchStudentExamResults.stream().filter(x -> x.getAttendanceStatus().equals(AttendanceStatus.AB)).collect(Collectors.toList());
      absentBatchStudentExamResults.forEach(x->absentStudents.add(studentService.findById(x.getBatchStudent().getStudent().getId())));
      batchExamResultStudent.setAbsentCount(absentBatchStudentExamResults.size());
      batchExamResultStudent.setAbsentStudents(absentStudents);


      List< Student > aPlusStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.AP);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> aPlusStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));
      model.addAttribute("aPlusStudents", aPlusStudents);
      batchExamResultStudent.setAPlusStudents(aPlusStudents);
      List< Student > aStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.A);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> aStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));
      model.addAttribute("aStudents", aStudents);
      batchExamResultStudent.setAStudents(aStudents);
      List< Student > aMinusStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.AM);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> aMinusStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));
      model.addAttribute("aMinusStudents", aMinusStudents);
      batchExamResultStudent.setAMinusStudents(aMinusStudents);
      List< Student > bPlusStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.BP);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> bPlusStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));
      model.addAttribute("bPlusStudents", bPlusStudents);
      batchExamResultStudent.setBPlusStudents(bPlusStudents);
      List< Student > bStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.B);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> bStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));
      model.addAttribute("bStudents", bStudents);
      batchExamResultStudent.setBStudents(bStudents);
      List< Student > bMinusStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.BM);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> bMinusStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));
      model.addAttribute("bMinusStudents", bMinusStudents);
      batchExamResultStudent.setBMinusStudents(bMinusStudents);
      List< Student > cPlusStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.CP);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> cPlusStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));

      batchExamResultStudent.setCPlusStudents(cPlusStudents);

      List< Student > cStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.C);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> cStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));

      batchExamResultStudent.setCStudents(cStudents);

      List< Student > cMinusStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.CM);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> cMinusStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));

      batchExamResultStudent.setCMinusStudents(cMinusStudents);

      List< Student > dPlusStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.DP);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> dPlusStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));

      batchExamResultStudent.setDPlusStudents(dPlusStudents);
      List< Student > dStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.D);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> dStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));

      batchExamResultStudent.setDStudents(dStudents);

      List< Student > eStudents = new ArrayList<>();
      batchStudentExamResults.stream().filter(x -> {
        if ( x.getResultGrade() != null ) {
          return x.getResultGrade().equals(ResultGrade.E);
        } else {
          return false;
        }
      }).collect(Collectors.toList()).forEach(batchStudentExamResult -> eStudents.add(studentService.findById(batchStudentExamResult.getBatchStudent().getStudent().getId())));

      batchExamResultStudent.setEStudents(eStudents);
      batchExamResultStudents.add(batchExamResultStudent);
    }
    model.addAttribute("batchExamResultStudents", batchExamResultStudents);
    model.addAttribute("message", message);
    model.addAttribute("batchExams", batchExamService.findAll());
    return "report/batchExamReport";
  }


//  @GetMapping("/studentSchool")
//  public String StudentSchool(Model model){
//model.addAttribute("school",schoolService.findAll());
//return "report/studentSchoolReport";
//
//@PostMapping("/studentSchool")
//public String StudentSchool(@ModelAttribute,TwoDate twoDate,Model model){
//  List<Student> students= studentService.findAll();
//LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
//LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
//}
//  }


}


