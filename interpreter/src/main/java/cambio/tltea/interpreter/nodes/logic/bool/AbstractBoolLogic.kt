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
    //protected lateinit var satisfactionState: SatisfactionState

    /*
    override fun initialize(node: INode<Boolean, Boolean>) {
        this.satisfactionState = SatisfactionState(node)
    }


    override fun isSatisfiable(): Boolean {
        return satisfactionState.satisfiable
    }*/

    /*
    override fun getLatestState(): Boolean {
        return satisfactionState.currentTempSatisfied
    }
    */

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
        //satisfactionState.currentSatisfied = satisfactionState.currentTempSatisfied
    }

    protected fun updateState(state: Boolean, time: TimeInstance) {
        satisfactionState.add(TimeEvent(time, state))
        /* TODO: remove
        if (state) {
            satisfactionState.addStartEvent(time)
        } else {
            satisfactionState.addEndEvent(time)
        }*/
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

    final override fun evaluate(stateChange: TimeEvent) {
        forceEvaluate(stateChange.time)
    }

    abstract fun evaluateBool(at: TimeInstance)

}
