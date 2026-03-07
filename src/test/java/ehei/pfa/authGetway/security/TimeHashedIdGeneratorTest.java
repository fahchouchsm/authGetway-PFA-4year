package ehei.pfa.authGetway.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeHashedIdGeneratorTest {

    @Test
    void shouldGenerateTimeHashedIdWithExpectedShape() {
        String id = TimeHashedIdGenerator.generate();

        assertNotNull(id);
        String[] parts = id.split("-");
        assertEquals(2, parts.length);
        assertTrue(parts[0].matches("[0-9a-z]+"));
        assertTrue(parts[1].matches("[0-9a-f]{24}"));
    }

    @Test
    void shouldGenerateDifferentIdsAcrossCalls() {
        String id1 = TimeHashedIdGenerator.generate();
        String id2 = TimeHashedIdGenerator.generate();

        assertNotEquals(id1, id2);
    }
}

