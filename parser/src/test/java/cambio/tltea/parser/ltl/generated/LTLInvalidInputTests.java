package cambio.tltea.parser.ltl.generated;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Lion Wagner
 */
public class LTLInvalidInputTests {

    private static void assertThrowsException(String s) {
        LTLParser parser = new LTLParser(s);
        Exception ex = assertThrows(Exception.class, parser::LTL_Formula_File);
        assertTrue(ex instanceof ParseException || ex instanceof TokenMgrException);
        parser.ReInit(s);
    }


    @Test
    void throws_exception_on_empty_input() {
        assertThrowsException("");
    }

    @Test
    void throws_exception_on_empty_brackets() {
        assertThrowsException("()");

        for (int i = 0; i < 3; i++) {
            assertThrowsException("()".repeat(new Random().nextInt(Integer.MAX_VALUE / 8)));
        }
    }

    @Test
    void throws_exception_on_invalid_input_1() {
        assertThrowsException("((A)(B))");
    }

    @Test
    void throws_exception_on_invalid_input_2() {
        assertThrowsException("||(A)");
    }

    @Test
    void throws_exception_on_empty_operator() {
        assertThrowsException("(A)&&");
        assertThrowsException("(A)U");
        assertThrowsException("U(A)");
        assertThrowsException("G");
        assertThrowsException("F");
    }

    @Test
    void throws_exception_on_unary_as_binary() {
        assertThrowsException("(A)G(B)");
    }

    @Test
    void throws_exception_on_binary_as_unary() {
        assertThrowsException("U(B)");
    }


    @Test
    void throws_exception_on_brace_in_proposition() {
        assertThrowsException("((A)");
        assertThrowsException("F(AB(EF)");
        assertThrowsException("F(AB(EF))");
        assertThrowsException("F((AB(EF))");
        assertThrowsException("F((AB)EF))");
    }

    @Test
    void throws_exception_on_double_negate_empty() {
        assertThrowsException("!!");
        assertThrowsException("(a)&!!");
    }
}
