package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.AbsentAQBaseTest

class AbsentAQ10Test : AbsentAQBaseTest() {

    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/AbsentAQ" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}