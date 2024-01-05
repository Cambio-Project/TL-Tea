package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.RecurGLBBaseTest

class RecurGLB100Test : RecurGLBBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/RecurGLB" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 10
    }
}