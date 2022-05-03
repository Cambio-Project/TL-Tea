package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.parser.core.OperatorToken;
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo;

/**
 * @author Lion Wagner
 */
public class ComparisonCauseNode extends CauseNode {
    private final OperatorToken operator;
    private final ValueProvider<?> left;
    private final ValueProvider<?> right;

    private boolean lastValue = false;

    public ComparisonCauseNode(OperatorToken operator,
                               TemporalOperatorInfo temporalContext,
                               ValueProvider<?> left,
                               ValueProvider<?> right) {
        if (!OperatorToken.ComparisonOperatorTokens.contains(operator)) {
            throw new IllegalArgumentException("Operator not supported as comparison: " + operator);
        }
        this.operator = operator;
        this.left = left;
        this.right = right;

        this.left.subscribe(event -> {
            var lastValue = this.lastValue;
            super.notifySubscribers(new StateChangeEvent<>(this, getCurrentValue(), lastValue, event.when()));
        });
        this.right.subscribe(event -> {
            var lastValue = this.lastValue;
            super.notifySubscribers(new StateChangeEvent<>(this, getCurrentValue(), lastValue, event.when()));
        });
    }


    //TODO: optimization we always take the same path through this method therefore we may be able to optimize
    @Override
    public Boolean getCurrentValue() {
        return lastValue = compareSides();
    }

    private boolean compareSides() {
        var val1 = left.currentValue;
        var val2 = right.currentValue;

        if (val1 == null || val2 == null) {
            return false;
        }
        if (val1 instanceof String && val2 instanceof String) {
            return switch (operator) {
                case EQ -> val1.equals(val2);
                case NEQ -> !val1.equals(val2);
                default -> throw new IllegalStateException("Operator not supported for string comparison:" + operator);
            };
        } else if (val1 instanceof Number n1 && val2 instanceof Number n2) {
            return switch (operator) {
                case EQ -> val1.equals(val2);
                case NEQ -> !val1.equals(val2);
                case GT -> n1.doubleValue() > n2.doubleValue();
                case GEQ -> n1.doubleValue() >= n2.doubleValue();
                case LT -> n1.doubleValue() < n2.doubleValue();
                case LEQ -> n1.doubleValue() <= n2.doubleValue();
                default -> throw new IllegalStateException("Operator not supported as comparison: " + operator);
            };
        } else if (val1 instanceof Comparable || val2 instanceof Comparable) {
            try {
                int compareValue = 0;
                if (val1 instanceof Comparable c1) {
                    compareValue = c1.compareTo(val2);
                } else //noinspection ConstantConditions
                    if (val2 instanceof Comparable c2) {
                        compareValue = -c2.compareTo(val1);//need to be reversed because we are comparing the other way around
                    }
                return switch (operator) {
                    case EQ -> compareValue == 0;
                    case NEQ -> compareValue != 0;
                    case GT -> compareValue > 0;
                    case GEQ -> compareValue >= 0;
                    case LT -> compareValue < 0;
                    case LEQ -> compareValue <= 0;
                    default -> throw new IllegalStateException("Operator not supported as comparison: " + operator);
                };
            } catch (ClassCastException ignored) {
            }
        }

        throw new IllegalStateException("Value type could not be compared: " + val1.getClass() + " and " + val2.getClass());
    }


    public OperatorToken getOperator() {
        return operator;
    }

}
