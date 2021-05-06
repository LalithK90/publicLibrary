package lk.wasity_institute.asset.employee.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Designation {
  ADMIN("Admin"),
    MANAGER("Manager"),
    RECEPTIONIST("Receptionist");

    private final String designation;
}
