package cambio.tltea.interpreter.timescales.smallsuite.positive

import cambio.tltea.interpreter.timescales.AlwaysAQBaseTest

class AlwaysAQTest : AlwaysAQBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite/AlwaysAQ.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}