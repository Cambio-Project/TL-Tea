package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.RecurBQRBaseTest

class RecurBQR1000Test : RecurBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/RecurBQR" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 100
    }
}