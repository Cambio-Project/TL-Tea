package cambio.tltea.interpreter.timescales.largesuite.positive

import cambio.tltea.interpreter.timescales.AlwaysBRBaseTest

class AlwaysBR10Test : AlwaysBRBaseTest() {
    override fun getMonitoringDataFileName(): String {
        return "resources/timescales/largesuite/AlwaysBR" + getFactor() + "0.csv"
    }

    override fun getExpectedResult(): Boolean {
        return true
    }

    override fun getFactor(): Int {
        return 1
    }
}