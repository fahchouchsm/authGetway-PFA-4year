package ehei.pfa.authGetway.DTO.res;

import ehei.pfa.authGetway.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResDTO {
    private String id;
    private String email;
    private boolean verifiedEmail;
    private UserRole role;
}
