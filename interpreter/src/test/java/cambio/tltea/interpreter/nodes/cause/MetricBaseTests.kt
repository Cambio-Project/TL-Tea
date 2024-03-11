package cambio.tltea.interpreter.nodes.cause

import cambio.tltea.interpreter.BehaviorInterpretationResult
import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.connector.value.IMetricListener
import cambio.tltea.interpreter.connector.value.IMetricRegistrationStrategy
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.nodes.ImplicationNode
import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode
import cambio.tltea.interpreter.testutils.TestBase
import cambio.tltea.parser.core.temporal.TimeInstance
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class MetricBaseTests : TestBase() {
    lateinit var strategy: MetricRegistrationStrategy
    lateinit var brokers: Brokers

    class MetricRegistrationStrategy : IMetricRegistrationStrategy {
        lateinit var a: IMetricListener<Double>
        lateinit var b: IMetricListener<Double>

        override fun register(listener: IMetricListener<*>, descriptor: MetricDescriptor) {
            listener as IMetricListener<Double>
            
            if (descriptor.architectureIdentifier == "service1" && descriptor.metricIdentifier == "a") {
                a = listener
                return
            }
            if (descriptor.architectureIdentifier == "service2" && descriptor.metricIdentifier == "b") {
                b = listener
                return
            }
            fail("Value Listener not set up")
        }
    }

    @BeforeEach
    fun registerStrategy() {
        brokers = Brokers()
        strategy = MetricRegistrationStrategy()
        brokers.metricBroker.listenerFactory.registrationStrategy = strategy
    }

    @Test
    fun updateResultOnValueUpdates() {
        val result: BehaviorInterpretationResult =
            interpretFormula("((service1\$a) < (service2\$b)) -> (event:test)", brokers)
        assertEquals(brokers.metricBroker.listenerFactory.registrationStrategy, strategy)
        assertNotNull(strategy.a)
        assertNotNull(strategy.b)

        val node: ConsequenceNode = result.consequenceDescription.consequenceAST!!
        (node as ImplicationNode)

        strategy.a.update(1.0, TimeInstance(0))
        strategy.b.update(2.0, TimeInstance(0))
        assertTrue(node.causeDescription.causeASTRoot.currentValue)

        strategy.a.update(2.0, TimeInstance(1))
        assertFalse(node.causeDescription.causeASTRoot.currentValue)

        strategy.b.update(1.0, TimeInstance(2))
        assertFalse(node.causeDescription.causeASTRoot.currentValue)

        strategy.a.update(0.5, TimeInstance(3))
        assertTrue(node.causeDescription.causeASTRoot.currentValue)

    }
}