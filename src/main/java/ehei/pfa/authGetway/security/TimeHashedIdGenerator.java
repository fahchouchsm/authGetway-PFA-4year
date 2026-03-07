package ehei.pfa.authGetway.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;

public final class TimeHashedIdGenerator {

    private TimeHashedIdGenerator() {
    }

    public static String generate() {
        long now = Instant.now().toEpochMilli();
        String timestamp = Long.toString(now, 36);
        String hash = sha256Hex(now + ":" + UUID.randomUUID());

        return timestamp + "-" + hash.substring(0, 24);
    }

    private static String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is not available in this JVM environment", e);
        }
    }
}

