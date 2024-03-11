package cambio.tltea.interpreter.timescales.smallsuite.negative

import cambio.tltea.interpreter.timescales.RespondBQRBaseTest

class RespondBQRTest : RespondBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite-false/RespondBQR.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}