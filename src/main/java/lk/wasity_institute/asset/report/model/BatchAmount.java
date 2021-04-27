package lk.wasity_institute.asset.report.model;

import lk.wasity_institute.asset.batch.entity.Batch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchAmount {
private Batch batch;
private BigDecimal amount;
private long count;


}
