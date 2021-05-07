package lk.wasity_institute.asset.report.model;


import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.time_table_student_attendence.entity.TimeTableStudentAttendance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchStudentAttendance {
  private Batch batch;
  private List< TimeTableStudentAttendance > timeTableStudentAttendances;
}
