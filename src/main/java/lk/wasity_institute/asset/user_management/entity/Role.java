package lk.wasity_institute.asset.user_management.entity;

import lk.wasity_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AuditEntity {

    @Column( unique = true )
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List< User > users;

    public String getName() {
        return null;
    }
}
