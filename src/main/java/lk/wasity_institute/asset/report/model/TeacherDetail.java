package lk.wasity_institute.asset.report.model;


import lk.wasity_institute.asset.teacher.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDetail {

private Teacher teacher;
private long paymentCount;
private BigDecimal amount;


}
