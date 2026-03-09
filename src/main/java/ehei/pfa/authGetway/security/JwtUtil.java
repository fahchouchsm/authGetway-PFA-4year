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
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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
        signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(appProperties.getJwtSecret()));
        refreshKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(appProperties.getJwtRefreshSecret()));
    }

    public String genToken(String userId, UserRole role, long expMillis) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .claim("id", userId)
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
}