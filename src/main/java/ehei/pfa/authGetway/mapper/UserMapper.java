package ehei.pfa.authGetway.mapper;

import ehei.pfa.authGetway.DTO.RegisterDTO;
import ehei.pfa.authGetway.DTO.UserLoginDTO;
import ehei.pfa.authGetway.DTO.res.RegisterResDTO;
import ehei.pfa.authGetway.database.entity.User;
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

    public RegisterResDTO toRegisterRes(User user, String accessToken) {
        return new RegisterResDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.isEmailVerified(),
                user.getRole(),
                user.getWebsite(),
                accessToken
        );
    }
}
