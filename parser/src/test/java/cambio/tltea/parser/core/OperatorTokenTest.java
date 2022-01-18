package cambio.tltea.parser.core;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperatorTokenTest {

    @Test
    void createsUNKNOWN() {
        var image = RandomStringUtils.randomAlphanumeric(10,20);
        OperatorToken token = OperatorTokenImageMap.INSTANCE.getToken(image);
        assertEquals(image, token.image());
        assertEquals(image, token.getImage());
        assertEquals(OperatorToken.UNKNOWN, token);
        assertSame(OperatorToken.UNKNOWN, token);
    }
}