package cambio.tltea.interpreter.nodes.logic.relational

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.AbstractLogic
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.parser.core.temporal.TimeInstance

sealed class AbstractRelationalLogic<T : Comparable<T>>(
    brokers: Brokers
) : AbstractLogic(brokers), IRelationalNodeLogic<T> {
    //protected lateinit var satisfactionState: SatisfactionState
    private var receivedUpdate = false
    protected lateinit var left: T
    protected lateinit var right: T

    /*
    override fun initialize(node: INode<Boolean, Boolean>) {
        super.initialize(node)
        this.lastTempValue = satisfactionState.currentTempSatisfied
    }
    */

    override fun setLeftValue(value: T): IRelationalNodeLogic<T> {
        this.left = value
        return this
    }

    override fun setRightValue(value: T): IRelationalNodeLogic<T> {
        this.right = value
        return this
    }

    final override fun isSatisfiable(): Boolean {
        return true
    }

    /*
    override fun getLatestState(): Boolean {
        return satisfactionState.currentTempSatisfied
    }
    */

    override fun on(event: ActivationChangeNodeEvent) {
        // do nothing
    }

    override fun on(event: EndOfExperimentNodeEvent) {
        onEndOfRound(event.getTime())
    }

    override fun on(event: EndOfRoundNodeEvent) {
        onEndOfRound(event.getTime())
    }

    private fun onEndOfRound(time: TimeInstance) {
        //satisfactionState.currentSatisfied = satisfactionState.currentTempSatisfied
        if (receivedUpdate) {
            //updateLastValue()
            updateCurrentValue(time)
            receivedUpdate = false
        }
        updateCurrentTime(time)
    }

    override fun on(event: InitializeNodeEvent) {
        // do nothing
    }

    /*
    override fun on(event: StateChangeNodeEvent) {
        receivedUpdate = true
        //updateCurrentValue()
        //updateLastValue()
    }
     */

    override fun forceEvaluate(at: TimeInstance) {
        receivedUpdate = true
    }

    override fun evaluate(changePoint: TimeEvent) {
        receivedUpdate = true
    }

    abstract fun evaluate(): Boolean

    private fun updateCurrentValue(time:TimeInstance) {
        //satisfactionState.currentTempSatisfied = evaluate()
        val state: Boolean = evaluate()
        satisfactionState.add(TimeEvent(time, state))
        /* TODO: remove
        if (state) {
            satisfactionState.addStartEvent(time)

        } else {
            satisfactionState.addEndEvent(time)
        }*/
    }

    /*
    private fun updateLastValue() {
        lastTempValue = satisfactionState.currentTempSatisfied
    }
    */
}
