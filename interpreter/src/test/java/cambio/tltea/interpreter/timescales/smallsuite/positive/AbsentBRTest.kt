package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.AbsentBRBaseTest

class AbsentBRTest : AbsentBRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/AbsentBR.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}