package ehei.pfa.authGetway.DTO;

import ehei.pfa.authGetway.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RoleDTO {
    @NotNull
    public UserRole userRole;
}
