package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.email.RegisterEmailDTO;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import ehei.pfa.authGetway.exception.UserNotFoundException;
import ehei.pfa.authGetway.security.VerificationToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MailService mailService;
    private final VerificationToken verificationToken;
    private final UserRepository userRepository;

    public void sendVerificationEmail(User user) {
        String token = verificationToken.createToken(user.getId());
        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/verify/email")
                .queryParam("token", token)
                .toUriString();
        mailService.sendVerificationEmail(user.getEmail(), new RegisterEmailDTO(link, user.getName(), user.getLastName()));
    }

    @Transactional
    public void verifyEmailLink(String token) {
        String userId = verificationToken.consumeToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setEmailVerified(true);
        userRepository.save(user);
    }
}