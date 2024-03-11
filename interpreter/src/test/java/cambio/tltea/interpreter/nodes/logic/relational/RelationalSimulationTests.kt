package cambio.tltea.interpreter.nodes.logic.relational

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test

class RelationalSimulationTests : SimulationTest() {
    @Test
    fun lowerThan() {
        val formula = "((\$A) < (\$B))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricA, 1.9)
        simulator.forceEndRound()
        assertStateEquals(true)
    }

    @Test
    fun lowerEquals() {
        val formula = "((\$A) <= (\$B))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricA, 1.9)
        simulator.forceEndRound()
        assertStateEquals(true)
    }

    @Test
    fun greaterThan(){
        val formula = "((\$A) > (\$B))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricA, 1.9)
        simulator.forceEndRound()
        assertStateEquals(false)
    }


    @Test
    fun greaterEquals(){
        val formula = "((\$A) >= (\$B))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricA, 1.9)
        simulator.forceEndRound()
        assertStateEquals(false)
    }


    @Test
    fun equals(){
        val formula = "((\$A) == (\$B))"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)
        val metricB = MetricDescriptor("", "B", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceHandle(TimeInstance(0), metricB, 5.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceHandle(TimeInstance(2), metricB, 2.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricA, 1.9)
        simulator.forceEndRound()
        assertStateEquals(false)
    }


    @Test
    fun booleanEquals(){
        val formula = "((\$b:A) == (true))"
        load(formula)

        val metricA = MetricDescriptor("", "A", true)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, false)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricA, false)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, true)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricA, false)
        simulator.forceEndRound()
        assertStateEquals(false)
    }


    @Test
    fun equalsValue() {
        val formula = "((\$A) == 2)"
        load(formula)

        val metricA = MetricDescriptor("", "A", false)

        // A smaller B
        simulator.forceHandle(TimeInstance(0), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A greater B
        simulator.forceHandle(TimeInstance(2), metricA, 5.0)
        simulator.forceEndRound()
        assertStateEquals(false)

        // A equals B
        simulator.forceHandle(TimeInstance(3), metricA, 2.0)
        simulator.forceEndRound()
        assertStateEquals(true)

        // A smaller B
        simulator.forceHandle(TimeInstance(4), metricA, 1.9)
        simulator.forceEndRound()
        assertStateEquals(false)
    }


}