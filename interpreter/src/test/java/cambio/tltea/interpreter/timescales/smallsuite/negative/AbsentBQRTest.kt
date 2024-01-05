package cambio.tltea.interpreter.timescales.smallsuite.negative

import cambio.tltea.interpreter.timescales.AbsentBQRBaseTest

class AbsentBQRTest : AbsentBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite-false/AbsentBQR.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}