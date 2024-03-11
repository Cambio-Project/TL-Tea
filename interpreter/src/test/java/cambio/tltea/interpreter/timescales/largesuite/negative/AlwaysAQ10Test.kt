package cambio.tltea.interpreter.timescales.largesuite.negative

import cambio.tltea.interpreter.timescales.AlwaysAQBaseTest

class AlwaysAQ10Test : AlwaysAQBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite-false/AlwaysAQ" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}