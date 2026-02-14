package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.RegisterDTO;
import ehei.pfa.authGetway.DTO.UserLoginDTO;
 import ehei.pfa.authGetway.constant.TIME;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import ehei.pfa.authGetway.enums.UserRole;
import ehei.pfa.authGetway.exception.InvalidCredentialsException;
import ehei.pfa.authGetway.exception.UserAlreadyExistsException;
import ehei.pfa.authGetway.exception.UserNotFoundException;
import ehei.pfa.authGetway.mapper.UserMapper;
import ehei.pfa.authGetway.security.JwtUtil;
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
    public void register(RegisterDTO dto) {

        if (userRepository.existsUserByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        UserRole role = (dto.getRole() == null) ? UserRole.USER : dto.getRole();

        if (role == UserRole.COMPANY) {
            if (dto.getWebsite() == null || dto.getWebsite().trim().isEmpty()) {
                throw new InvalidCredentialsException("Website required for company."); // or your own exception
            }
            if (!dto.getWebsite().startsWith("http://") && !dto.getWebsite().startsWith("https://")) {
                throw new InvalidCredentialsException("Website must start with http:// or https://");
            }
        }

        User user = userMapper.toEntity(dto);
        user.setRole(role);
        user.setPassword(encoder.encode(dto.getPassword()));

        if (role != UserRole.COMPANY) {
            user.setWebsite(null); // keep DB clean
        }

        userRepository.save(user);
    }

    @Transactional
    public String login(UserLoginDTO dto) {
        User user = userRepository.findByEmail((dto.getEmail()));
        if(user == null) {
            throw new UserNotFoundException("User with " + dto.getEmail() + " mail not found.");
        }

        if(!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        if(dto.isStayLogin()){
            return JwtUtil.genToken(String.valueOf(user.getId()), user.getRole(), TIME.ONEMONTH);
        }
        return JwtUtil.genToken(String.valueOf(user.getId()), user.getRole());
    }
}