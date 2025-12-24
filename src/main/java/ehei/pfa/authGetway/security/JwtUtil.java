package ehei.pfa.authGetway.security;

import ehei.pfa.authGetway.constant.TIME;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String genToken(String userId) {
        return Jwts.builder().setSubject(userId).setExpiration(new Date(System.currentTimeMillis() + TIME.ONEDAY)).signWith(key).compact();
    }

    public static String genToken(String userId, long exp) {
        return Jwts.builder().setSubject(userId).setExpiration(new Date(System.currentTimeMillis() + exp)).signWith(key).compact();
    }

    public static String validateToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}