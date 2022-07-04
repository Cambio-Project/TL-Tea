package cambio.tltea.interpreter

import cambio.tltea.interpreter.testutils.TestBase
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InputRegressionTests : TestBase() {
    @Test
    internal fun one() {
        super.interpretFormula("((service1\$instances)>5) -> (F[10]((service2\$instances)==5))")
        assertEquals(1, triggerManager.eventActivationListeners.size)
    }

    @Test
    internal fun two() {
        val result = interpretFormula("((example-service.fail) -> (F[10]((example-service.start))))")
        activateEvent("example-service.fail")
        println(result)
    }
}