package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.RespondBQRBaseTest

class RespondBQRTest : RespondBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/RespondBQR.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}