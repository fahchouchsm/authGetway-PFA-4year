package ehei.pfa.authGetway.security;

import ehei.pfa.authGetway.constant.TIME;
import ehei.pfa.authGetway.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import java.security.*;
import java.util.Date;

public class JwtUtil {
    private static final KeyPair keyPair = generateKeyPair();
    private static final PrivateKey privateKey = keyPair.getPrivate();
    @Getter
    private static final PublicKey publicKey = keyPair.getPublic();
    // refresh token
    private static final Key refreshKey = generateHS256Key();

    private static Key generateHS256Key() {
        try {
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate HS256 key", ex);
        }
    }

    public static String genRefreshToken(String userId, long exp) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String validateRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
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
        return genToken(userId, role, TIME.ONEHOUR);
    }

    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}