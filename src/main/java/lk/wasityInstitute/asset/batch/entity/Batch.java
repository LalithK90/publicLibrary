package lk.wasityInstitute.asset.batch.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.hall.entity.Hall;
import lk.wasityInstitute.asset.student.entity.Student;
import lk.wasityInstitute.asset.subject.entity.Subject;
import lk.wasityInstitute.util.audit.AuditEntity;
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
@JsonFilter( "Batch" )
public class Batch extends AuditEntity {

    private String classReferance;
    private String grade;
    private String year;
    private String subjectName;
    private String teacherName;

    @Enumerated(EnumType.STRING)
    private LiveDead liveDead;

    @ManyToOne
    private Subject subject;

//so many student on one batch
    @OneToMany(mappedBy = "batch")
    private List< Student > students;

    @ManyToMany
    @JoinTable( name = "batch_hall",
            joinColumns = @JoinColumn( name = "batch_id" ),
            inverseJoinColumns = @JoinColumn( name = "hall_id" ) )
    private List< Hall > halls;

}
