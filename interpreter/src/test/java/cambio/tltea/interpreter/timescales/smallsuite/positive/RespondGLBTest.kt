package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.RespondGLBBaseTest

class RespondGLBTest : RespondGLBBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/RespondGLB.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}