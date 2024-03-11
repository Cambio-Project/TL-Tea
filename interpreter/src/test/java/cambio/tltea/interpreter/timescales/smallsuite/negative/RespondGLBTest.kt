package cambio.tltea.interpreter.timescales.smallsuite.negative

import cambio.tltea.interpreter.timescales.RespondGLBBaseTest

class RespondGLBTest : RespondGLBBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite-false/RespondGLB.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}