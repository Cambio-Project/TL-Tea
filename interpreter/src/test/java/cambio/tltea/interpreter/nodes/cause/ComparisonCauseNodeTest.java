package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.parser.core.OperatorToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ComparisonCauseNodeTest {

    @Test
    void stringComparisonTest() {
        ConstantValueProvider<String> value = new ConstantValueProvider<>("a");
        ConstantValueProvider<String> clone = new ConstantValueProvider<>("a");
        ConstantValueProvider<String> other = new ConstantValueProvider<>("B");
        testEQNEQ(value, clone, other);

        ComparisonCauseNode failing = new ComparisonCauseNode(OperatorToken.LT, null, value, value);
        Assertions.assertThrows(IllegalStateException.class, failing::getCurrentValue);

    }

    @Test
    void integerComparisonTest() {
        ConstantValueProvider<Integer> value = new ConstantValueProvider<>(1);
        ConstantValueProvider<Integer> clone = new ConstantValueProvider<>(1);
        ConstantValueProvider<Integer> other = new ConstantValueProvider<>(2);
        testEQNEQ(value, clone, other);

        testWithInversion(true, value, value, OperatorToken.EQ);
        testWithInversion(true, value, clone, OperatorToken.EQ);
        testWithInversion(false, value, value, OperatorToken.NEQ);
        testWithInversion(false, value, clone, OperatorToken.NEQ);
        testWithInversion(true, value, other, OperatorToken.LT);
        testWithInversion(true, value, other, OperatorToken.LEQ);
        testWithInversion(false, value, other, OperatorToken.GT);
        testWithInversion(false, value, other, OperatorToken.GEQ);
    }

    private void testEQNEQ(ConstantValueProvider<?> value,
                           ConstantValueProvider<?> equalValue,
                           ConstantValueProvider<?> otherValue) {
        ComparisonCauseNode identityNode = new ComparisonCauseNode(OperatorToken.EQ, null, value, value);
        ComparisonCauseNode equalNode = new ComparisonCauseNode(OperatorToken.EQ, null, value, equalValue);
        ComparisonCauseNode neqNode = new ComparisonCauseNode(OperatorToken.NEQ, null, value, otherValue);

        Assertions.assertTrue(identityNode.getCurrentValue());
        Assertions.assertTrue(equalNode.getCurrentValue());
        Assertions.assertFalse(neqNode.getCurrentValue());
    }

    private void testWithInversion(boolean expected, ValueProvider<?> value1, ValueProvider<?> value2, OperatorToken op) {
        ComparisonCauseNode node = new ComparisonCauseNode(op, null, value1, value2);
        ComparisonCauseNode invertedOpAndValues = new ComparisonCauseNode(op.invert(), null, value2, value1);
        ComparisonCauseNode invertedOP = new ComparisonCauseNode(op.invert(), null, value1, value2);

        Assertions.assertEquals(expected, node.getCurrentValue());
        Assertions.assertEquals(expected, invertedOpAndValues.getCurrentValue());
        Assertions.assertEquals(!expected, invertedOP.getCurrentValue());

    }
}