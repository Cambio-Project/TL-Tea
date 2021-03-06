package cambio.tltea.core;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.BinaryOperationASTNode;
import cambio.tltea.parser.core.UnaryOperationASTNode;
import cambio.tltea.parser.core.ValueASTNode;
import cambio.tltea.parser.mtl.generated.MTLParser;
import cambio.tltea.parser.mtl.generated.ParseException;
import cambio.tltea.parser.testhelper.ASTTreeComparison;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ASTNodeTest {

    @Test
    void getSize() {
        ASTNode leaf1 = new ValueASTNode("1");
        ASTNode leaf2 = new ValueASTNode("2");
        ASTNode leaf3 = new ValueASTNode("3");

        ASTNode leftUnary = new UnaryOperationASTNode("minus", leaf1);
        ASTNode rightBinary = new BinaryOperationASTNode("plus", leaf2, leaf3);

        ASTNode root = new BinaryOperationASTNode("op", leftUnary, rightBinary);

        assertEquals(6, root.getSize());
    }


    @Test
    void cloneTest() {
        ASTNode leaf1 = new ValueASTNode("1");
        ASTNode leaf2 = new ValueASTNode("2");
        ASTNode leaf3 = new ValueASTNode("3");

        ASTNode leftUnary = new UnaryOperationASTNode("minus", leaf1);
        ASTNode rightBinary = new BinaryOperationASTNode("plus", leaf2, leaf3);

        ASTNode root = new BinaryOperationASTNode("op", leftUnary, rightBinary);

        ASTNode clone = root.clone();

        assertEquals(root.getSize(), clone.getSize());
        assertNotSame(root, clone);
        ASTTreeComparison.compareAST(root, clone);
    }


    @Test
    void cloneTest2() throws ParseException {
        MTLParser parser = new MTLParser("G[0,1]((A)&(B)|F[A](C)->!(F))");
        ASTNode result = parser.parse();
        ASTNode clone = result.clone();
        ASTTreeComparison.compareAST(result, clone);
    }
}