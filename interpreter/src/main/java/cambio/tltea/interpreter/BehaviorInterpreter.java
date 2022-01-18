package cambio.tltea.interpreter;

import cambio.tltea.interpreter.nodes.requirements.*;
import cambio.tltea.interpreter.utils.ASTManipulator;
import cambio.tltea.parser.core.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Lion Wagner
 */
class BehaviorInterpreter {

    public final BehaviorInterpretationResult result = new BehaviorInterpretationResult(null,
                                                                                        null,
                                                                                        new LinkedList<>(),
                                                                                        new TriggerNotifier());

    public BehaviorInterpretationResult interpret(ASTNode ast) {
        var workCopy = ast.clone();
        result.setModifiedAST(workCopy);
        var interpretedAST = interpretAsRequirement(workCopy);
        result.setInterpretedAST(interpretedAST);
        return result;
    }


    private InteractionNode<?> interpretAsRequirement(ASTNode root) {
        if (root instanceof ValueASTNode valueNode) {
            return interpretAsRequirement(valueNode);
        } else if (root instanceof UnaryOperationASTNode unNode) {
            return interpretAsRequirement(unNode);
        } else if (root instanceof BinaryOperationASTNode biNode) {
            return interpretAsRequirement(biNode);
        }
        return null;
    }

    private InteractionNode<Boolean> interpretAsRequirement(UnaryOperationASTNode unNode) {
        switch (unNode.getOperator()) {
            case NOT -> {
                if (unNode.getChild() instanceof UnaryOperationASTNode child
                    && child.getOperator() == OperatorToken.NOT) {
                    interpretAsRequirement(ASTManipulator.removeDoubleNot(unNode));
                }//TODO: replace !true / !false with false / true
                var child = interpretAsRequirement(unNode.getChild());
                return new NotInteractionNode((InteractionNode<Boolean>) child);
            }
            default -> throw new UnsupportedOperationException("Operator not yet supported: " + unNode.getOperator());
        }
    }

    private InteractionNode<Boolean> interpretAsRequirement(BinaryOperationASTNode binaryNode) {
        switch (binaryNode.getOperator()) {
            case IFF: {
                interpretAsRequirement(ASTManipulator.splitIFF(binaryNode));
            }
            case IMPLIES: {
                var left = (InteractionNode<Boolean>) interpretAsRequirement(binaryNode.getLeftChild());
                var right = interpretAsBehavior(binaryNode.getRightChild());
                return new ImplicationNode(left, right, result.getTriggerNotifier());
            }
            case AND: {
                var children = flattenRequirement(binaryNode);
                return new AndInteractionNode(children.toArray(new InteractionNode[0]));
            }
            case OR: {
                var children = flattenRequirement(binaryNode);
                return new OrInteractionNode(children.toArray(new InteractionNode[0]));
            }
            default: {
                throw new UnsupportedOperationException("Operator not yet supported: " + binaryNode.getOperator());
            }
        }
    }

    private InteractionNode<?> interpretAsRequirement(ValueASTNode valueNode) {
        try {
            double d = Double.parseDouble(valueNode.getValue());
            return new FixedValueDescription<>(d);
        } catch (NumberFormatException e) {

        }

        if (valueNode.getValue().startsWith("(") && valueNode.getValue().endsWith(")")) {
            ActivatableEvent activatableEvent = new ActivatableEvent(valueNode.getValue());
            result.addListener(activatableEvent.getEventListener());
            return activatableEvent;
        } else if (valueNode.getValue().startsWith("[") && valueNode.getValue().endsWith("]")) {
            //TODO: return value watcher
        }
        return new FixedValueDescription<>(valueNode.getValue());

    }


    @SuppressWarnings("unchecked") //should be safe casting
    private List<InteractionNode<Boolean>> flattenRequirement(BinaryOperationASTNode root) {
        List<InteractionNode<Boolean>> children = new ArrayList<>();

        if (root.getLeftChild() instanceof BinaryOperationASTNode leftChild && leftChild.getOperator() == root.getOperator()) {
            children.addAll(flattenRequirement(leftChild));
        } else {
            children.add((InteractionNode<Boolean>) interpretAsRequirement(root.getLeftChild()));
        }

        if (root.getRightChild() instanceof BinaryOperationASTNode rightChild && rightChild.getOperator() == root.getOperator()) {
            children.addAll(flattenRequirement(rightChild));
        } else {
            children.add((InteractionNode<Boolean>) interpretAsRequirement(root.getRightChild()));
        }

        return children;
    }


    private String interpretAsBehavior(ASTNode node) {
        if (node instanceof ValueASTNode) {
            return ((ValueASTNode) node).getValue();
        } else {
            throw new IllegalArgumentException("Expected Value Node");
        }
    }
}
