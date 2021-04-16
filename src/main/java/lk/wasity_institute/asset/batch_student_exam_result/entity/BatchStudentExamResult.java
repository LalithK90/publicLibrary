package lk.wasity_institute.asset.batch_student_exam_result.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasity_institute.asset.batch_exam.entity.BatchExam;
import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.common_asset.model.enums.AttendanceStatus;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.common_asset.model.enums.ResultGrade;
import lk.wasity_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "BatchStudentExamResult" )
public class BatchStudentExamResult extends AuditEntity {

  @Column( unique = true )
  private String code;

  private String remark;

  private Integer mark;

  @Enumerated( EnumType.STRING )
  private ResultGrade resultGrade;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @Enumerated( EnumType.STRING )
  private AttendanceStatus attendanceStatus;

  @ManyToOne
  private BatchStudent batchStudent;

  @ManyToOne
  private BatchExam batchExam;

}
