package cambio.tltea.interpreter.testutils

import cambio.tltea.interpreter.BehaviorInterpretationResult
import cambio.tltea.interpreter.Interpreter
import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.interpreter.nodes.cause.EventActivationListener
import cambio.tltea.interpreter.nodes.consequence.EventActivationData
import cambio.tltea.interpreter.nodes.consequence.EventPreventionData
import cambio.tltea.interpreter.nodes.consequence.ValueEventActivationData
import cambio.tltea.parser.core.temporal.ITemporalValue
import cambio.tltea.parser.mtl.generated.MTLParser
import org.junit.jupiter.api.Assertions.assertEquals

open class TestBase {

    protected lateinit var currentInterpretationResult: BehaviorInterpretationResult
    protected lateinit var triggerManager: TriggerManager
    protected lateinit var generalActivationLog: MutableList<String>
    protected lateinit var eventActivationLog: MutableList<String>
    protected lateinit var eventPreventionLog: MutableList<String>
    protected lateinit var valueEventActivationLog: MutableList<String>


    protected fun interpretFormula(formula: String): BehaviorInterpretationResult {
        val interpretAsBehavior = Interpreter.interpretAsBehavior(MTLParser.parse(formula))

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
        return triggerManager.eventActivationListeners.find { eventActivationListener -> eventActivationListener.eventName == eventName }
    }

    protected fun assertLogSizes(eventActivation: Int, eventPrevention: Int, valueEvents: Int) {
        assertEquals(eventActivation, eventActivationLog.size)
        assertEquals(eventPrevention, eventPreventionLog.size)
        assertEquals(valueEvents, valueEventActivationLog.size)
        assertEquals(eventActivation + eventPrevention + valueEvents, generalActivationLog.size)
    }

    protected fun activateEvent(eventName: String, `when`: ITemporalValue) {
        getEventListeners(eventName)?.activate(`when`)
    }

    protected fun deactivateEvent(eventName: String, `when`: ITemporalValue) {
        getEventListeners(eventName)?.deactivate(`when`)
    }
}