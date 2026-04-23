package ehei.pfa.authGetway.DTO;

import lombok.Data;

@Data
public class UpdateUserByAdminDTO {
    private String email;
    private String password;
    private Boolean verifiedEmail;
}
