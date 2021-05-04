package lk.wasity_institute.asset.report.model;

import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.time_table.entity.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableStudents {
    private TimeTable timeTable;
    private Student student;
    long count;
}
