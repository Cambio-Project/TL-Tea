package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.AbstractLogic
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.parser.core.temporal.TimeInstance

sealed class AbstractBoolLogic(
    private val expectedChildCount: Int,
    brokers: Brokers
) : AbstractLogic(brokers), IBoolLogic {

    override fun on(event: ActivationChangeNodeEvent) {
        // do nothing
    }

    override fun on(event: EndOfExperimentNodeEvent) {
        onEndOfRound()
        on(EndOfRoundNodeEvent(event.getTime()))
        updateCurrentTime(event.getTime())
    }

    override fun on(event: EndOfRoundNodeEvent) {
        onEndOfRound()
    }

    private fun onEndOfRound() {
        // do nothing
    }

    protected fun updateState(state: Boolean, time: TimeInstance) {
        satisfactionState.add(TimeEvent(time, state))
    }

    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size == expectedChildCount)
        evaluateBool(event.getTime())
    }

    final override fun forceEvaluate(at: TimeInstance) {
        evaluateBool(at)
        updateCurrentTime(at)
    }

    final override fun evaluate(changePoint: TimeEvent) {
        forceEvaluate(changePoint.time)
    }

    abstract fun evaluateBool(at: TimeInstance)

}
