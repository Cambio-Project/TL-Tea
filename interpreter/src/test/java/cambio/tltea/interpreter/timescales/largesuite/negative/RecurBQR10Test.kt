package cambio.tltea.interpreter.timescales.largesuite.negative

import cambio.tltea.interpreter.timescales.RecurBQRBaseTest

class RecurBQR10Test : RecurBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite-false/RecurBQR" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}