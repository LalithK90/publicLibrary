package lk.wasity_institute.asset.batch.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Medium {
  SINHALA("Sinhala"),
  ENGLISH("English");
  private final String medium;
}
