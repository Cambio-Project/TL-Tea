package cambio.tltea.interpreter.utils;

import cambio.tltea.parser.core.*;
import cambio.tltea.parser.core.temporal.TemporalUnaryOperationASTNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
     * Removes double negation from an AST. Accepts both parent and child negation nodes as input. E.g. morphs (a) &&
     * !!(b) into (a) && (b), if given the first or second NOT node as input.
     * <p>
     * It is recommended to always use the parent as input to ensure a correct interpretation.
     *
     * @param doubleNotParent Negation node that should be removed.
     * @return The node that was double negated.
     */
    public static @NotNull OperatorASTNode removeDoubleNot(@NotNull UnaryOperationASTNode doubleNotParent) {
        if (doubleNotParent.getOperator() != OperatorToken.NOT) {
            throw new IllegalArgumentException("Expected a NOT node.");
        }
        if (doubleNotParent.getChild() instanceof UnaryOperationASTNode doubleNotChild && doubleNotChild.getOperator() == OperatorToken.NOT) {
            redirectParent(doubleNotParent.getParent(),
                           doubleNotParent,
                           ((UnaryOperationASTNode) doubleNotParent.getChild()).getChild());
            return doubleNotParent.getParent();
        }
        return doubleNotParent;
    }

    public static TemporalUnaryOperationASTNode insertImplicitGloballyRoot(ASTNode root) {
        if (root instanceof TemporalUnaryOperationASTNode temporalNode) {
            return temporalNode;
        }

        var newNode = new TemporalUnaryOperationASTNode(OperatorToken.GLOBALLY, root);
        insertBefore(root, newNode);
        return newNode;
    }

    public static TemporalUnaryOperationASTNode insertImplicitFinallyRoot(ASTNode root) {
        if (root instanceof TemporalUnaryOperationASTNode temporalNode) {
            return temporalNode;
        }

        var newNode = new TemporalUnaryOperationASTNode(OperatorToken.FINALLY, root);
        insertBefore(root, newNode);
        return newNode;
    }


    public static ASTNode applyNot(@NotNull UnaryOperationASTNode notNode) {
        if (notNode.getOperator() != OperatorToken.NOT) {
            throw new IllegalArgumentException("Expected a NOT node.");
        }

        if (notNode.getChild() instanceof BinaryOperationASTNode binaryNode) {
            if (OperatorToken.ComparisonOperatorTokens.contains(binaryNode.getOperator())) {
                return applyNotToComparison(binaryNode);
            }

            return switch (binaryNode.getOperator()) {
                case AND -> applyNotToAnd(binaryNode);
                case OR -> applyNotToOr(binaryNode);
                default -> throw new IllegalArgumentException("Operator not supported.");
            };
        } else if (notNode.getChild() instanceof UnaryOperationASTNode unaryChild &&
                   unaryChild.getOperator() == OperatorToken.NOT) {
            return removeDoubleNot(notNode);
        }
        throw new IllegalArgumentException("Cannot apply NOT (yet?) to " + notNode.getChild()
                                                                                  .getClass()
                                                                                  .getSimpleName() + " node.");
    }

    @Contract("_ -> new")
    private static @NotNull OperatorASTNode applyNotToOr(@NotNull BinaryOperationASTNode binaryNode) {
        return new BinaryOperationASTNode(OperatorToken.AND,
                                          new UnaryOperationASTNode(OperatorToken.NOT, binaryNode.getLeftChild()),
                                          new UnaryOperationASTNode(OperatorToken.NOT, binaryNode.getRightChild()));
    }

    @Contract("_ -> new")
    private static @NotNull OperatorASTNode applyNotToAnd(@NotNull BinaryOperationASTNode binaryNode) {
        return new BinaryOperationASTNode(OperatorToken.OR,
                                          new UnaryOperationASTNode(OperatorToken.NOT, binaryNode.getLeftChild()),
                                          new UnaryOperationASTNode(OperatorToken.NOT, binaryNode.getRightChild()));
    }


    /**
     * Inserts the insertedNode before the currentNode.
     *
     * @param currentNode
     * @param insertedNode
     */
    private static void insertBefore(ASTNode currentNode, UnaryOperationASTNode insertedNode) {
        redirectParent(currentNode.getParent(), currentNode, insertedNode);
    }

    /**
     * Replaces the given oldChild with the newChild. Does handle {@link UnaryOperationASTNode}s and {@link
     * BinaryOperationASTNode}s.
     *
     * <b>This does not replace the relation that the oldChild potentially has to its children!</b>
     *
     * @param parent   The parent of the oldChild.
     * @param oldChild The child that should be replaced.
     * @param newChild The new child that should replace the oldChild.
     */
    public static void redirectParent(@NotNull OperatorASTNode parent,
                                      @NotNull ASTNode oldChild,
                                      @NotNull ASTNode newChild) {
        if (parent instanceof UnaryOperationASTNode unParent) {
            if (unParent.getChild() == oldChild) {
                unParent.setChild(newChild);
            } else {
                throw new IllegalStateException("Expected oldChild to be a child of parent.");
            }
        } else if (parent instanceof BinaryOperationASTNode binParent) {
            if (binParent.getLeftChild() == oldChild) {
                binParent.setLeftChild(newChild);
            } else if (binParent.getRightChild() == oldChild) {
                binParent.setRightChild(newChild);
            } else {
                throw new IllegalStateException("Expected oldChild to be a child of parent.");
            }
        } else {
            throw new IllegalStateException("Unexpected parent type: " + parent.getClass().getSimpleName());
        }
    }

    private static BinaryOperationASTNode applyNotToComparison(@NotNull BinaryOperationASTNode comparisonNode) {
        OperatorToken reversedOperator = comparisonNode.getOperator().invert();
        UnaryOperationASTNode notParent = (UnaryOperationASTNode) comparisonNode.getParent();
        BinaryOperationASTNode replacer = new BinaryOperationASTNode(reversedOperator,
                                                                     comparisonNode.getLeftChild(),
                                                                     comparisonNode.getRightChild());
        redirectParent(notParent.getParent(), notParent, replacer);
        return replacer;
    }

    public static @NotNull Collection<ASTNode> flatten(@NotNull BinaryOperationASTNode node) {
        OperatorToken op = node.getOperator();
        if (op != OperatorToken.AND && op != OperatorToken.OR) {
            throw new IllegalArgumentException("Expected AND or OR operator, but got " + op);
        }

        Collection<ASTNode> result = new HashSet<>();

        ASTNode leftChild = node.getLeftChild();
        ASTNode rightChild = node.getRightChild();

        result.add(leftChild);
        result.add(rightChild);
        if (leftChild instanceof BinaryOperationASTNode binLeft && binLeft.getOperator() == op) {
            result.addAll(flatten(binLeft));
        }
        if (rightChild instanceof BinaryOperationASTNode binRight && binRight.getOperator() == op) {
            result.addAll(flatten(binRight));
        }

        return result;
    }
}
