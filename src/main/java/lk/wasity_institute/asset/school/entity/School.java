package lk.wasity_institute.asset.school.entity;


import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45, unique = true)
    private String name;

    @Enumerated( EnumType.STRING)
    private LiveDead liveDead;



}
