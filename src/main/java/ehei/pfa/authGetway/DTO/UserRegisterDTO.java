package ehei.pfa.authGetway.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank
    @Size(min=3, max=50)
    private String name;
    @NotBlank
    @Size(min=3, max=50)
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=8, max=50)
    private String password;
}
