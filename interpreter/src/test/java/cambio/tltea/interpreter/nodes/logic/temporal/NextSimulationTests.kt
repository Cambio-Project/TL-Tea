package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test

class NextSimulationTests : SimulationTest() {
    @Test
    fun basicUntimedNext() {
        val formula = "(X((\$A) = (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // (false)
        simulator.forceHandle(TimeInstance(0), metricA, 0.0)
        simulator.forceHandle(TimeInstance(0), metricB, 1.0)
        simulator.forceEndRound()

        // (true)
        simulator.forceHandle(TimeInstance(1), metricA, 1.0)
        simulator.forceEndRound()

        // (false)
        simulator.forceHandle(TimeInstance(3), metricA, 0.0)
        simulator.forceEndRound()

        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 0.9)
        assertStateEquals(true, 1.0)
        assertStateEquals(true, 1.9)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 4.0)
        assertStateEquals(false, 5.0)
    }

}