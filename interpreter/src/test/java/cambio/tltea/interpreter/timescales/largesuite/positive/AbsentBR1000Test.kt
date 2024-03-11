package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.AbsentBRBaseTest

class AbsentBR1000Test : AbsentBRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/AbsentBR" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 100
    }
}