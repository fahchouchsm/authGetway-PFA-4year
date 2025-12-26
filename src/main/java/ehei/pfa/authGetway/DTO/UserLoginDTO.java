package ehei.pfa.authGetway.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {
    private String email;
    private String password;
    private boolean stayLogin;
}