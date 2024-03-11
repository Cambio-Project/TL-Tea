package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test

class BoolSimulationTests : SimulationTest() {
    @Test
    fun constantTrue() {
        val formula = "((true))"
        load(formula)
        simulator.forceEndRound()
        assertStateEquals(true)
    }

    @Test
    fun constantFalse() {
        val formula = "((false))"
        load(formula)
        simulator.forceEndRound()
        assertStateEquals(false)
    }

    @Test
    fun and() {
        val formula = "(((\$A) == 5) & ((\$B) == 2))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A != 5 AND B != 2
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A = 5 AND B = 5
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A != 5 AND B = 5
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A != 5 AND B = 5
        simulator.forceHandle(TimeInstance(4), metricA, 1.9)
        simulator.forceEndRound()
        assertStateEquals(false)
    }

    @Test
    fun or() {
        val formula = "(((\$A) == 5) | ((\$B) == 2))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A != 5 OR B != 2
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A = 5 OR B = 2
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A != 5 OR B == 2
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A != 5 OR B != 2
        simulator.forceHandle(TimeInstance(4), metricB, 1.9)
        simulator.forceEndRound()
        assertStateEquals(false)
    }


    @Test
    fun not() {
        val formula = "(!((\$A) == 2))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)

        // !(A = 2)
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // !(A != 2)
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // !(A = 2)
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)
    }

    @Test
    fun implication() {
        val formula = "(((\$A)==2) => ((\$B)==5))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)
    }


    @Test
    fun iff() {
        val formula = "(((\$A)==2) <-> ((\$B)==5))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(false)
    }

}