package lk.wasity_institute.asset.report.model;


import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.teacher.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherBatch {
    private Teacher teacher;
    private Batch batch;
    long Count;
}
