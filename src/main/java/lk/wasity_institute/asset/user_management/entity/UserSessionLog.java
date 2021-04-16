package lk.wasity_institute.asset.user_management.entity;


import lk.wasity_institute.asset.user_management.entity.Enum.UserSessionLogStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionLog {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    private int failureAttempts;

    @Column( updatable = false, nullable = false )
    private LocalDateTime createdAt;

    @Enumerated( EnumType.STRING )
    private UserSessionLogStatus userSessionLogStatus;

    @ManyToOne
    private User user;
}
