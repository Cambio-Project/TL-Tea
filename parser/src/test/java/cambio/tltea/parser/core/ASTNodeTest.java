package cambio.tltea.core;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.BinaryOperationASTNode;
import cambio.tltea.parser.core.UnaryOperationASTNode;
import cambio.tltea.parser.core.ValueASTNode;
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
}