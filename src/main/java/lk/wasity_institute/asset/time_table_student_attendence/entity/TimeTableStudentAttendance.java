package lk.wasity_institute.asset.time_table_student_attendence.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.common_asset.model.enums.AttendanceStatus;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.time_table.entity.TimeTable;
import lk.wasity_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "TimeTableStudentAttendance" )
public class TimeTableStudentAttendance extends AuditEntity {

  @Column( unique = true )
  private String code;

  @Enumerated( EnumType.STRING )
  private AttendanceStatus attendanceStatus;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @ManyToOne
  private BatchStudent batchStudent;

  @ManyToOne
  private TimeTable timeTable;
}
