package lk.wasity_institute.asset.student.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasity_institute.asset.batch.entity.enums.Grade;
import lk.wasity_institute.asset.batch.entity.enums.Medium;
import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.common_asset.model.enums.Gender;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.school.entity.School;
import lk.wasity_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Student" )
public class Student extends AuditEntity {

  private String regNo;

  private String firstName;

  private String lastName;

  @Enumerated( EnumType.STRING )
  private Gender gender;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate dateOfBirth;

  private String address;

  @Size( max = 12, min = 10, message = "NIC number is contained numbers between 9 and X/V or 12 " )
  @Column( unique = true )
  private String nic;

  private String city;

  private String guardian;

  private String mobile;

  private String home;

  @Column( unique = true )
  private String email;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @Enumerated( EnumType.STRING )
  private Grade grade;

  @Enumerated( EnumType.STRING )
  private Medium medium;

  @ManyToOne
  private School school;

  @OneToMany(mappedBy = "student",cascade ={ CascadeType.MERGE, CascadeType.PERSIST})
  private List<BatchStudent> batchStudents;


}
