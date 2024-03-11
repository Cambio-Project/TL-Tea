package cambio.tltea.interpreter.timescales.largesuite.negative

import cambio.tltea.interpreter.timescales.RespondBQRBaseTest

class RespondBQR10Test : RespondBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite-false/RespondBQR" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}