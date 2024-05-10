package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.simulator.SimulationTest
import org.junit.jupiter.api.Test

class MultiFormulaTest() : SimulationTest() {

    @Test
    fun runBasicTwoFormulaTest() {
        val formula1 = "((true))"
        val formula2 = "((true))"
        load(listOf(formula1, formula2))
        simulator.forceEndRound()
        assertStateEquals(1, true)
        assertStateEquals(0, true)
    }

}
