package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.AbsentBQRBaseTest

class AbsentBQRTest : AbsentBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/AbsentBQR.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}