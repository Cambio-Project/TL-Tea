package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.parser.core.OperatorToken;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiConsumer;

class ComparisonCauseNodeTest {


    private static void testComparison(boolean expected,
                                       ValueListener<?> value1,
                                       ValueListener<?> value2,
                                       OperatorToken op) {
        ComparisonCauseNode node = new ComparisonCauseNode(op, null, value1, value2);
        Assertions.assertEquals(expected, node.getCurrentValue());
    }

    private static void testThrowsException(ValueListener<?> value1, ValueListener<?> value2, OperatorToken op) {
        try {
            new ComparisonCauseNode(op, null, value1, value2).getCurrentValue();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected IllegalStateException or IllegalArgumentException");
    }

    private static void testEQNEQ(ConstantValueProvider<?> value,
                                  ConstantValueProvider<?> differentValue) {
        testComparison(true, value, value, OperatorToken.EQ); //identity
        testComparison(false, value, value, OperatorToken.NEQ); //inverted identity
        testComparison(true, value, value.clone(), OperatorToken.EQ); //equality
        testComparison(false, value, value.clone(), OperatorToken.NEQ); //inverted equality
        testComparison(false, value, differentValue, OperatorToken.EQ); //difference
        testComparison(true, value, differentValue, OperatorToken.NEQ); //inverted difference
    }


    @Test
    void stringComparisonTest() {
        ConstantValueProvider<String> value = new ConstantValueProvider<>("a");
        ConstantValueProvider<String> other = new ConstantValueProvider<>("B");
        testEQNEQ(value, other);
        testThrowsException(value, other, OperatorToken.GT);
        testThrowsException(value, other, OperatorToken.GEQ);
        testThrowsException(value, other, OperatorToken.LT);
        testThrowsException(value, other, OperatorToken.LEQ);

    }


    private static void testComparisonOperators(ValueListener<Number> smallProvider,
                                                ValueListener<Number> largeProvider) {
        testComparison(true, smallProvider, smallProvider, OperatorToken.EQ);
        testComparison(true, smallProvider, smallProvider.clone(), OperatorToken.EQ);
        testComparison(false, smallProvider, smallProvider, OperatorToken.NEQ);
        testComparison(false, smallProvider, smallProvider.clone(), OperatorToken.NEQ);
        testComparison(true, smallProvider, largeProvider, OperatorToken.LT);
        testComparison(false, smallProvider, largeProvider, OperatorToken.LT.invert());
        testComparison(true, smallProvider, largeProvider, OperatorToken.LEQ);
        testComparison(false, smallProvider, largeProvider, OperatorToken.LEQ.invert());
        testComparison(false, smallProvider, largeProvider, OperatorToken.GT);
        testComparison(true, smallProvider, largeProvider, OperatorToken.GT.invert());
        testComparison(false, smallProvider, largeProvider, OperatorToken.GEQ);
        testComparison(true, smallProvider, largeProvider, OperatorToken.GEQ.invert());
    }

    @Test
    void numberComparisonTest() {

        Random rng = new Random();

        BiConsumer<Number, Number> constantNumberProviderTest = (smaller, larger) -> {
            ConstantValueProvider<Number> smallProvider = new ConstantValueProvider<>(smaller);
            ConstantValueProvider<Number> largeProvider = new ConstantValueProvider<>(larger);

            testEQNEQ(smallProvider, largeProvider);

            testComparisonOperators(smallProvider, largeProvider);

            Arrays.stream(OperatorToken.values())
                  .filter(op -> !OperatorToken.ComparisonOperatorTokens.contains(op))
                  .forEach(op -> testThrowsException(smallProvider, largeProvider, op));
        };


        for (int i = 0; i < 1000; i++) {
            int other, value = rng.nextInt();

            do {
                other = rng.nextInt();
            } while (other >= value);

            constantNumberProviderTest.accept(other, value);
        }

        for (int i = 0; i < 1000; i++) {
            double other, value = Integer.MAX_VALUE * rng.nextDouble();

            do {
                other = Integer.MAX_VALUE * rng.nextDouble();
            } while (other >= value);

            constantNumberProviderTest.accept(other, value);
        }
    }

    abstract static class Parent<T> implements Comparable<Parent<T>> {
        protected final T value;

        @Override
        public abstract int compareTo(@NotNull ComparisonCauseNodeTest.Parent o);

        public Parent(T value) {
            this.value = value;
        }

    }

    static class Child1<T> extends Parent<T> {

        public Child1(T value) {
            super(value);
        }

        @Override
        public int compareTo(@NotNull ComparisonCauseNodeTest.Parent o) {
            return value == o.value ? 0 : value.hashCode() - o.value.hashCode();
        }
    }

    static class Child2<T> extends Parent<T> {

        public Child2(T value) {
            super(value);
        }

        @Override
        public int compareTo(@NotNull ComparisonCauseNodeTest.Parent o) {
            return value == o.value ? 0 : value.hashCode() - o.value.hashCode();
        }
    }


    static class IntComparable implements Comparable<Integer>{

        private final int value;

        public IntComparable(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(@NotNull Integer o) {
            return value == o ? 0 : value - o;
        }
    }

    static class NotComparable {
        public final int value;
        NotComparable(int value) {
            this.value = value;
        }
    }
    static class NotComparableComparable implements Comparable<NotComparable>{
        public final int value;
        NotComparableComparable(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(@NotNull ComparisonCauseNodeTest.NotComparable o) {
            return value == o.value ? 0 : value - o.value;
        }
    }

    @Test
    void objectComparisonTest() {
        Parent<Integer> parent1 = new Child1<>(1);
        Parent<Integer> parent2 = new Child2<>(2);

        var smallProvider = new ConstantValueProvider<>(parent1);
        var largeProvider = new ConstantValueProvider<>(parent2);

        testComparison(true, smallProvider, smallProvider, OperatorToken.EQ);
        testComparison(true, smallProvider, smallProvider.clone(), OperatorToken.EQ);
        testComparison(false, smallProvider, smallProvider, OperatorToken.NEQ);
        testComparison(false, smallProvider, smallProvider.clone(), OperatorToken.NEQ);
        testComparison(true, smallProvider, largeProvider, OperatorToken.LT);
        testComparison(false, smallProvider, largeProvider, OperatorToken.LT.invert());
        testComparison(true, smallProvider, largeProvider, OperatorToken.LEQ);
        testComparison(false, smallProvider, largeProvider, OperatorToken.LEQ.invert());
        testComparison(false, smallProvider, largeProvider, OperatorToken.GT);
        testComparison(true, smallProvider, largeProvider, OperatorToken.GT.invert());
        testComparison(false, smallProvider, largeProvider, OperatorToken.GEQ);
        testComparison(true, smallProvider, largeProvider, OperatorToken.GEQ.invert());

    }

    @Test
    void oneWayObjectComparisonTest(){

        var smallProvider2 = new ConstantValueProvider<>(1);
        var largeProvider2 = new ConstantValueProvider<>(new IntComparable(2));

        testComparison(true, smallProvider2, smallProvider2, OperatorToken.EQ);
        testComparison(true, smallProvider2, smallProvider2.clone(), OperatorToken.EQ);
        testComparison(false, smallProvider2, smallProvider2, OperatorToken.NEQ);
        testComparison(false, smallProvider2, smallProvider2.clone(), OperatorToken.NEQ);
        testComparison(true, smallProvider2, largeProvider2, OperatorToken.LT);
        testComparison(false, smallProvider2, largeProvider2, OperatorToken.LT.invert());
        testComparison(true, smallProvider2, largeProvider2, OperatorToken.LEQ);
        testComparison(false, smallProvider2, largeProvider2, OperatorToken.LEQ.invert());
        testComparison(false, smallProvider2, largeProvider2, OperatorToken.GT);
        testComparison(true, smallProvider2, largeProvider2, OperatorToken.GT.invert());
        testComparison(false, smallProvider2, largeProvider2, OperatorToken.GEQ);
        testComparison(true, smallProvider2, largeProvider2, OperatorToken.GEQ.invert());



        var notComparable = new ConstantValueProvider<>(new NotComparable(1));
        var notComparableComparable = new ConstantValueProvider<>(new NotComparableComparable(1));

        testComparison(true, notComparable, notComparableComparable, OperatorToken.EQ);
        testComparison(true, notComparable, notComparableComparable.clone(), OperatorToken.EQ);
        testComparison(false, notComparable, notComparableComparable, OperatorToken.NEQ);
        testComparison(false, notComparable, notComparableComparable.clone(), OperatorToken.NEQ);
    }
}