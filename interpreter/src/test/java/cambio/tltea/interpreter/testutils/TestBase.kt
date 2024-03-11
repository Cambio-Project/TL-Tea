package cambio.tltea.interpreter.testutils

import cambio.tltea.interpreter.BehaviorInterpretationResult
import cambio.tltea.interpreter.Interpreter
import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.connector.value.IMetricListener
import cambio.tltea.interpreter.connector.value.IMetricRegistrationStrategy
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.interpreter.nodes.cause.EventActivationListener
import cambio.tltea.interpreter.nodes.consequence.activation.EventActivationData
import cambio.tltea.interpreter.nodes.consequence.activation.EventPreventionData
import cambio.tltea.interpreter.nodes.consequence.activation.ValueEventActivationData
import cambio.tltea.parser.core.temporal.ITemporalValue
import cambio.tltea.parser.core.temporal.TimeInstance
import cambio.tltea.parser.mtl.generated.MTLParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach

open class TestBase {

    protected lateinit var currentInterpretationResult: BehaviorInterpretationResult
    protected lateinit var triggerManager: TriggerManager
    protected lateinit var generalActivationLog: MutableList<String>
    protected lateinit var eventActivationLog: MutableList<String>
    protected lateinit var eventPreventionLog: MutableList<String>
    protected lateinit var valueEventActivationLog: MutableList<String>

    protected fun interpretFormula(formula: String): BehaviorInterpretationResult {
        return interpretFormula(formula, Brokers())
    }

    protected fun interpretFormula(formula: String, brokers: Brokers): BehaviorInterpretationResult {
        val interpretAsBehavior = Interpreter.interpretAsBehavior(MTLParser.parse(formula), brokers)

        currentInterpretationResult = interpretAsBehavior
        triggerManager = interpretAsBehavior.triggerManager
        generalActivationLog = mutableListOf()
        eventActivationLog = mutableListOf()
        eventPreventionLog = mutableListOf()
        valueEventActivationLog = mutableListOf()

        triggerManager.subscribeEventListener { t -> generalActivationLog.add(t.toString()) }
        triggerManager.subscribeEventListenerWithFilter(
            { t -> eventActivationLog.add(t.toString()) },
            EventActivationData::class.java
        )
        triggerManager.subscribeEventListenerWithFilter(
            { t -> eventPreventionLog.add(t.toString()) },
            EventPreventionData::class.java
        )
        triggerManager.subscribeEventListenerWithFilter(
            { t -> valueEventActivationLog.add(t.toString()) },
            ValueEventActivationData::class.java
        )

        interpretAsBehavior.activateProcessing()
        return interpretAsBehavior
    }

    protected fun getEventListeners(eventName: String): EventActivationListener? {
        return triggerManager.eventActivationListeners
            .find { eventActivationListener -> eventActivationListener.valueOrEventName == eventName } as EventActivationListener?
    }

    protected fun assertLogSizes(eventActivation: Int, eventPrevention: Int, valueEvents: Int) {
        assertEquals(eventActivation, eventActivationLog.size)
        assertEquals(eventPrevention, eventPreventionLog.size)
        assertEquals(valueEvents, valueEventActivationLog.size)
        assertEquals(eventActivation + eventPrevention + valueEvents, generalActivationLog.size)
    }

    protected fun activateEvent(eventName: String, `when`: ITemporalValue = TimeInstance(0)) {
        getEventListeners(eventName)?.activate(`when`)
    }

    protected fun deactivateEvent(eventName: String, `when`: ITemporalValue) {
        getEventListeners(eventName)?.deactivate(`when`)
    }
}