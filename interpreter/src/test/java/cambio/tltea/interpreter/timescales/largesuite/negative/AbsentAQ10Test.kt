package cambio.tltea.interpreter.timescales.largesuite.negative

import cambio.tltea.interpreter.timescales.AbsentAQBaseTest

class AbsentAQ10Test : AbsentAQBaseTest() {

    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite-false/AbsentAQ" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}