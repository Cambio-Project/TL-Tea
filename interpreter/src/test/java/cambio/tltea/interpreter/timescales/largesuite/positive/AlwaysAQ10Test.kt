package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.AlwaysAQBaseTest

class AlwaysAQ10Test : AlwaysAQBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/AlwaysAQ" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}