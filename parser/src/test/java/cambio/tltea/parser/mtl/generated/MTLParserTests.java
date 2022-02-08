package cambio.tltea.parser.mtl.generated;

import cambio.tltea.parser.core.*;
import cambio.tltea.parser.core.temporal.TemporalBinaryOperationASTNode;
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo;
import cambio.tltea.parser.core.temporal.TemporalUnaryOperationASTNode;
import cambio.tltea.parser.testhelper.ASTTreeComparison;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MTLParserTests {

    @Test
    void Value() throws ParseException {
        MTLParser parser = new MTLParser("(proposition)");
        ASTNode node = parser.parse();

        ASTTreeComparison.compareAST(new ValueASTNode("(proposition)"), node);
    }

    @Test
    void Comparison() throws ParseException {
        ASTNode value1 = new ValueASTNode("1");
        ASTNode value2 = new ValueASTNode("2");
        ASTNode expected = new BinaryOperationASTNode(">", value1, value2);

        ASTNode node = MTLParser.parse("1>2");
        ASTTreeComparison.compareAST(expected, node);
    }

    @Test
    void unaryOperation() throws ParseException {
        ASTNode value = new ValueASTNode("(do_stuff)");
        UnaryOperationASTNode expectedNested = new TemporalUnaryOperationASTNode(new TemporalOperatorInfo("F","[10]"), value);
        UnaryOperationASTNode expected = new UnaryOperationASTNode("!", expectedNested);

        ASTNode node = MTLParser.parse("!F[10](do_stuff)");
        ASTTreeComparison.compareAST(expected, node);
    }

    @Test
    void binaryMTLOperator() throws ParseException {
        ASTNode result = MTLParser.parse("(A)&(B)&(C)|(D)&(F)");

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
    void MTL_Formula_returns_correct_type() throws ParseException {
        MTLParser parser = new MTLParser("(A)");
        ASTNode result = parser.parse();
        assertTrue(result instanceof ValueASTNode);

        parser = new MTLParser("!(A)");
        result = parser.parse();
        assertTrue(result instanceof UnaryOperationASTNode);

        parser = new MTLParser("(A)&(B)");
        result = parser.parse();
        assertTrue(result instanceof BinaryOperationASTNode);


        parser = new MTLParser("G[10](A)");
        result = parser.parse();
        assertTrue(result instanceof TemporalUnaryOperationASTNode);


        parser = new MTLParser("(A)S[10](B)");
        result = parser.parse();
        assertTrue(result instanceof TemporalBinaryOperationASTNode);

    }

    @Test
    void unmatchedBracketsTest() throws ParseException {
        MTLParser parser = new MTLParser("(((A))");
        assertThrows(ParseException.class, parser::parse);
    }

    @Test
    void allows_basic_bracketing() throws ParseException {
        MTLParser parser = new MTLParser("((((A))))");
        ASTNode result = parser.parse();
        ASTNode expected = new ValueASTNode("(A)");

        ASTTreeComparison.compareAST(expected, result);
    }

    @Test
    void allows_basic_bracketing2() throws ParseException {
        MTLParser parser = new MTLParser("(((!(A))))");
        ASTNode result = parser.parse();
        ASTNode expected = new UnaryOperationASTNode("!", new ValueASTNode("(A)"));
        ASTTreeComparison.compareAST(expected, result);
    }

    @Test
    void allows_basic_bracketing3() throws ParseException {
        MTLParser parser = new MTLParser("!((A))");
        ASTNode result = parser.parse();
        ASTNode expected = new UnaryOperationASTNode("!", new ValueASTNode("(A)"));
        ASTTreeComparison.compareAST(expected, result);
    }


    @Test
    void allows_bracketing_inside_temporal_values() throws ParseException {
        MTLParser parser = new MTLParser("G[<(10+5)]((A))");
        ASTNode result = parser.parse();

        ASTNode expected = new TemporalUnaryOperationASTNode(new TemporalOperatorInfo("G","[<(10+5)]"), new ValueASTNode("(A)"));
        ASTTreeComparison.compareAST(expected, result);
    }
}