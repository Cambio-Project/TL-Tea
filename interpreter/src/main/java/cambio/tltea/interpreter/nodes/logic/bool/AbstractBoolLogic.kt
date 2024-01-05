package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.AbstractLogic
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.TreeSet

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
    }

    override fun on(event: EndOfRoundNodeEvent) {
        onEndOfRound()
    }

    private fun onEndOfRound() {
        //satisfactionState.currentSatisfied = satisfactionState.currentTempSatisfied
    }

    protected fun updateState(state: Boolean, time: TimeInstance) {
        if (state) {
            satisfactionState.addStartEvent(time)
        } else {
            satisfactionState.addEndEvent(time)
        }
    }

    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size == expectedChildCount)
        evaluateBool(event.getTime())
    }

    final override fun evaluate(at: TimeInstance) {
        evaluateBool(at)
        updateCurrentTime(at)
    }

    abstract fun evaluateBool(at: TimeInstance)

}
