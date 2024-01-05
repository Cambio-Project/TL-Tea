package cambio.tltea.parser.core.temporal;

import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TemporalIntervalTest {

    private final static Random RANDOM = new Random();

    private final static int repetitions = 100;

    @RepeatedTest(repetitions)
    void getStart_getEnd() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end);
        assertEquals(start, interval.getStartAsDouble());
        assertEquals(end, interval.getEndAsDouble());
    }

    @RepeatedTest(repetitions)
    void isStartInclusive() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end);
        assertTrue(interval.isStartInclusive());

        var interval2 = new TemporalInterval(start, end, false);
        assertFalse(interval2.isStartInclusive());
        assertFalse(interval2.contains(start));

        var interval3 = new TemporalInterval(start, end, false, false);
        assertFalse(interval3.isStartInclusive());
        assertFalse(interval3.contains(start));
    }

    @RepeatedTest(repetitions)
    void isEndInclusive() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end);
        assertTrue(interval.isEndInclusive());

        var interval2 = new TemporalInterval(start, end, false, false);
        assertFalse(interval2.isEndInclusive());
        assertFalse(interval2.contains(end));

        var interval3 = new TemporalInterval(start, end, false, false);
        assertFalse(interval3.isEndInclusive());
        assertFalse(interval3.contains(end));
    }

    @RepeatedTest(repetitions)
    void contains() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end);
        assertTrue(interval.contains(start));
        assertTrue(interval.contains(end));

        for (int i = 0; i < 100; i++) {
            var inInterval = start + RANDOM.nextDouble() * (end - start);
            assertTrue(interval.contains(inInterval));
        }
    }


    @RepeatedTest(repetitions)
    void containsInterval() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end);
        assertTrue(interval.contains(start));
        assertTrue(interval.contains(end));

        for (int i = 0; i < 100; i++) {
            var inIntervalStart = start + RANDOM.nextDouble() * (end - start);
            var inIntervalEnd = inIntervalStart + RANDOM.nextDouble() * (end - inIntervalStart);
            var inInterval = new TemporalInterval(inIntervalStart, inIntervalEnd);
            assertTrue(interval.contains(inInterval));
        }

        var exclusiveInterval = new TemporalInterval(start, end, false, false);
        assertTrue(interval.contains(exclusiveInterval));
        var inclusiveInterval = new TemporalInterval(start, end, true, true);
        assertTrue(interval.contains(inclusiveInterval));


        var exclusiveInterval2 = new TemporalInterval(start - (RANDOM.nextDouble() + 0.000000001), end, false, false);
        assertFalse(interval.contains(exclusiveInterval2));
        var inclusiveInterval2 = new TemporalInterval(start, end + (RANDOM.nextDouble() + 0.000000001), true, true);
        assertFalse(interval.contains(inclusiveInterval2));

    }

    @RepeatedTest(repetitions)
    void overlaps() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end);

        for (int i = 0; i < 50; i++) {
            var inIntervalStart = start + RANDOM.nextDouble() * (end - start);
            var inIntervalEnd = inIntervalStart + RANDOM.nextDouble() * (Double.MAX_VALUE - inIntervalStart);
            var inInterval = new TemporalInterval(inIntervalStart, inIntervalEnd);
            assertTrue(interval.overlaps(inInterval));
        }
        for (int i = 0; i < 50; i++) {
            var inIntervalEnd = start + RANDOM.nextDouble() * (end - start);
            var inIntervalStart = RANDOM.nextDouble() * inIntervalEnd;
            var inInterval = new TemporalInterval(inIntervalStart, inIntervalEnd);
            assertTrue(interval.overlaps(inInterval));
        }

        var exclusiveInterval = new TemporalInterval(-1, start, false, false);
        assertFalse(interval.overlaps(exclusiveInterval));
        var exclusiveInterval2 = new TemporalInterval(end, end + RANDOM.nextDouble() * end, false, false);
        assertFalse(interval.overlaps(exclusiveInterval2));

    }

    @RepeatedTest(repetitions)
    void isBefore() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var inclusiveInterval = new TemporalInterval(start, end, true, true);
        var exclusiveInterval = new TemporalInterval(start, end, false, false);

        var beforeInterval = new TemporalInterval(-1, start - (RANDOM.nextDouble() + 0.000000001));
        var beforeInterval2 = new TemporalInterval(-1, start, true, true);

        var overlappingStart = end * RANDOM.nextDouble();
        var overlappingEnd = exclusiveInterval.contains(overlappingStart)
                             ? overlappingStart + (Double.MAX_VALUE - overlappingStart) * RANDOM.nextDouble()
                             : start + (end - start) * RANDOM.nextDouble();
        var overlappingInterval = new TemporalInterval(overlappingStart, overlappingEnd);

        assertTrue(beforeInterval.isBefore(inclusiveInterval));
        assertTrue(inclusiveInterval.isAfter(beforeInterval));

        assertTrue(beforeInterval2.isBefore(exclusiveInterval));
        assertTrue(exclusiveInterval.isAfter(beforeInterval2));

        assertFalse(overlappingInterval.isBefore(inclusiveInterval));
        assertFalse(inclusiveInterval.isAfter(overlappingInterval));
        assertFalse(overlappingInterval.isBefore(exclusiveInterval));
        assertFalse(exclusiveInterval.isAfter(overlappingInterval));
    }


    @RepeatedTest(repetitions)
    void getDuration() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end, RANDOM.nextBoolean(), RANDOM.nextBoolean());
        assertEquals(end - start, interval.getDuration());
    }

    @RepeatedTest(repetitions)
    void testEquals() {
        double start = (Integer.MAX_VALUE / 2.0) * RANDOM.nextDouble();
        double end = start + start * RANDOM.nextDouble();
        var interval = new TemporalInterval(start, end);
        var interval2 = new TemporalInterval(start, end);
        var interval3 = new TemporalInterval(start, end, true, false);
        var interval4 = new TemporalInterval(start, end, false, true);
        var interval5 = new TemporalInterval(start, end, false, false);

        //noinspection SimplifiableAssertion,ConstantConditions
        assertFalse(interval.equals(null));
        assertEquals(interval, interval);
        assertEquals(interval, interval2);
        assertNotEquals(interval, interval3);
        assertNotEquals(interval, interval4);
        assertNotEquals(interval, interval5);

        var interval6 = new TemporalInterval(start + RANDOM.nextDouble(), end);
        var interval7 = new TemporalInterval(start, end + RANDOM.nextDouble());
        var interval8 = new TemporalInterval(start + RANDOM.nextDouble(), end + RANDOM.nextDouble());

        assertNotEquals(interval, interval6);
        assertNotEquals(interval, interval7);
        assertNotEquals(interval, interval8);



    }
}