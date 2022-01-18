package cambio.tltea.interpreter.utils;

import cambio.tltea.parser.core.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public final class ASTManipulator {

    /**
     * Splits and IFF node into two IMPLIES nodes and replaces it in its AST. (A) <-> (B) into (A) -> (B) and (B) ->
     * (A)
     *
     * @param iffNode IFF node to be split.
     * @return The and node that holds both IMPLIES nodes.
     */
    public static @NotNull BinaryOperationASTNode splitIFF(@NotNull BinaryOperationASTNode iffNode) {
        if (iffNode.getOperator() != OperatorToken.IFF) {
            throw new IllegalArgumentException("Expected an IFF node.");
        }

        ASTNode leftChild = iffNode.getLeftChild();
        ASTNode rightChild = iffNode.getRightChild();

        BinaryOperationASTNode newLeft = new BinaryOperationASTNode(OperatorToken.IMPLIES,
                                                                    leftChild,
                                                                    rightChild);
        BinaryOperationASTNode newRight = new BinaryOperationASTNode(OperatorToken.IMPLIES,
                                                                     rightChild,
                                                                     leftChild);
        BinaryOperationASTNode and = new BinaryOperationASTNode(OperatorToken.AND, newLeft, newRight);
        and.setBracketed(true); //create brackets to ensure correct order of evaluation

        OperatorASTNode parent = iffNode.getParent();
        if (parent instanceof UnaryOperationASTNode unaryParent) {
            unaryParent.setChild(and);
        } else if (parent instanceof BinaryOperationASTNode binaryParent) {
            if (binaryParent.getLeftChild() == iffNode) {
                binaryParent.setLeftChild(and);
            } else {
                binaryParent.setRightChild(and);
            }
        }


        return and;
    }


    /**
     * Removes double negation from an AST.
     * Accepts both parent and child negation nodes as input.
     * E.g. morphs (a) && !!(b) into (a) && (b), if given the first or second NOT node as input.
     *
     * It is recommended to always use the parent as input to ensure a correct interpretation.
     *
     * @param unNode Negation node that should be removed.
     * @return The node that was double negated.
     */
    public static @NotNull ASTNode removeDoubleNot(@NotNull UnaryOperationASTNode unNode) {
        if (unNode.getOperator() != OperatorToken.NOT) {
            throw new IllegalArgumentException("Expected a NOT node.");
        }
        ASTNode targetChild = null;
        ASTNode targetParent = null;
        if (unNode.getParent() instanceof UnaryOperationASTNode unParent &&
            unParent.getOperator() == OperatorToken.NOT) {
            targetChild = unNode.getChild();
            targetParent = unParent.getParent();
        } else if (unNode.getChild() instanceof UnaryOperationASTNode unChild &&
                   unChild.getOperator() == OperatorToken.NOT) {
            targetChild = unChild.getChild();
            targetParent = unNode.getParent();
        }

        if (targetChild == null) {
            throw new IllegalStateException("Expected a double NOT node. Or remaining child was empty.");
        }

        if (targetParent instanceof BinaryOperationASTNode binParent) {
            if (binParent.getLeftChild() == unNode) {
                binParent.setLeftChild(targetChild);
            } else {
                binParent.setRightChild(targetChild);
            }
        } else if (targetParent instanceof UnaryOperationASTNode unParent) {
            unParent.setChild(targetChild);
        }

        return targetChild;
    }
}
