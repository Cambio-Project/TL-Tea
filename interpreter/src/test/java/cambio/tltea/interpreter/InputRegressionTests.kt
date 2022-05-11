package cambio.tltea.interpreter

import cambio.tltea.interpreter.testutils.TestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InputRegressionTests : TestBase() {
    @Test
    internal fun one() {
        super.interpretFormula("((service1\$instances)>5) -> (F[10]((service2\$instances)==5))")
        assertEquals(1, triggerManager.eventActivationListeners.size)
    }
}