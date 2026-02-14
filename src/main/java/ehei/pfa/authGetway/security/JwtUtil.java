package ehei.pfa.authGetway.security;

import ehei.pfa.authGetway.constant.TIME;
import ehei.pfa.authGetway.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.security.*;
import java.util.Date;

public class JwtUtil {
    private static final KeyPair keyPair = generateKeyPair();
    private static final PrivateKey privateKey = keyPair.getPrivate();
    @Getter
    private static final PublicKey publicKey = keyPair.getPublic();

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate RS256 key pair", ex);
        }
    }

    public static String genToken(String userId, UserRole role, long expMillis) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(userId)
                .claim("id",userId)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expMillis))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public static String genToken(String userId, UserRole role) {
        return genToken(userId, role, TIME.ONEDAY);
    }

    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}