package cambio.tltea.parser.mtl.generated;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.ltl.generated.LTLParser;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Lion Wagner
 */
public class MTLInvalidInputTests {

    private static void assertThrowsException(String s) {
        MTLParser parser = new MTLParser(s);
        Exception ex = assertThrows(Exception.class, parser::parse);
        assertTrue(ex instanceof ParseException || ex instanceof TokenMgrException);
        parser.ReInit(s);
    }

    @Test
    void catches_exception_silently(){
        ASTNode parser = MTLParser.tryParse("((A)");
        assertNull(parser);
    }

    @Test
    void throws_exception_on_empty_input() {
        assertThrowsException("");
    }

    @Test
    void throws_exception_on_empty_brackets() {
        assertThrowsException("()");

        for (int i = 0; i < 3; i++) {
            assertThrowsException("()".repeat(new Random().nextInt(Integer.MAX_VALUE/8)));
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
}
