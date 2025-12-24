package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.UserRegisterDTO;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import ehei.pfa.authGetway.exception.UserAlreadyExistsException;
import ehei.pfa.authGetway.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public void register(UserRegisterDTO dto) {
        if(userRepository.existsUserByEmail((dto.getEmail()))){
            throw new UserAlreadyExistsException("Email already in use");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        userRepository.save(user);

    }

}