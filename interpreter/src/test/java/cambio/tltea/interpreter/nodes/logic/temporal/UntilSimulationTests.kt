package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test

class UntilSimulationTests : SimulationTest() {
    @Test
    fun basicUntimedUntil() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(3), metricA, 0.0)
        simulator.forceHandle(TimeInstance(3), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(3.1), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(false, 3.1)
        assertStateEquals(false, 4.0)
    }

    @Test
    fun basicUntimedUntilSatisfiedAtStart() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (true) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 1.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(3), metricA, 0.0)
        simulator.forceHandle(TimeInstance(3), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(3.1), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(false, 3.1)
        assertStateEquals(false, 4.0)
    }

    @Test
    fun basicUntimedUntilUnsatisfied() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (true) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 1.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(3), metricA, 10.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(false, 1.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 3.1)
        assertStateEquals(false, 4.0)
    }

    @Test
    fun basicUntimedOnlyRelease() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(3), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(3.1), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(false, 1.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(false, 3.1)
        assertStateEquals(false, 4.0)
    }

    @Test
    fun basicUntimedImmediateStart() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (true) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 1.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(10), metricC, 1.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(10))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 9.0)
        assertStateEquals(true, 10.0)
    }


    @Test
    fun basicUntimedTrueThenFalse() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (true) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 1.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(7), metricC, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(8), metricC, 0.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(9), metricA, 0.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(10), metricA, 1.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(21), metricC, 1.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(21))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 7.0)
        assertStateEquals(false, 8.0)
        assertStateEquals(false, 9.0)
        assertStateEquals(true, 10.0)
        assertStateEquals(true, 21.0)
    }

    @Test
    fun basicUntimedUntilDoubleOccurrence() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(3), metricA, 0.0)
        simulator.forceHandle(TimeInstance(3), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(3.1), metricC, 0.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(5), metricA, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(6), metricA, 0.0)
        simulator.forceHandle(TimeInstance(6), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(6.1), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(false, 3.1)
        assertStateEquals(false, 4.0)
        assertStateEquals(true, 5.0)
        assertStateEquals(true, 6.0)
        assertStateEquals(false, 6.1)
        assertStateEquals(false, 7.0)
    }


    @Test
    fun basicUntimedWithInterruption() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(2), metricA, 0.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(3), metricA, 1.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(6), metricC, 1.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 6.0)
        assertStateEquals(true, 7.0)
    }


    @Test
    fun basicUntimedUntilDoubleOccurrenceContinuous() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(3), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(6), metricA, 0.0)
        simulator.forceHandle(TimeInstance(6), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(6.1), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 3.1)
        assertStateEquals(true, 4.0)
        assertStateEquals(true, 5.0)
        assertStateEquals(true, 6.0)
        assertStateEquals(false, 6.1)
        assertStateEquals(false, 7.0)
    }


    @Test
    fun basicUntimedUntilEndTwice() {
        val formula = "(((\$A) = (\$B))U((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(3), metricC, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(4), metricC, 0.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(6), metricC, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(7), metricC, 0.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(7.1), metricA, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 3.9)
        assertStateEquals(true, 4.0)
        assertStateEquals(true, 5.0)
        assertStateEquals(true, 6.0)
        assertStateEquals(true, 6.9)
        assertStateEquals(false, 7.0)
    }

    @Test
    fun basicTimedUntilSatisfied() {
        val formula = "(((\$A) = (\$B))U[1,3]((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(3), metricA, 0.0)
        simulator.forceHandle(TimeInstance(3), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(3.1), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(false, 2.1)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 4.0)
    }


    @Test
    fun basicTimedLongUntilSatisfied() {
        val formula = "(((\$A) = (\$B))U[1,3]((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(2), metricA, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(5), metricA, 0.0)
        simulator.forceHandle(TimeInstance(5), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(5.1), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(7))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(false, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 4.0)
        assertStateEquals(false, 4.1)
        assertStateEquals(false, 5.0)
        assertStateEquals(false, 6.0)
    }


    @Test
    fun basicTimedSatisfiedLonger() {
        val formula = "(((\$A) = (\$B))U[1,3]((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(2), metricA, 1.0)
        simulator.forceEndRound()

        // (true) U (true)
        simulator.forceHandle(TimeInstance(5), metricC, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(5.1), metricC, 0.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(7), metricA, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(false, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 4.0)
        assertStateEquals(false, 4.1)
        assertStateEquals(false, 5.0)
        assertStateEquals(false, 6.0)
    }

    @Test
    fun basicTimedUntilUnsatisfied() {
        val formula = "(((\$A) = (\$B))U[1,3]((\$C) = (\$D)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")
        val metricC = MetricDescriptor("", "C")
        val metricD = MetricDescriptor("", "D")

        // (false) U (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceHandle(TimeInstance(0), metricC, 0.0)
        simulator.forceHandle(TimeInstance(0), metricD, 1.0)
        simulator.forceEndRound()

        // (true) U (false)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (false) U (true)
        simulator.forceHandle(TimeInstance(1.5), metricA, 0.0)
        simulator.forceHandle(TimeInstance(1.5), metricC, 1.0)
        simulator.forceEndRound()

        // (false) U (false)
        simulator.forceHandle(TimeInstance(1.6), metricC, 0.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.4)
        assertStateEquals(false, 0.9)
        assertStateEquals(false, 1.0)
        assertStateEquals(false, 1.4)
        assertStateEquals(false, 1.5)
        assertStateEquals(false, 2.0)
    }

}