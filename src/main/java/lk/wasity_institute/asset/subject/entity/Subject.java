package lk.wasity_institute.asset.subject.entity;


import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.teacher.entity.Teacher;
import lk.wasity_institute.util.audit.AuditEntity;
import lombok.Getter;

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
