package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test

class EventuallySimulationTests : SimulationTest() {
    @Test
    fun basicUntimedEventually() {
        val formula = "(F((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        //assertStateEquals(false)

        // F (false)
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 4.0)
        simulator.forceEndRound()
        //assertStateEquals(false)

        // F (true)
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        //assertStateEquals(true)

        // F (false)
        simulator.forceHandle(TimeInstance(3.5), metricA, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(false, 3.5)
        assertStateEquals(false, 5.0)
    }

    @Test
    fun basicTimedEventually() {
        val formula = "(F[0,4]((\$A) < (\$B)))"
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

        assertStateEquals(true, 0.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(false, 3.5)
        assertStateEquals(false, 4.0)
    }


    @Test
    fun basicTimedEventually2() {
        val formula = "(F[4,6]((\$A) < (\$B)))"
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

        simulator.forceEndExperiment(TimeInstance(8))

        assertStateEquals(false, 0.0)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 3.0)
        assertStateEquals(false, 3.5)
        assertStateEquals(false, 4.0)
    }


    @Test
    fun basicTimedEventually3() {
        val formula = "(F[4,6]((\$A) < (\$B)))"
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
        simulator.forceHandle(TimeInstance(5), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(5.5), metricA, 5.0)
        simulator.forceEndRound()

        simulator.forceEndExperiment(TimeInstance(8))

        assertStateEquals(true, 0.0)
        assertStateEquals(true, 1.4)
        assertStateEquals(false, 1.5)
        assertStateEquals(false, 2.0)
        assertStateEquals(false, 5.0)
    }

    @Test
    fun basicTimedEventually4() {
        val formula = "(F[0,3]((\$A) < (\$B)))"
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

        // F (true)
        simulator.forceHandle(TimeInstance(6.5), metricA, 2.0)
        simulator.forceEndRound()

        // F (false)
        simulator.forceHandle(TimeInstance(7.0), metricA, 5.0)
        simulator.forceEndRound()


        simulator.forceEndExperiment(TimeInstance(10))

        assertStateEquals(true, 0.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 3.5)
        assertStateEquals(true, 6.5)
        assertStateEquals(true, 6.9)
        assertStateEquals(false, 7.0)
    }

@Test
fun basicTimedEventually5() {
    val formula = "(F[0,4]((\$A) < (\$B)))"
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
    simulator.forceHandle(TimeInstance(5), metricA, 2.0)
    simulator.forceEndRound()

    // F (false)
    simulator.forceHandle(TimeInstance(5.5), metricA, 5.0)
    simulator.forceEndRound()

    simulator.forceEndExperiment(TimeInstance(6))

    assertStateEquals(false, 0.0)
    assertStateEquals(false, 0.9)
    assertStateEquals(true, 1.0)
    assertStateEquals(true, 3.0)
    assertStateEquals(true, 5.4)
    assertStateEquals(false, 5.5)
}


    @Test
    fun basicUntimedEventually6() {
        val formula = "(F((\$A) < (\$B)))"
        load(formula)

        val metricA = MetricDescriptor("", "A")
        val metricB = MetricDescriptor("", "B")

        // F (false)
        simulator.forceHandle(TimeInstance(0), metricA, 6.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        //assertStateEquals(false)

        // F (false)
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 4.0)
        simulator.forceEndRound()
        //assertStateEquals(false)

        // F (true)
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        //assertStateEquals(true)

        simulator.forceEndExperiment(TimeInstance(5))
        assertStateEquals(true, 0.0)
        assertStateEquals(true, 2.0)
        assertStateEquals(true, 3.0)
        assertStateEquals(true, 3.5)
        assertStateEquals(true, 5.0)
    }
}