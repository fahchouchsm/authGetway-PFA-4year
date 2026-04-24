package ehei.pfa.authGetway.security;

import ehei.pfa.authGetway.config.AppProperties;
import ehei.pfa.authGetway.constant.TIME;
import ehei.pfa.authGetway.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final AppProperties appProperties;
    private SecretKey signingKey;
    private SecretKey refreshKey;

    @PostConstruct
    public void init() {
        this.signingKey = getSecretKey(appProperties.getJwtSecret());
        this.refreshKey = getSecretKey(appProperties.getJwtRefreshSecret());
    }

    private SecretKey getSecretKey(String secret) {
        byte[] keyBytes;

        try {
            // Try to decode as Base64 first
            keyBytes = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            // If not valid Base64, use as plain text
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        // Ensure key is at least 256 bits (32 bytes)
        if (keyBytes.length < 32) {
            // Pad the key if it's too short (not recommended for production)
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
            // Fill remaining with zeros or repeat pattern
            for (int i = keyBytes.length; i < 32; i++) {
                paddedKey[i] = keyBytes[i % keyBytes.length];
            }
            keyBytes = paddedKey;
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String genToken(String userId, UserRole role, long expMillis) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expMillis))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String genToken(String userId, UserRole role) {
        return genToken(userId, role, TIME.ONEHOUR);
    }

    public String genRefreshToken(String userId, long expMillis) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expMillis))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getRole(Authentication authentication) {
        return authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
    }
}