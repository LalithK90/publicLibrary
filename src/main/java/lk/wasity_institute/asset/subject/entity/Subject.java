package lk.wasity_institute.asset.subject.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.teacher.entity.Teacher;
import lk.wasity_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Subject" )
public class Subject extends AuditEntity {

  @Column( unique = true )
  private String code;

  @Column( unique = true )
  private String name;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @OneToMany( mappedBy = "subject" )
  private List< Teacher > teachers;


}
