package ehei.pfa.authGetway.DTO.res;

import ehei.pfa.authGetway.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MeDTO {
    private String name;
    private String lastName;
    private String email;

    private boolean emailVerified;

    private String pfpLink;

    private UserRole role;

    private String website;

    public MeDTO(String name, String lastName, String email, boolean emailVerified, String pfpLink, UserRole role) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.pfpLink = pfpLink;
        this.role = role;
    }

    public MeDTO(String name, String lastName, String email, boolean emailVerified, String pfpLink, UserRole role, String website) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.pfpLink = pfpLink;
        this.role = role;
        this.website = website;
    }
}
