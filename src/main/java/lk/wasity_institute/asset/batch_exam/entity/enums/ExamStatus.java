package lk.wasity_institute.asset.batch_exam.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamStatus {
  RESULTED("Resulted"),
  PENDING("Pending"),
  DETAIL("Detail");

  private final String examStatus;
}
