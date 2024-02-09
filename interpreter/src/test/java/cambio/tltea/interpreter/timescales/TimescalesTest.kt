package cambio.tltea.interpreter.timescales

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.simulator.SimulationTest
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.FileReader

abstract class TimescalesTest : SimulationTest() {
    private val monitoringData: MutableList<List<String>> = ArrayList()
    private val metrics: MutableList<MetricDescriptor> = ArrayList()

    abstract fun getMonitoringDataFileName(): String
    abstract fun getMTLFormula(): String
    abstract fun getExpectedResult(): Boolean
    abstract fun getFactor(): Int

    fun applyFactor(number: Int): Int {
        return number * getFactor()
    }

    @Test
    fun timescalesTest() {
        readCSV()
        extractMetricDescriptors()
        load(getMTLFormula())
        simulateScenario()
        endSimulation()
        checkResult()
    }

    private fun readCSV() {
        monitoringData.clear()
        val br = BufferedReader(FileReader(getMonitoringDataFileName()))
        var line: String? = br.readLine()
        while (line != null) {
            val values = line.split(",")
            monitoringData.add(values)
            line = br.readLine()
        }
    }

    private fun extractMetricDescriptors() {
        val metricsWithoutTime = monitoringData[0].subList(1, monitoringData[0].size)
        for (metric in metricsWithoutTime) {
            metrics.add(MetricDescriptor("", metric, true))
        }
        monitoringData.removeAt(0)
    }

    private fun simulateScenario() {
        for (currentMonitoringData in monitoringData.withIndex()) {
            val time = TimeInstance(currentMonitoringData.index)
            for (metric in metrics.withIndex()) {
                simulator.forceHandle(time, metric.value, currentMonitoringData.value[metric.index + 1].toBoolean())
            }
            simulator.forceEndRound()
        }
    }

    private fun endSimulation() {
        val time = TimeInstance(monitoringData.size - 1)
        simulator.forceEndExperiment(time)
    }

    private fun checkResult() {
        assertStateEquals(getExpectedResult(), 0.0)
    }
}