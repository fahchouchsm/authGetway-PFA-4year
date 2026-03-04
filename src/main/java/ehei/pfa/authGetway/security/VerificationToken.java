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

    public String createToken(String userId) {
        String token = UUID.randomUUID().toString();
        String key = PREFIX + token;

        redis.opsForValue().set(key, userId, TTL);
        return token;
    }

    public String consumeToken(String token) {
        String key = PREFIX + token;

        String userId = redis.opsForValue().get(key);
        if (userId == null || userId.isBlank()) {
            throw new InvalidVerificationTokenException("Token invalide ou expiré.");
        }

        redis.delete(key);
        return userId;
    }
}