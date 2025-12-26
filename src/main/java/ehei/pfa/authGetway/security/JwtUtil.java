package ehei.pfa.authGetway.security;

import ehei.pfa.authGetway.constant.TIME;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Date;

public class JwtUtil {
    private static final KeyPair keyPair = generateKeyPair();
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate RS256 key pair", ex);
        }
    }


    public static String genToken(String userId) {
        return Jwts.builder().setSubject(userId).setExpiration(new Date(System.currentTimeMillis() + TIME.ONEDAY)).signWith(key).compact();
    }

    public static String genToken(String userId, long exp) {
        return Jwts.builder().setSubject(userId).setExpiration(new Date(System.currentTimeMillis() + exp)).signWith(key).compact();
    }
}