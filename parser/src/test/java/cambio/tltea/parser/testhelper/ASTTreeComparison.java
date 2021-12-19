package cambio.tltea.parser.testhelper;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.temporal.ITemporalExpressionValueHolder;
import org.junit.jupiter.api.Assertions;
import cambio.tltea.parser.core.*;

import java.util.Objects;

/**
 * @author Lion Wagner
 */
public final class ASTTreeComparison {

    public static void compareAST(ASTNode root1, ASTNode root2) {
        compareAST(root1, root2, 0);
    }

    private static void compareAST(ASTNode root1, ASTNode root2, int depth) {
        // if one of the roots is null and the other is not, throw an AssertionError with the null root name
        if (root1 == null && root2 != null) {
            Assertions.fail("root1 is null, root2 is not");
        }
        if (root2 == null && root1 != null) {
            Assertions.fail("root2 is null, root1 is not");
        }

        if (root1 == null && root2 == null) {
            return;
        }

        if (root1.getClass() != root2.getClass()) {
            throw new AssertionError("Different ASTNode classes detected at depth " + depth);
        }
        if (root1.getSize() != root2.getSize()) {
            throw new AssertionError(String.format("Different subtree sizes (%s vs %s) detected at depth %s",
                                                   root1.getSize(),
                                                   root2.getSize(), depth));
        }

        if (root1 instanceof ValueASTNode node && root2 instanceof ValueASTNode node2) {
            Assertions.assertEquals(node.getValue(), node2.getValue(), "Wrong value at depth " + depth);
        } else if (root1 instanceof OperatorASTNode op_node && root2 instanceof OperatorASTNode op_node2) {

            if (!Objects.equals(op_node.getOperator(), op_node2.getOperator())) {
                throw new AssertionError("Wrong operator at depth %d. Expected '%s' got '%s' ".formatted(depth,
                                                                                                         op_node.getOperator(),
                                                                                                         op_node2.getOperator()));
            }

            if (root1 instanceof ITemporalExpressionValueHolder holder1 && root2 instanceof ITemporalExpressionValueHolder holder2) {
                Assertions.assertEquals(holder1.getTemporalValue(),
                                        holder2.getTemporalValue(),
                                        "Wrong temporal value at depth %s. Expected '%s' got '%s' ".formatted(depth,
                                                                                                              holder1.getTemporalValue(),
                                                                                                              holder2.getTemporalValue()));
            }

            if (root1 instanceof UnaryOperationASTNode node && root2 instanceof UnaryOperationASTNode node2) {
                compareAST(node.getChild(), node2.getChild(), depth + 1);
            }

            if (root1 instanceof BinaryOperationASTNode node && root2 instanceof BinaryOperationASTNode node2) {
                compareAST(node.getLeftChild(), node2.getLeftChild(), depth + 1);
                compareAST(node.getRightChild(), node2.getRightChild(), depth + 1);
            }
        }
    }

}
