package lk.wasity_institute.asset.report.model;


import lk.wasity_institute.asset.school.entity.School;
import lk.wasity_institute.asset.student.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSchool {
    private Student student;
    private long count;
    private School school;
}
