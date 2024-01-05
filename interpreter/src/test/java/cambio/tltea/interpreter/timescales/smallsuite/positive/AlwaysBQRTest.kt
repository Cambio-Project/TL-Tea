package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.AlwaysBQRBaseTest

class AlwaysBQRTest : AlwaysBQRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/AlwaysBQR.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}