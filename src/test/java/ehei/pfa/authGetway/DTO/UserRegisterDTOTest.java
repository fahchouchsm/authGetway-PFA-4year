package ehei.pfa.authGetway.DTO;

import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRegisterDTOTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void userPasswordShouldBeEncryptedOnSave() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("MySecret123"));
        userRepository.save(user);

        User saved = userRepository.findById(user.getId()).orElseThrow();
        assertNotEquals("MySecret123", saved.getPassword());
        assertTrue(passwordEncoder.matches("MySecret123", saved.getPassword()));
    }
}
