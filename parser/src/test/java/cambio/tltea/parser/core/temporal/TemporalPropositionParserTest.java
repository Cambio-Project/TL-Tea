package cambio.tltea.parser.core.temporal;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemporalPropositionParserTest {


    private static String stripBrackets(String s) {
        if (s.startsWith("[") && s.endsWith("]")) {
            return s.substring(1, s.length() - 1);
        } else return s;
    }

    private void TemporalEventTest(String input, String expectedOut) {
        ITemporalValue result = TemporalPropositionParser.parse(input);
        assertTrue(result instanceof TemporalEventDescription);
        TemporalEventDescription temporalEventDescription = (TemporalEventDescription) result;
        assertEquals(expectedOut, temporalEventDescription.getValue());
    }

    private void TimeInstantTest(String input, double i) {
        ITemporalValue result = TemporalPropositionParser.parse(input);
        assertTrue(result instanceof TimeInstance);
        TimeInstance timeInstance = (TimeInstance) result;
        assertEquals(i, timeInstance.getTime());
    }


    private void IntervalTest(String input, double expectedStart, double expectedEnd, boolean expectedStartInclusive, boolean expectedEndInclusive) {
        ITemporalValue result = TemporalPropositionParser.parse(input);
        assertTrue(result instanceof DoubleInterval);
        DoubleInterval interval = (DoubleInterval) result;
        assertEquals(expectedStart, interval.getStart());
        assertEquals(expectedEnd, interval.getEnd());
        assertEquals(expectedStartInclusive, interval.isStartInclusive());
        assertEquals(expectedEndInclusive, interval.isEndInclusive());
    }

    @Test
    void TemporalEventDescriptionTest() {
        List<String> inputs = List.of("    [42a]","[[42a]]","[>42a]","[1337,42a]","[endOf Time]");
        for (String input : inputs) {
            String expected = stripBrackets(input.trim());
            System.out.println("Testing: " + input + " -> " + expected);
            TemporalEventTest(input, expected);
        }
    }


    @Test
    void TimeInstanceTest() {
        String input = "[42]";
        TimeInstantTest(input, 42);
    }

    @Test
    void TimeInstanceNoBracketsTest() {
        String input = "42";
        TimeInstantTest(input, 42);
    }

    @Test
    void TimeInstanceDoubleTest() {
        String input = "[42.5]";
        TimeInstantTest(input, 42.5);
    }

    @Test
    void TimeInstanceDoubleNoBracketsTest() {
        String input = "42.5";
        TimeInstantTest(input, 42.5);
    }

    @Test
    void IntervalTest() {
        String input = "[42,43]";
        IntervalTest(input, 42, 43, true, true);
    }

    @Test
    void IntervalNoBracketsTest() {
        String input = "42,43";
        IntervalTest(input, 42, 43, true, true);
    }

    @Test
    void IntervalDoubleTest() {
        String input = "[42.5 ,43.5]";
        IntervalTest(input, 42.5, 43.5, true, true);
    }

    @Test
    void IntervalDoubleNoBracketsTest() {
        String input = "42.5, 43.5";
        IntervalTest(input, 42.5, 43.5, true, true);
    }

    @Test
    void ComparisonEQTest() {
        String input = "[=4]";
        TimeInstantTest(input, 4);
    }

    @Test
    void ComparisonGEQTest() {
        String input = "[>=4]";
        IntervalTest(input, 4, Double.POSITIVE_INFINITY, true, false);
    }

    @Test
    void ComparisonLEQTest() {
        String input = "[<=4]";
        IntervalTest(input, 0, 4, true, true);
    }

    @Test
    void ComparisonGTTest() {
        String input = "[>4]";
        IntervalTest(input, 4, Double.POSITIVE_INFINITY, false, false);
    }

    @Test
    void ComparisonLTTest() {
        String input = "[<4]";
        IntervalTest(input, 0, 4, true, false);
    }

    @Test
    void IntervalInfTest() {
        List<String> l = List.of("inf", "+inf", "INF", "Inf", "infinity", "+infinity", "∞", "+∞");

        for (String end : l) {
            System.out.printf("Testing parsing of %s to infinity%n", end);
            String input = "[42," + end + "]";
            IntervalTest(input, 42, Double.POSITIVE_INFINITY, true, false);
        }
    }

}