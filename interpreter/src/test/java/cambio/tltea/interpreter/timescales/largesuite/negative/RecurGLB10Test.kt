package cambio.tltea.interpreter.timescales.largesuite.negative

import cambio.tltea.interpreter.timescales.RecurGLBBaseTest

class RecurGLB10Test : RecurGLBBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite-false/RecurGLB" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}