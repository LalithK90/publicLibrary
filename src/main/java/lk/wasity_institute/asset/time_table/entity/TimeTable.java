package lk.wasity_institute.asset.time_table.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasity_institute.asset.time_table.entity.enums.TimeTableStatus;
import lk.wasity_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "TimeTable" )
public class TimeTable extends AuditEntity {

  @Column( unique = true )
  private String code;

  private String lesson;

  private String remark;

  @Enumerated(EnumType.STRING)
  private TimeTableStatus timeTableStatus;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime startAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime endAt;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @ManyToOne
  private Batch batch;

  @ManyToOne
  private Hall hall;

  @OneToMany( mappedBy = "timeTable", cascade = CascadeType.PERSIST )
  private List< TimeTableStudentAttendance > timeTableStudentAttendances;
}
