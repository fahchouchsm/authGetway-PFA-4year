package ehei.pfa.authGetway.mapper;

import ehei.pfa.authGetway.DTO.RegisterDTO;
import ehei.pfa.authGetway.DTO.UserLoginDTO;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterDTO dto) {
        User user = new User();
        user.setLastName(dto.getLastName());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setWebsite(dto.getWebsite());
        return user;
    }

    public UserLoginDTO fromUserRegisterToUserLogin(RegisterDTO dto) {
        return new UserLoginDTO(dto.getEmail(), dto.getPassword(), true);
    }
}
