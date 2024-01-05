package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.RecurGLBBaseTest

class RecurGLBTest : RecurGLBBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/RecurGLB.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}