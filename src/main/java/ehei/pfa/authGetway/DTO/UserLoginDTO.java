package ehei.pfa.authGetway.DTO.register;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=3, max=50)
    private String password;

    private boolean stayLogin;
}