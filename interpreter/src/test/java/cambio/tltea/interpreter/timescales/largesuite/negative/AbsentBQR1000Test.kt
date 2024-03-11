package cambio.tltea.interpreter.timescales.largesuite.negative

import cambio.tltea.interpreter.timescales.AbsentBQRBaseTest

class AbsentBQR1000Test : AbsentBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite-false/AbsentBQR" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 100
    }
}