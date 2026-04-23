package ehei.pfa.authGetway.DTO;

import ehei.pfa.authGetway.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminCreateUserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private UserRole role;
    private boolean verifiedEmail;
}