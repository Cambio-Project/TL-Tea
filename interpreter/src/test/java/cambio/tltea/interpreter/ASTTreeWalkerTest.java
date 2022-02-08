package cambio.tltea.interpreter;

import cambio.tltea.interpreter.utils.ASTTreeWalker;
import cambio.tltea.parser.core.*;
import cambio.tltea.parser.ltl.generated.LTLParser;
import cambio.tltea.parser.ltl.generated.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ASTTreeWalkerTest {

    @Test
    void outputs_correct_preorder() throws ParseException {
        ASTNode tree = LTLParser.parse("☐((P) → ☐ (S))");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode unaryNode = new UnaryOperationASTNode("☐", value2);
        ASTNode binaryNode = new BinaryOperationASTNode("→", value1, unaryNode);
        ASTNode unaryNode2 = new UnaryOperationASTNode("☐", binaryNode);

        List<ASTNode> expected = new LinkedList<>();
        expected.add(unaryNode2);
        expected.add(binaryNode);
        expected.add(value1);
        expected.add(unaryNode);
        expected.add(value2);

        List<ASTNode> nodes = toList(tree, ASTTreeWalker::walkPreOrder);
        compareNodeListShallow(expected, nodes);
    }

    @Test
    void outputs_correct_inorder() throws ParseException {
        ASTNode tree =  LTLParser.parse("☐((P) → ☐ (S))");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode unaryNode = new UnaryOperationASTNode("☐", value2);
        ASTNode binaryNode = new BinaryOperationASTNode("→", value1, unaryNode);
        ASTNode unaryNode2 = new UnaryOperationASTNode("☐", binaryNode);

        List<ASTNode> expected = new LinkedList<>();
        expected.add(unaryNode2);
        expected.add(value1);
        expected.add(binaryNode);
        expected.add(unaryNode);
        expected.add(value2);

        List<ASTNode> nodes = toList(tree, ASTTreeWalker::walkInOrder);
        compareNodeListShallow(expected, nodes);
    }

    @Test
    void outputs_correct_postorder() throws ParseException {
        ASTNode tree =  LTLParser.parse("☐((P) → ☐ (S))");

        ASTNode PValue = new ValueASTNode("(P)");
        ASTNode SValue = new ValueASTNode("(S)");
        ASTNode innerGlobally = new UnaryOperationASTNode("☐", SValue);
        ASTNode implies = new BinaryOperationASTNode("→", PValue, innerGlobally);
        ASTNode outerGlobally = new UnaryOperationASTNode("☐", implies);

        List<ASTNode> expected = new LinkedList<>();
        expected.add(outerGlobally);
        expected.add(PValue);
        expected.add(innerGlobally);
        expected.add(SValue);
        expected.add(implies);

        List<ASTNode> nodes = toList(tree, ASTTreeWalker::walkPostOrder);
        compareNodeListShallow(expected, nodes);
    }

    private static List<ASTNode> toList(ASTNode tree, BiConsumer<ASTNode, Consumer<ASTNode>> walker) {
        List<ASTNode> list = new LinkedList<>();
        walker.accept(tree, list::add);
        return list;
    }

    private static void compareNodeListShallow(List<ASTNode> expected, List<ASTNode> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            ASTNode expectedNode = expected.get(i);
            ASTNode actualNode = actual.get(i);
            if (expectedNode instanceof ValueASTNode v_node1 && actualNode instanceof ValueASTNode v_node2) {
                Assertions.assertEquals(v_node1.getValue(),
                                        v_node2.getValue(),
                                        String.format("Found difference at position %s", i));
            } else if (expectedNode instanceof OperatorASTNode u_node1 && actualNode instanceof OperatorASTNode u_node2) {
                Assertions.assertEquals(u_node1.getOperator(),
                                        u_node2.getOperator(),
                                        String.format("Found difference at position %s", i));
            }
        }
    }


}