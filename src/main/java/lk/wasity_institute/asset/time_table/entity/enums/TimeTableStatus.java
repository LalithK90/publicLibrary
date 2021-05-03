package lk.wasity_institute.asset.time_table.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeTableStatus {
  MARK("Mark"),
  NOTMARKED("Notmarked");
  private final String timeTableStatus;
}
