package cambio.tltea.interpreter

import cambio.tltea.interpreter.testutils.TestBase
import cambio.tltea.parser.core.temporal.TemporalEventDescription
import cambio.tltea.parser.core.temporal.TimeInstance
import cambio.tltea.parser.mtl.generated.ParseException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class InterpreterTest : TestBase() {

    @Test
    fun testFailOnEmptyString() {
        assertThrows(ParseException::class.java) {
            val res = interpretFormula("")
        }
        assertThrows(ParseException::class.java) {
            interpretFormula("   ")
        }
    }


    @Test
    fun timeInstanceInIntervalTest() {
        interpretFormula("G[5,10]((A)->(B))")

        activateEvent("A", TimeInstance(4.0))
        activateEvent("A", TimeInstance(5.0))
        activateEvent("A", TimeInstance(7.5))
        activateEvent("A", TimeInstance(10.0))
        activateEvent("A", TimeInstance(11.0))

        assertLogSizes(3,0,0)
    }


    @Test
    fun eventIntervalTest() {
        interpretFormula("G[A]((A)->(B))")
        getEventListeners("A")?.activate(TemporalEventDescription("A"))
        getEventListeners("A")?.activate(TemporalEventDescription("B"))

        assertLogSizes(1,0,0)
    }

    @Test
    fun exactTimeInstanceTest() {
        interpretFormula("G[1]((A)->(B))")
        activateEvent("A", TimeInstance(0))
        activateEvent("A", TimeInstance(1))
        activateEvent("A", TimeInstance(2))

        assertLogSizes(1,0,0)
    }

    @Test
    fun complexScenario1() {
        interpretFormula("(A)->((B)->(C))")
        activateEvent("A", TimeInstance(0))
        assertLogSizes(0,0,0)
        activateEvent("B", TimeInstance(0))
        assertLogSizes(1,0,0)
        deactivateEvent("A", TimeInstance(0))
        activateEvent("B", TimeInstance(0))
        assertLogSizes(1,0,0)
    }
}