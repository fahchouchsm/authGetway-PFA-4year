package ehei.pfa.authGetway.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDTO {
    private String password;
    private String name;
    private String lastName;
    private String email;
}
