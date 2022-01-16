package cambio.tltea.parser.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum BinaryOperatorPrecedenceMap {
    INSTANCE;

    static {
        HashMap<OperatorToken, Integer> prepMap = new HashMap<>();

        prepMap.put(OperatorToken.LT, 0);
        prepMap.put(OperatorToken.LEQ, 0);
        prepMap.put(OperatorToken.EQ, 0);
        prepMap.put(OperatorToken.NEQ, 0);
        prepMap.put(OperatorToken.GT, 0);
        prepMap.put(OperatorToken.GEQ, 0);

        prepMap.put(OperatorToken.OR, 1);
        prepMap.put(OperatorToken.AND, 2);

        prepMap.put(OperatorToken.IMPLIES, 3);

        prepMap.put(OperatorToken.IFF, 4);

        prepMap.put(OperatorToken.UNTIL, 5);
        prepMap.put(OperatorToken.RELEASE, 5);
        prepMap.put(OperatorToken.WEAKUNTIL, 5);

        //noinspection Java9CollectionFactory
        PRECEDENCE_MAP = Collections.unmodifiableMap(prepMap);
    }

    private final static Map<OperatorToken, Integer> PRECEDENCE_MAP;

    public int getPrecedence(OperatorToken operator) {

        Integer precedence;

        if (!hasPrecedence(operator)) {
            precedence = Integer.MIN_VALUE;
            System.out.printf("[TL-Tea] Operator '%s' does not have an assigned precedence, defaulting to weakest.%n",
                              operator);
        } else {
            precedence = PRECEDENCE_MAP.get(operator);
        }


        return precedence;
    }


    /**
     * Checks if the operator has higher precedence than the other operator.
     */
    public boolean hasHigherPrecedence(OperatorToken operator, OperatorToken otherOperator) {
        return getPrecedence(operator) > getPrecedence(otherOperator);
    }

    /**
     * Check if an operator has a precedence rating.
     */
    public boolean hasPrecedence(OperatorToken operator) {
        return PRECEDENCE_MAP.containsKey(operator);
    }
}
