package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.RespondGLBBaseTest

class RespondGLB100Test : RespondGLBBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/RespondGLB" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 10
    }
}