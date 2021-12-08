package cambio.tltea.parser.ltl.generated;

import cambio.tltea.parser.testhelper.ASTTreeComparison;
import org.junit.jupiter.api.Test;
import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.BinaryOperationASTNode;
import cambio.tltea.parser.core.UnaryOperationASTNode;
import cambio.tltea.parser.core.ValueASTNode;

import static org.junit.jupiter.api.Assertions.*;

class LTLParserTests {

    @Test
    void Value() throws ParseException {
        LTLParser parser = new LTLParser("(proposition)");
        ASTNode node = parser.ValueOrComparison();

        ASTTreeComparison.compareAST(new ValueASTNode("(proposition)"), node);
    }

    @Test
    void Comparison() throws ParseException {
        LTLParser parser = new LTLParser("1>2");
        ASTNode value1 = new ValueASTNode("1");
        ASTNode value2 = new ValueASTNode("2");
        ASTNode expected = new BinaryOperationASTNode(">", value1, value2);

        ASTNode node = parser.ValueOrComparison();
        ASTTreeComparison.compareAST(expected, node);
    }

    @Test
    void unaryOperation() throws ParseException {
        LTLParser parser = new LTLParser("!F(do_stuff)");
        UnaryOperationASTNode node = parser.UnaryOperation();

        ASTNode value = new ValueASTNode("(do_stuff)");
        UnaryOperationASTNode expectedNested = new UnaryOperationASTNode("F", value);
        UnaryOperationASTNode expected = new UnaryOperationASTNode("!", expectedNested);

        ASTTreeComparison.compareAST(expected, node);
    }

    @Test
    void binaryLTLOperator() throws ParseException {
        LTLParser parser = new LTLParser("(A)&(B)&(C)|(D)&(F)");
        ASTNode result = parser.LTL_Formula();

        new ValueASTNode("(A)");
        new ValueASTNode("(B)");
        new ValueASTNode("(C)");
        new ValueASTNode("(D)");
        new ValueASTNode("(F)");

        BinaryOperationASTNode node1 = new BinaryOperationASTNode("&",
                                                                  new ValueASTNode("(A)"),
                                                                  new ValueASTNode("(B)"));
        BinaryOperationASTNode node2 = new BinaryOperationASTNode("&", node1, new ValueASTNode("(C)"));
        BinaryOperationASTNode node3 = new BinaryOperationASTNode("&",
                                                                  new ValueASTNode("(D)"),
                                                                  new ValueASTNode("(F)"));
        BinaryOperationASTNode expected = new BinaryOperationASTNode("|", node2, node3);


        assertEquals(9, result.getSize());
        ASTTreeComparison.compareAST(expected, result);
    }

    @Test
    void LTL_Formula_returns_correct_type() throws ParseException {
        LTLParser parser = new LTLParser("(A)");
        ASTNode result = parser.LTL_Formula();
        assertTrue(result instanceof ValueASTNode);

        parser = new LTLParser("!(A)");
        result = parser.LTL_Formula();
        assertTrue(result instanceof UnaryOperationASTNode);

        parser = new LTLParser("(A)&(B)");
        result = parser.LTL_Formula();
        assertTrue(result instanceof BinaryOperationASTNode);

    }

    @Test
    void unmatchedBracketsTest() throws ParseException {
        LTLParser parser = new LTLParser("(((A))");
        assertThrows(ParseException.class, parser::LTL_Formula_File);
    }

    @Test
    void allows_basic_bracketing() throws ParseException {
        LTLParser parser = new LTLParser("((((A))))");
        ASTNode result = parser.LTL_Formula_File();
        ASTNode expected = new ValueASTNode("(A)");

        ASTTreeComparison.compareAST(expected, result);
    }

    @Test
    void allows_basic_bracketing2() throws ParseException {
        LTLParser parser = new LTLParser("(((!(A))))");
        ASTNode result = parser.LTL_Formula_File();
        ASTNode expected = new UnaryOperationASTNode("!", new ValueASTNode("(A)"));
        ASTTreeComparison.compareAST(expected, result);
    }

    @Test
    void allows_basic_bracketing3() throws ParseException {
        LTLParser parser = new LTLParser("!((A))");
        ASTNode result = parser.LTL_Formula_File();
        ASTNode expected = new UnaryOperationASTNode("!", new ValueASTNode("(A)"));
        ASTTreeComparison.compareAST(expected, result);
    }
}