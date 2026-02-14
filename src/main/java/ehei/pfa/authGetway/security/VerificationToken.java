package ehei.pfa.authGetway.security;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationToken {
    private static final Duration timeLimit = Duration.ofMinutes(10);

    private static class Entry {
        final long userId;
        final Instant expiredAt;

        public Entry(long userId, Instant expiredAt) {
            this.userId = userId;
            this.expiredAt = expiredAt;
        }
    }

    private final Map<String, Entry> tokens = new ConcurrentHashMap<>();

    public String createToken(long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new Entry(userId, Instant.now().plus(timeLimit)));
        return token;
    }

    public Long consumeToken(String token) {

        Entry entry = tokens.remove(token);

        if (entry == null) {
            throw new InvalidVerificationTokenException("Token invalide.");
        }

        if (Instant.now().isAfter(entry.expiredAt)) {
            throw new InvalidVerificationTokenException("Token expiré.");
        }

        return entry.userId;
    }
}
