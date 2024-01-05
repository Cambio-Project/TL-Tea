package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.AbsentAQBaseTest

class AbsentAQTest : AbsentAQBaseTest() {

    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/AbsentAQ.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}