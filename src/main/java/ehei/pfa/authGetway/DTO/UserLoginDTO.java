package ehei.pfa.authGetway.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLoginDTO {
    private String email;
    private String password;
    private boolean stayLogin;
}