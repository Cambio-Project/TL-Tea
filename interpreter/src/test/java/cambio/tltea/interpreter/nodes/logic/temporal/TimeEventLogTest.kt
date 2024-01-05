package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimeEventLogTest {

    @Test
    fun singleIntervalTest() {
        val startTime = 2.0
        val endTime = 4.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime))
        log.addEndEvent(TimeInstance(endTime))

        val intervals = log.findIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }

    @Test
    fun startEqualsEndIntervalTest() {
        val startTime = 4.0
        val endTime = 4.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime), true)
        log.addEndEvent(TimeInstance(endTime), false)

        val intervals = log.findIntervals()
        assertEquals(0, intervals.size)
    }

    @Test
    fun singlePointIntervalTest() {
        val startTime = 4.0
        val endTime = 4.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime), true)
        log.addEndEvent(TimeInstance(endTime), true)

        val intervals = log.findIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertTrue(interval.isEndInclusive)
    }

    @Test
    fun beginningOpenIntervalTest() {
        val startTime = 0.0
        val endTime = 4.0
        val log = TimeEventLog()
        log.addEndEvent(TimeInstance(endTime))

        val intervals = log.findIntervals()
        assertEquals(0, intervals.size)

        //val interval = intervals[0]
        //assertEquals(startTime,interval.startAsDouble)
        //assertEquals(endTime, interval.endAsDouble)

        //assertTrue(interval.isStartInclusive)
        //assertFalse(interval.isEndInclusive)
    }

    @Test
    fun endOpenIntervalTest() {
        val startTime = 4.0
        val endTime = Double.POSITIVE_INFINITY
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime), true)

        val intervals = log.findIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }

    @Test
    fun singleIntervalReverseEnteredTest() {
        val startTime = 2.0
        val endTime = 4.0
        val log = TimeEventLog()
        log.addEndEvent(TimeInstance(endTime))
        log.addStartEvent(TimeInstance(startTime))

        val intervals = log.findIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }

    @Test
    fun multipleIntervalTest() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTime2 = 4.0
        val endTime2 = 5.0
        val startTime3 = 5.0
        val endTime3 = 6.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime1))
        log.addEndEvent(TimeInstance(endTime1))
        log.addStartEvent(TimeInstance(startTime2))
        log.addEndEvent(TimeInstance(endTime2))
        log.addStartEvent(TimeInstance(startTime3))
        log.addEndEvent(TimeInstance(endTime3))

        val intervals = log.findIntervals()
        assertEquals(2, intervals.size)

        val interval1 = intervals[0]
        assertEquals(startTime1, interval1.startAsDouble)
        assertEquals(endTime1, interval1.endAsDouble)
        assertTrue(interval1.isStartInclusive)
        assertFalse(interval1.isEndInclusive)

        val interval2 = intervals[1]
        assertEquals(startTime2, interval2.startAsDouble)
        assertEquals(endTime3, interval2.endAsDouble)
        assertTrue(interval2.isStartInclusive)
        assertFalse(interval2.isEndInclusive)
    }

    @Test
    fun beginningOpenClosedIntervalConnection() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTime2 = 3.0
        val endTime2 = 5.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime1))
        log.addEndEvent(TimeInstance(endTime1))
        log.addStartEvent(TimeInstance(startTime2))
        log.addEndEvent(TimeInstance(endTime2))

        val intervals = log.findIntervals()
        assertEquals(1, intervals.size)

        val interval1 = intervals[0]
        assertEquals(startTime1, interval1.startAsDouble)
        assertEquals(endTime2, interval1.endAsDouble)
        assertTrue(interval1.isStartInclusive)
        assertFalse(interval1.isEndInclusive)
    }

    @Test
    fun beginningOpenClosedIntervalNotConnection() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTime2 = 3.0
        val endTime2 = 5.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime1))
        log.addEndEvent(TimeInstance(endTime1), false)
        log.addStartEvent(TimeInstance(startTime2), false)
        log.addEndEvent(TimeInstance(endTime2))

        val intervals = log.findIntervals()
        assertEquals(2, intervals.size)

        val interval1 = intervals[0]
        assertEquals(startTime1, interval1.startAsDouble)
        assertEquals(endTime1, interval1.endAsDouble)
        assertTrue(interval1.isStartInclusive)
        assertFalse(interval1.isEndInclusive)

        val interval2 = intervals[1]
        assertEquals(startTime2, interval2.startAsDouble)
        assertEquals(endTime2, interval2.endAsDouble)
        assertFalse(interval2.isStartInclusive)
        assertFalse(interval2.isEndInclusive)
    }

    @Test
    fun beginningOverrideIntervalTest() {
        val startTime1 = 1.0
        val startTime2 = 3.0
        val endTime = 5.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime1))
        log.addStartEvent(TimeInstance(startTime2))
        log.addEndEvent(TimeInstance(endTime))

        val intervals = log.findIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime1, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)
        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }

    @Test
    fun endOverrideIntervalTest() {
        val startTime = 1.0
        val endTime1 = 3.0
        val endTime2 = 5.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime))
        log.addEndEvent(TimeInstance(endTime1))
        log.addEndEvent(TimeInstance(endTime2))

        val intervals = log.findIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime2, interval.endAsDouble)
        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }

    @Test
    fun findNextStartNormal() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val findPoint = 4.0
        val startTime2 = 5.0
        val endTime2 = 6.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime1))
        log.addEndEvent(TimeInstance(endTime1))
        log.addStartEvent(TimeInstance(startTime2))
        log.addEndEvent(TimeInstance(endTime2))

        val nextStart = log.findNextStart(TimeInstance(findPoint))
        assertEquals(5.0, nextStart.time.time)
    }

    @Test
    fun findNextStartIncluding() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val findPoint = 1.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime1), true)
        log.addEndEvent(TimeInstance(endTime1))

        val nextStart = log.findNextStart(TimeInstance(findPoint))
        assertEquals(Double.POSITIVE_INFINITY, nextStart.time.time)
    }

    @Test
    fun findNextStartExcluding() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val findPoint = 1.0
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(startTime1), false)
        log.addEndEvent(TimeInstance(endTime1))

        val nextStart = log.findNextStart(TimeInstance(findPoint))
        assertEquals(1.0, nextStart.time.time)
    }

    @Test
    fun findNextStartNoElement() {
        val log = TimeEventLog()
        val findPoint = 1.0
        val nextStart = log.findNextStart(TimeInstance(findPoint))
        assertEquals(Double.POSITIVE_INFINITY, nextStart.time.time)
    }

    @Test
    fun evaluate() {
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(1))
        log.addEndEvent(TimeInstance(3))
        assertFalse(log.evaluate(TimeInstance(0)))
        assertTrue(log.evaluate(TimeInstance(1)))
        assertTrue(log.evaluate(TimeInstance(2)))
        assertTrue(log.evaluate(TimeInstance(2.9)))
        assertFalse(log.evaluate(TimeInstance(3)))
        assertFalse(log.evaluate(TimeInstance(4)))
    }

    @Test
    fun evaluateSpecialCases() {
        val log = TimeEventLog()
        log.addStartEvent(TimeInstance(1), false)
        log.addEndEvent(TimeInstance(3), true)
        assertFalse(log.evaluate(TimeInstance(0)))
        assertFalse(log.evaluate(TimeInstance(1)))
        assertTrue(log.evaluate(TimeInstance(2)))
        assertTrue(log.evaluate(TimeInstance(2.9)))
        assertTrue(log.evaluate(TimeInstance(3)))
        assertFalse(log.evaluate(TimeInstance(3.1)))
        assertFalse(log.evaluate(TimeInstance(4)))
    }

}