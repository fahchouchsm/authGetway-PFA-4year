package ehei.pfa.authGetway.database.entity;

import ehei.pfa.authGetway.enums.UserRole;
import ehei.pfa.authGetway.security.TimeHashedIdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @Column(nullable = false, updatable = false, length = 64)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean verifiedEmail = false;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @PrePersist
    void setDefault() {
        if (id == null || id.isBlank()) {
            id = TimeHashedIdGenerator.generate();
        }
        if (role == null) {
            role = UserRole.USER;
        }
    }

}
