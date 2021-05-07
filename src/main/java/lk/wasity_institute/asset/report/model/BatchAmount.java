package lk.wasity_institute.asset.report.model;


import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.teacher.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchAmount {
private Batch batch;
public Teacher teacher;
public Student student;
private BigDecimal amount;
private long count;
private BigDecimal fee;



}
