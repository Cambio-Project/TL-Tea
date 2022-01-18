package cambio.tltea.interpreter.nodes.requirements;

import cambio.tltea.parser.core.OperatorToken;

/**
 * @author Lion Wagner
 */
public class ComparisonInteractionNode extends InteractionNode<Boolean> {
    private final OperatorToken operator;
    private final IValueDescription<?> left;
    private final IValueDescription<?> right;

    public ComparisonInteractionNode(OperatorToken operator,
                                     IValueDescription<?> left,
                                     IValueDescription<?> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }


    @Override
    public Boolean getValue() {
        var val1 = left.getValue();
        var val2 = right.getValue();

        if (val1 == null || val2 == null) {
            return false;
        }
        if (val1 instanceof String && val2 instanceof String) {
            return switch (operator) {
                case EQ -> val1.equals(val2);
                case NEQ -> !val1.equals(val2);
                default -> throw new IllegalArgumentException("Operator not supported for string comparison:" + operator);
            };
        } else if (val1 instanceof Number n1 && val2 instanceof Number n2) {
            return switch (operator) {
                case EQ -> val1.equals(val2);
                case NEQ -> !val1.equals(val2);
                case GT -> n1.doubleValue() > n2.doubleValue();
                case GEQ -> n1.doubleValue() >= n2.doubleValue();
                case LT -> n1.doubleValue() < n2.doubleValue();
                case LEQ -> n1.doubleValue() <= n2.doubleValue();
                default -> throw new IllegalArgumentException("Operator not supported as comparison: " + operator);
            };
        }
        else throw new IllegalArgumentException("Value type could not be compared: " + val1.getClass() + " and " + val2.getClass());
    }
}
