package cambio.tltea.interpreter.utils;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.BinaryOperationASTNode;
import cambio.tltea.parser.core.UnaryOperationASTNode;
import cambio.tltea.parser.core.ValueASTNode;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Lion Wagner
 */
public final class ASTTreeWalker {

    public static void walkInOrder(ASTNode rootNode, Consumer<ASTNode> listener) {
        walk(rootNode, listener, ASTTreeWalker::walkInOrder, (BinaryOperationASTNode node) -> {
            walkInOrder(node.getLeftChild(), listener);
            listener.accept(node);
            walkInOrder(node.getRightChild(), listener);
        });
    }

    private static void walk(ASTNode rootNode,
                             Consumer<ASTNode> listener,
                             BiConsumer<ASTNode, Consumer<ASTNode>> walker,
                             Consumer<BinaryOperationASTNode> binaryDecision) {
        if (rootNode == null) {
            throw new IllegalArgumentException("rootNode cannot be null");
        }
        if (listener == null) {
            return;
        }

        if (rootNode instanceof ValueASTNode) {
            listener.accept(rootNode);
        } else if (rootNode instanceof UnaryOperationASTNode unary) {
            listener.accept(rootNode);
            walker.accept(unary.getChild(), listener);
        } else if (rootNode instanceof BinaryOperationASTNode binary) {
            binaryDecision.accept(binary);
        } else {
            throw new RuntimeException("Unknown ASTNode type");
        }
    }


    public static void walkPreOrder(ASTNode rootNode, Consumer<ASTNode> listener) {
        walk(rootNode, listener, ASTTreeWalker::walkPreOrder, (BinaryOperationASTNode node) -> {
            listener.accept(node);
            walkInOrder(node.getLeftChild(), listener);
            walkInOrder(node.getRightChild(), listener);
        });
    }


    public static void walkPostOrder(ASTNode rootNode, Consumer<ASTNode> listener) {
        walk(rootNode, listener, ASTTreeWalker::walkPostOrder, (BinaryOperationASTNode node) -> {
            walkInOrder(node.getLeftChild(), listener);
            walkInOrder(node.getRightChild(), listener);
            listener.accept(node);
        });
    }
}
