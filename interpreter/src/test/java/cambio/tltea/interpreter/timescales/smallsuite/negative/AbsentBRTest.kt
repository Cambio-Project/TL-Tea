package cambio.tltea.interpreter.timescales.smallsuite.negative

import cambio.tltea.interpreter.timescales.AbsentBRBaseTest

class AbsentBRTest : AbsentBRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite-false/AbsentBR.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}