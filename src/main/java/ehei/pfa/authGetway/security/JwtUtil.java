package ehei.pfa.authGetway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long exp = 24 * 1000 * 60 * 60; // 1 day

    public static String genToken(String userId) {
        return Jwts.builder().setSubject(userId).setExpiration(new Date(System.currentTimeMillis() + exp)).signWith(key).compact();
    }

    public static String validateToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
