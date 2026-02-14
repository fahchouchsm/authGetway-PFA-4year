package ehei.pfa.authGetway.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class VerificationToken {

    private static final Duration TTL = Duration.ofMinutes(10);
    private static final String PREFIX = "verify:";

    private final StringRedisTemplate redis;

    public VerificationToken(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public String createToken(long userId) {
        String token = UUID.randomUUID().toString();
        String key = PREFIX + token;

        redis.opsForValue().set(key, String.valueOf(userId), TTL);
        return token;
    }

    public Long consumeToken(String token) {
        String key = PREFIX + token;

        String userIdStr = redis.opsForValue().get(key);
        if (userIdStr == null) {
            throw new InvalidVerificationTokenException("Token invalide ou expiré.");
        }

        redis.delete(key);

        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            redis.delete(key);
            throw new InvalidVerificationTokenException("Token invalide.");
        }
    }
}