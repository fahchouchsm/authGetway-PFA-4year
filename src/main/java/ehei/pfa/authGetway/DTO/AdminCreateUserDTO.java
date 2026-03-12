package ehei.pfa.authGetway.DTO;

import ehei.pfa.authGetway.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminCreateUserDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private UserRole role;
    private String website;
    private boolean emailVerified;
}