package cambio.tltea.interpreter;

import cambio.tltea.interpreter.nodes.ImplicationNode;
import cambio.tltea.interpreter.nodes.TriggerNotifier;
import cambio.tltea.interpreter.nodes.cause.*;
import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode;
import cambio.tltea.interpreter.nodes.consequence.EventActivationDescription;
import cambio.tltea.interpreter.utils.ASTManipulator;
import cambio.tltea.parser.core.*;
import cambio.tltea.parser.core.temporal.ITemporalExpressionValueHolder;
import cambio.tltea.parser.core.temporal.ITemporalValue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Expects a future MTL formula.
 * <p>
 * Operators G[...] - Globally/Always F[...] - Finally/Eventually N[...] - Next
 * <p>
 * Binary R[...] - Release U[...] - Until
 * <p>
 * Not Allowed:
 * <p>
 * S[...] - Since H[...] - History (always in the past) P[...] - Past (Once in the Past) Back to
 * <p>
 * <p>
 * <p>
 * Simplifications:
 * <p>
 * F[...] α = TRUE U[...] β R[...] α = NOT β U[...] NOT α N[TimeInstance] α = FALSE U[...] α
 * <p>
 * <p>
 * NOT G NOT α = F α NOT F NOT α = G α
 *
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

        workCopy = ASTManipulator.insertImplicitGloballyRoot(workCopy);

        var interpretedAST = interpretAsCause(workCopy);
        result.setInterpretedAST(interpretedAST);
        return result;
    }


    private CauseNode<?> interpretAsCause(ASTNode root) {
        if (root instanceof ValueASTNode valueNode) {
            return interpretAsCause(valueNode);
        } else if (root instanceof UnaryOperationASTNode unNode) {
            return interpretAsCause(unNode);
        } else if (root instanceof BinaryOperationASTNode biNode) {
            return interpretAsCause(biNode);
        }
        return null;
    }

    private CauseNode<Boolean> interpretAsCause(UnaryOperationASTNode unNode) {
        switch (unNode.getOperator()) {
            case NOT -> {
                if (unNode.getChild() instanceof UnaryOperationASTNode child
                    && child.getOperator() == OperatorToken.NOT) {
                    interpretAsCause(ASTManipulator.removeDoubleNot(unNode));
                }//TODO: replace !true / !false with false / true
                var child = interpretAsCause(unNode.getChild());
                return new NotCauseNode((CauseNode<Boolean>) child);
            }
            default -> throw new UnsupportedOperationException(
                    "Operator not supported for cause description (left side of implication): " + unNode.getOperator());
        }
    }

    private CauseNode<Boolean> interpretAsCause(BinaryOperationASTNode binaryNode) {
        switch (binaryNode.getOperator()) {
            case IFF: {
                interpretAsCause(ASTManipulator.splitIFF(binaryNode));
            }
            case IMPLIES: {
                var left = (CauseNode<Boolean>) interpretAsCause(binaryNode.getLeftChild());
                var right = interpretAsBehavior(binaryNode.getRightChild());
                return new ImplicationNode(left, right, result.getTriggerNotifier());
            }
            case AND: {
                var children = flattenCause(binaryNode);
                return new AndCauseNode(children.toArray(new CauseNode[0]));
            }
            case OR: {
                var children = flattenCause(binaryNode);
                return new OrCauseNode(children.toArray(new CauseNode[0]));
            }
            default: {
                throw new UnsupportedOperationException("Operator not yet supported: " + binaryNode.getOperator());
            }
        }
    }

    private CauseNode<?> interpretAsCause(ValueASTNode valueNode) {
        try {
            double d = Double.parseDouble(valueNode.getValue());
            return new ConstantValueProvider<>(d);
        } catch (NumberFormatException e) {
        }

        if (valueNode.getValue().startsWith("(") && valueNode.getValue().endsWith(")")) {
            EventActivationNode eventActivationNode = new EventActivationNode(valueNode.getValue());
            result.addListener(eventActivationNode.getEventListener());
            return eventActivationNode;
        } else if (valueNode.getValue().startsWith("[") && valueNode.getValue().endsWith("]")) {
            //TODO: return value watcher
        }
        return new ConstantValueProvider<>(valueNode.getValue());

    }


    @SuppressWarnings("unchecked") //should be safe casting
    private List<CauseNode<Boolean>> flattenCause(BinaryOperationASTNode root) {
        List<CauseNode<Boolean>> children = new ArrayList<>();

        if (root.getLeftChild() instanceof BinaryOperationASTNode leftChild && leftChild.getOperator() == root.getOperator()) {
            children.addAll(flattenCause(leftChild));
        } else {
            children.add((CauseNode<Boolean>) interpretAsCause(root.getLeftChild()));
        }

        if (root.getRightChild() instanceof BinaryOperationASTNode rightChild && rightChild.getOperator() == root.getOperator()) {
            children.addAll(flattenCause(rightChild));
        } else {
            children.add((CauseNode<Boolean>) interpretAsCause(root.getRightChild()));
        }

        //TODO: map EventActivationListeners to ComparisonNodes (==TRUE)

        return children;
    }


    private String interpretAsBehavior(ASTNode node) {
        if (node instanceof ValueASTNode) {
            return ((ValueASTNode) node).getValue();
        } else {
            throw new IllegalArgumentException("Expected Value Node");
        }
    }

    private ConsequenceNode interpretAsConsequence(ASTNode node) {
        return interpretAsConsequence(node, null);
    }

    private ConsequenceNode interpretAsConsequence(ASTNode node, ITemporalValue tmpvalue) {
        if (node instanceof BinaryOperationASTNode binode) {
            return interpretAsConsequence(binode, tmpvalue);
        } else if (node instanceof UnaryOperationASTNode unode) {
            return interpretAsConsequence(unode, tmpvalue);
        } else if (node instanceof ValueASTNode vnode) {
            return interpretAsConsequence(vnode, tmpvalue);
        }
        return null;
    }

    private ConsequenceNode interpretAsConsequence(ValueASTNode node, ITemporalValue tmpvalue) {
        return new EventActivationDescription(node.getValue(), tmpvalue);
    }

    private ConsequenceNode interpretAsConsequence(UnaryOperationASTNode node, ITemporalValue tmpvalue) {
        if (node instanceof ITemporalExpressionValueHolder valueHolder) {
            tmpvalue = valueHolder.getTemporalValue();
        } else {
            throw new IllegalArgumentException("Unsupported Operation");
        }
        return interpretAsConsequence(node.getChild(), tmpvalue);
    }

    private ConsequenceNode interpretAsConsequence(BinaryOperationASTNode node, ITemporalValue tmpvalue) {

        return null;
    }

}

