package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test

class ComposedSimulationTests : SimulationTest() {
    @Test
    fun basicUntimedEventually() {
        val formula = "(G[0,4](F((\$A) < (\$B))))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 4.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 4.0)
    }



    @Test
    fun basicUntimedEventuallyTemp() {
        val formula = "(F((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 4.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(true, 0.0)
    }

    @Test
    fun basicUntimedEventually2() {
        val formula = "(G(F((\$A) < (\$B))))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 4.0)
        simulator.forceEndRound()

        // F (true)
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(3.5), metricA, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(false, 0.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 3.5)
        assertStateEquals(false, 4.0)
    }
}