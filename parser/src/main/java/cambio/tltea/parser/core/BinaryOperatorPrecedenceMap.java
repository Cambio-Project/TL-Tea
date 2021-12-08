package cambio.tltea.parser.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum BinaryOperatorPrecedenceMap {
    INSTANCE;

    static {
        HashMap<String, Integer> prepMap = new HashMap<>();

        prepMap.put("<LT>", 0);
        prepMap.put("<LEQ>", 0);
        prepMap.put("<EQ>", 0);
        prepMap.put("<NEQ>", 0);
        prepMap.put("<GT>", 0);
        prepMap.put("<GEQ>", 0);

        prepMap.put("<OR>", 1);
        prepMap.put("<AND>", 2);

        prepMap.put("<IMPLIES>", 3);

        prepMap.put("<IFF>", 4);

        prepMap.put("<UNTIL_T>", 5);
        prepMap.put("<RELEASE_T>", 5);
        prepMap.put("<WEAKUNTIL_T>", 5);

        PRECEDENCE_MAP = Collections.unmodifiableMap(prepMap);
    }

    private final static Map<String, Integer> PRECEDENCE_MAP;

    public int getPrecedence(String operator) {

        Integer precedence;

        if (!hasPrecedence(operator)) {
            precedence = Integer.MIN_VALUE;
            System.out.printf("[TL-Tea] Operator '%s' does not have an assigned precedence, defaulting to weakest.%n",
                              operator);
        } else {
            precedence = PRECEDENCE_MAP.get(TokenImageMap.INSTANCE.getTokenImage(operator));
        }


        return precedence;
    }


    /**
     * Checks if the operator has higher precedence than the other operator.
     */
    public boolean hasHigherPrecedence(String operator, String otherOperator) {
        return getPrecedence(operator) > getPrecedence(otherOperator);
    }

    /**
     * Check if an operator has a precedence rating.
     */
    public boolean hasPrecedence(String operator) {
        return PRECEDENCE_MAP.containsKey(operator);
    }
}
