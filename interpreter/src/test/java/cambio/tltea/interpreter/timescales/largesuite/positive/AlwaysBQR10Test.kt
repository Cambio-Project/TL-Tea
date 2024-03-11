package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.AlwaysBQRBaseTest

class AlwaysBQR10Test : AlwaysBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/AlwaysBQR" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}