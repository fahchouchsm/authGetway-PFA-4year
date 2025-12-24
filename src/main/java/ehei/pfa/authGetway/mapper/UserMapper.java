package ehei.pfa.authGetway.mapper;

import ehei.pfa.authGetway.DTO.UserRegisterDTO;
import ehei.pfa.authGetway.database.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRegisterDTO dto) {
        User user = new User();
        user.setLastName(dto.getLastName());
        user.setName(dto.getName());
        // no password because the service will hash it
        return user;
    }
}
