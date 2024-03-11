package cambio.tltea.interpreter.timescales.smallsuite.negative

import cambio.tltea.interpreter.timescales.AlwaysAQBaseTest

class AlwaysAQTest : AlwaysAQBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/smallsuite-false/AlwaysAQ.csv"
    }

    override fun getExpectedResult(): Boolean {
        return false
    }

    override fun getFactor(): Int {
        return 1
    }
}