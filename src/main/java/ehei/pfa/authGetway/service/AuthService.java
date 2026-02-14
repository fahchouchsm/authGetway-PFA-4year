package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.RegisterDTO;
import ehei.pfa.authGetway.DTO.UserLoginDTO;
import ehei.pfa.authGetway.DTO.email.RegisterEmailDTO;
import ehei.pfa.authGetway.constant.TIME;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import ehei.pfa.authGetway.enums.UserRole;
import ehei.pfa.authGetway.exception.InvalidCredentialsException;
import ehei.pfa.authGetway.exception.UserAlreadyExistsException;
import ehei.pfa.authGetway.exception.UserNotFoundException;
import ehei.pfa.authGetway.mapper.UserMapper;
import ehei.pfa.authGetway.security.JwtUtil;
import ehei.pfa.authGetway.security.VerificationToken;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.boot.servlet.autoconfigure.ServletEncodingProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;
    private final VerificationToken verificationToken;
    private final MailService mailService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder, UserMapper userMapper, VerificationToken verificationToken, MailService mailService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.verificationToken = verificationToken;
        this.mailService = mailService;
    }

    @Transactional
    public void register(RegisterDTO dto) {

        if (userRepository.existsUserByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        UserRole role = (dto.getRole() == null) ? UserRole.USER : dto.getRole();

        if (role == UserRole.COMPANY) {
            if (dto.getWebsite() == null || dto.getWebsite().trim().isEmpty()) {
                throw new InvalidCredentialsException("Website required for company.");
            }
        }

        User user = userMapper.toEntity(dto);
        user.setRole(role);
        user.setPassword(encoder.encode(dto.getPassword()));

        if (role != UserRole.COMPANY) {
            user.setWebsite(null);
        }

        userRepository.save(user);
        String token = verificationToken.createToken(user.getId());
        String link = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/email").queryParam("token", token).toUriString();
        mailService.sendVerificationEmail(user.getEmail(), new RegisterEmailDTO(link, user.getName(), user.getLastName()));
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