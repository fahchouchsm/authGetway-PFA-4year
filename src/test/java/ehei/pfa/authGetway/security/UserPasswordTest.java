package ehei.pfa.authGetway.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class UserPasswordTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void passwordShouldBeEncrypted() {
        // GIVEN a plain password
        String rawPassword = "MySecret123";

        // WHEN we encode it
        String encodedPassword = encoder.encode(rawPassword);

        // THEN the encoded password should not equal the raw one
        assertNotEquals(rawPassword, encodedPassword, "Password must be encrypted");

        // AND the password matches when checked
        assertTrue(encoder.matches(rawPassword, encodedPassword), "Encoded password should match raw one");
    }
}
