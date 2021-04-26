package lk.wasity_institute.asset.hall.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.hall.entity.enums.HallCondition;
import lk.wasity_institute.asset.time_table.entity.TimeTable;
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
@JsonFilter("Hall")
public class Hall extends AuditEntity {

    @Enumerated( EnumType.STRING )
    private LiveDead liveDead;

//    private String name;

    @Column( unique = true )
    private String number;

    @Enumerated( EnumType.STRING )
    private HallCondition hallCondition;

    private String seatCount;

    private String hallLocation;

    @OneToMany( mappedBy = "hall" )
    private List< TimeTable > timeTables;

}
