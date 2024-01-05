package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test

class GloballySimulationTests : SimulationTest() {
    @Test
    fun basicUntimedAlwaysDelayedSatisfaction() {
        val formula = "(G((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(1), metricA, 2.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 4.0)
    }

    @Test
    fun basicUntimedAlwaysLateUnsatisfied() {
        val formula = "(G((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (true)
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(4), metricA, 6.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 1.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 4.0)
    }

    @Test
    fun basicUntimedAlwaysSatisfiedInMid() {
        val formula = "(G((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(1), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(4), metricA, 6.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))

        assertStateEquals(false, 0.0)
        assertStateEquals(false, 1.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 4.0)
    }

    @Test
    fun basicUntimedAlwaysAllSatisfied() {
        val formula = "(G((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (true)
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 4.0)
    }

    @Test
    fun basicTimedAlways() {
        val formula = "(G[0,3]((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(1), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(4.5), metricA, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))

        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(false, 1.5)
        assertStateEquals(false, 1.6)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 3.5)
        assertStateEquals(false, 4.0)
        assertStateEquals(false, 5.0)
        assertStateEquals(false, 6.0)
        assertStateEquals(false, 7.0)
    }

    @Test
    fun basicTimedAlwaysTooShort() {
        val formula = "(G[0,3]((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(1), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(2.9), metricA, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))

        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(false, 1.0)
        assertStateEquals(false, 1.5)
        assertStateEquals(false, 1.6)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 3.5)
        assertStateEquals(false, 4.0)
        assertStateEquals(false, 5.0)
        assertStateEquals(false, 6.0)
        assertStateEquals(false, 7.0)
    }

    @Test
    fun basicTimedAlwaysWithMinBoundary() {
        val formula = "(G[1,3]((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(1), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(3.5), metricA, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))

        assertStateEquals(true, 0.0)
        assertStateEquals(true, 0.4)
        assertStateEquals(false, 0.5)
        assertStateEquals(false, 1.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 3.5)
        assertStateEquals(false, 4.0)
        assertStateEquals(false, 5.0)
        assertStateEquals(false, 6.0)
        assertStateEquals(false, 7.0)
    }

    @Test
    fun basicTimedAlwaysWithInterrupt() {
        val formula = "(G[1,3]((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(2), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(5), metricA, 5.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(6), metricA, 2.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(10))

        assertStateEquals(false, 0.0)
        assertStateEquals(false, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 1.9)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 4.0)
        assertStateEquals(false, 4.9)
        assertStateEquals(true, 5.0)
        assertStateEquals(true, 6.0)
        assertStateEquals(true, 7.0)
    }

    @Test
    fun basicTimedAlwaysSatisfiedFromStart() {
        val formula = "(G[0,3]((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (true)
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))

        assertStateEquals(true, 0.0)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 4.0)
        assertStateEquals(true, 5.0)
        assertStateEquals(true, 6.0)
    }

}