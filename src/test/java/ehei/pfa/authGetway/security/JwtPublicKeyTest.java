package ehei.pfa.authGetway.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtPublicKeyTest {

    @Test
    public void testPublicKeyNotNull() {
        assertNotNull(JwtUtil.getPublicKey(), "Public key should not be null");
        System.out.println("Public Key: " + JwtUtil.getPublicKey());
    }

    @Test
    public void testVerifySampleToken() {
        // generate a token
        String token = JwtUtil.genToken("12345", 60000); // 1 min expiry
        System.out.println("Token: " + token);

        // decode the token using public key
        String userId = JwtUtil.validateToken(token);
        assertEquals("12345", userId, "User ID should match");
    }
}
