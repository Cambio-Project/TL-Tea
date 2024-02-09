package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEventLog
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimeEventLogTest {

    @Test
    fun singleIntervalViaEventsTest() {
        val startTime = 2.0
        val endTime = 4.0
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance = TimeInstance(endTime, true)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance))
        log.add(TimeEvent.end(endTimeInstance))

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertTrue(interval.isEndInclusive)
    }

    @Test
    fun singleIntervalTest() {
        val startTime = 2.0
        val endTime = 4.0
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance = TimeInstance(endTime, true)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance)
        )

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertTrue(interval.isEndInclusive)
    }

    @Test
    fun startEqualsEndIntervalTest() {
        val startTime = 4.0
        val endTime = 4.0
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance = TimeInstance(endTime)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance)
        )

        val intervals = log.findPositiveIntervals()
        assertEquals(0, intervals.size)
    }

    @Test
    fun startEqualsEndIntervalViaEventsTest() {
        val startTime = 4.0
        val endTime = 4.0
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance = TimeInstance(endTime)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance))
        log.add(TimeEvent.end(endTimeInstance))

        val intervals = log.findPositiveIntervals()
        assertEquals(0, intervals.size)
    }

    @Test
    fun singlePointIntervalTest() {
        val startTime = 4.0
        val endTime = 4.0
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance = TimeInstance(endTime, true)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance)
        )

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertTrue(interval.isEndInclusive)
    }

    @Test
    fun singlePointIntervalViaEventsTest() {
        val startTime = 4.0
        val endTime = 4.0
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance = TimeInstance(endTime, true)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance))
        log.add(TimeEvent.end(endTimeInstance))

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertTrue(interval.isEndInclusive)
    }


    //TODO: Test not that meaningful
    @Test
    fun beginningOpenIntervalTest() {
        val endTime = 4.0
        val endTimeInstance = TimeInstance(endTime, true)
        val log = TimeEventLog()

        log.add( TimeEvent.end(endTimeInstance))

        val intervals = log.findPositiveIntervals()
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
        val startTimeInstance = TimeInstance(startTime)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance))

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }

    // TODO: Does no longer make sense
    @Test
    fun singleIntervalReverseEnteredTest() {
        val startTime = 2.0
        val endTime = 4.0
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance = TimeInstance(endTime, true)
        val log = TimeEventLog()

        log.add(TimeEvent.end(endTimeInstance))
        log.add(TimeEvent.start(startTimeInstance))

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)

        assertTrue(interval.isStartInclusive)
        assertTrue(interval.isEndInclusive)
    }

    @Test
    fun multipleIntervalTest() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTime2 = 4.0
        val endTime2 = 5.0
        val startTime3 = 5.0
        val endTime3 = 6.0
        val startTimeInstance1 = TimeInstance(startTime1)
        val endTimeInstance1 = TimeInstance(endTime1)
        val startTimeInstance2 = TimeInstance(startTime2)
        val endTimeInstance2 = TimeInstance(endTime2)
        val startTimeInstance3 = TimeInstance(startTime3)
        val endTimeInstance3 = TimeInstance(endTime3)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance1),
            TimeEvent.end(endTimeInstance1)
        )
        log.addInterval(
            TimeEvent.start(startTimeInstance2),
            TimeEvent.end(endTimeInstance2)
        )
        log.addInterval(
            TimeEvent.start(startTimeInstance3),
            TimeEvent.end(endTimeInstance3)
        )

        val intervals = log.findPositiveIntervals()
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
    fun multipleIntervalAsEventsTest() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTime2 = 4.0
        val endTime2 = 5.0
        val startTime3 = 5.0
        val endTime3 = 6.0
        val startTimeInstance1 = TimeInstance(startTime1)
        val endTimeInstance1 = TimeInstance(endTime1)
        val startTimeInstance2 = TimeInstance(startTime2)
        val endTimeInstance2 = TimeInstance(endTime2)
        val startTimeInstance3 = TimeInstance(startTime3)
        val endTimeInstance3 = TimeInstance(endTime3)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance1))
        log.add(TimeEvent.end(endTimeInstance1))
        log.add(TimeEvent.start(startTimeInstance2))
        log.add(TimeEvent.end(endTimeInstance2))
        log.add(TimeEvent.start(startTimeInstance3))
        log.add(TimeEvent.end(endTimeInstance3))

        val intervals = log.findPositiveIntervals()
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
        val startTimeInstance1 = TimeInstance(startTime1)
        val endTimeInstance1 = TimeInstance(endTime1)
        val startTimeInstance2 = TimeInstance(startTime2)
        val endTimeInstance2 = TimeInstance(endTime2)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance1),
            TimeEvent.end(endTimeInstance1)
        )
        log.addInterval(
            TimeEvent.start(startTimeInstance2),
            TimeEvent.end(endTimeInstance2)
        )

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval1 = intervals[0]
        assertEquals(startTime1, interval1.startAsDouble)
        assertEquals(endTime2, interval1.endAsDouble)
        assertTrue(interval1.isStartInclusive)
        assertFalse(interval1.isEndInclusive)
    }

    @Test
    fun beginningOpenClosedIntervalViaEventsConnection() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTime2 = 3.0
        val endTime2 = 5.0
        val startTimeInstance1 = TimeInstance(startTime1)
        val endTimeInstance1 = TimeInstance(endTime1)
        val startTimeInstance2 = TimeInstance(startTime2)
        val endTimeInstance2 = TimeInstance(endTime2)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance1))
        log.add(TimeEvent.end(endTimeInstance1))
        log.add(TimeEvent.start(startTimeInstance2))
        log.add(TimeEvent.end(endTimeInstance2))

        val intervals = log.findPositiveIntervals()
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
        val startTimeInstance1 = TimeInstance(startTime1)
        val endTimeInstance1 = TimeInstance(endTime1)
        val startTimeInstance2 = TimeInstance(startTime2,true)
        val endTimeInstance2 = TimeInstance(endTime2)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance1),
            TimeEvent.end(endTimeInstance1)
        )
        log.addInterval(
            TimeEvent.start(startTimeInstance2),
            TimeEvent.end(endTimeInstance2)
        )


        val intervals = log.findPositiveIntervals()
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
    fun beginningOpenClosedIntervalNotConnectionViaEvents() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTime2 = 3.0
        val endTime2 = 5.0
        val startTimeInstance1 = TimeInstance(startTime1)
        val endTimeInstance1 = TimeInstance(endTime1)
        val startTimeInstance2 = TimeInstance(startTime2,true)
        val endTimeInstance2 = TimeInstance(endTime2)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance1))
        log.add(TimeEvent.end(endTimeInstance1))
        log.add(TimeEvent.start(startTimeInstance2))
        log.add(TimeEvent.end(endTimeInstance2))


        val intervals = log.findPositiveIntervals()
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
        val startTimeInstance1 = TimeInstance(startTime1)
        val startTimeInstance2 = TimeInstance(startTime2)
        val endTimeInstance = TimeInstance(endTime)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance1))
        log.addInterval(
            TimeEvent.start(startTimeInstance2),
            TimeEvent.end(endTimeInstance)
        )

        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime1, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)
        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }

    @Test
    fun beginningOverrideIntervalTest2() {
        val startTime1 = 1.0
        val startTime2 = 3.0
        val endTime = 5.0
        val startTimeInstance1 = TimeInstance(startTime1)
        val startTimeInstance2 = TimeInstance(startTime2)
        val endTimeInstance = TimeInstance(endTime)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance1),
            TimeEvent.end(endTimeInstance)
        )
        log.add(TimeEvent.start(startTimeInstance2))


        val intervals = log.findPositiveIntervals()
        assertEquals(1, intervals.size)

        val interval = intervals[0]
        assertEquals(startTime1, interval.startAsDouble)
        assertEquals(endTime, interval.endAsDouble)
        assertTrue(interval.isStartInclusive)
        assertFalse(interval.isEndInclusive)
    }


    @Test
    fun beginningOverrideIntervalViaEventsTest() {
        val startTime1 = 1.0
        val startTime2 = 3.0
        val endTime = 5.0
        val startTimeInstance1 = TimeInstance(startTime1)
        val startTimeInstance2 = TimeInstance(startTime2,true)
        val endTimeInstance = TimeInstance(endTime)
        val log = TimeEventLog()

        log.add(TimeEvent.start(startTimeInstance1))
        log.add(TimeEvent.start(startTimeInstance2))
        log.add(TimeEvent.end(endTimeInstance))

        val intervals = log.findPositiveIntervals()
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
        val startTimeInstance = TimeInstance(startTime)
        val endTimeInstance1 = TimeInstance(endTime1)
        val endTimeInstance2 = TimeInstance(endTime2)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance1)
        )
        log.delayEvent(endTimeInstance1, endTimeInstance2,false)

        val intervals = log.findPositiveIntervals()
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
        val startTimeInstance1 = TimeInstance(startTime1)
        val endTimeInstance1 = TimeInstance(endTime1)
        val startTimeInstance2 = TimeInstance(startTime2)
        val endTimeInstance2 = TimeInstance(endTime2)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance1),
            TimeEvent.end(endTimeInstance1)
        )
        log.addInterval(
            TimeEvent.start(startTimeInstance2),
            TimeEvent.end(endTimeInstance2)
        )

        val nextStart = log.findNextEventWithSameValueExcluding(TimeInstance(findPoint), true)

        assertEquals(5.0, nextStart.second.time.time)
    }

    @Test
    fun findNextStartIncluding() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val findPoint = 1.0
        val startTimeInstance = TimeInstance(startTime1)
        val endTimeInstance = TimeInstance(endTime1)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance)
        )

        val nextStart = log.findNextEventWithSameValueExcluding(TimeInstance(findPoint), true)

        assertEquals(Double.POSITIVE_INFINITY, nextStart.second.time.time)
    }

    @Test
    fun findNextStartExcluding() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val findPoint = 1.0
        val startTimeInstance = TimeInstance(startTime1, true)
        val endTimeInstance = TimeInstance(endTime1)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance)
        )

        val nextStart = log.findNextEventWithSameValueExcluding(TimeInstance(findPoint), true)

        assertEquals(1.0, nextStart.second.time.time)
    }

    @Test
    fun findNextStartNoElement() {
        val log = TimeEventLog()
        val findPoint = 1.0
        val nextStart = log.findNextEventWithSameValueExcluding(TimeInstance(findPoint), true)
        assertEquals(Double.POSITIVE_INFINITY, nextStart.second.time.time)
    }

    @Test
    fun evaluate() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTimeInstance = TimeInstance(startTime1)
        val endTimeInstance = TimeInstance(endTime1)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance)
        )

        assertFalse(log.evaluate(TimeInstance(0)))
        assertTrue(log.evaluate(TimeInstance(1)))
        assertTrue(log.evaluate(TimeInstance(2)))
        assertTrue(log.evaluate(TimeInstance(2.9)))
        assertFalse(log.evaluate(TimeInstance(3)))
        assertFalse(log.evaluate(TimeInstance(4)))
    }

    @Test
    fun evaluateSpecialCases() {
        val startTime1 = 1.0
        val endTime1 = 3.0
        val startTimeInstance = TimeInstance(startTime1,true)
        val endTimeInstance = TimeInstance(endTime1, true)
        val log = TimeEventLog()

        log.addInterval(
            TimeEvent.start(startTimeInstance),
            TimeEvent.end(endTimeInstance)
        )

        assertFalse(log.evaluate(TimeInstance(0)))
        assertFalse(log.evaluate(TimeInstance(1)))
        assertTrue(log.evaluate(TimeInstance(2)))
        assertTrue(log.evaluate(TimeInstance(2.9)))
        assertTrue(log.evaluate(TimeInstance(3)))
        assertFalse(log.evaluate(TimeInstance(3.1)))
        assertFalse(log.evaluate(TimeInstance(4)))
    }

}