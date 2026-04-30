package ehei.pfa.authGetway.DTO.res;

import ehei.pfa.authGetway.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResDTO {
    private String id;
    private String email;
    private boolean verifiedEmail;
    private String name;
    private String lastName;
    private UserRole role;
    private String accessToken;
}