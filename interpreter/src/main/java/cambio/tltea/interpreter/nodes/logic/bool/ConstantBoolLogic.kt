package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.EndOfExperimentNodeEvent
import cambio.tltea.interpreter.nodes.events.EndOfRoundNodeEvent
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.parser.core.temporal.TimeInstance

class ConstantBoolLogic(private val constant: Boolean, brokers: Brokers) : AbstractBoolLogic(0, brokers) {
    override fun on(event: InitializeNodeEvent) {
        updateState(constant, event.getTime())
        /*
        satisfactionState.currentSatisfied = constant
        satisfactionState.currentTempSatisfied = constant
        satisfactionState.satisfiable = constant
         */
    }

    override fun on(event: EndOfExperimentNodeEvent) {
        updateCurrentTime(event.getTime())
    }

    override fun on(event: EndOfRoundNodeEvent) {
        updateCurrentTime(event.getTime())
    }

    override fun evaluateBool(at: TimeInstance) {
        // do nothing
    }

    override fun isSatisfiable(): Boolean {
        return this.getLatestState()
    }

    /*
    override fun on(event: StateChangeNodeEvent) {
        // do nothing
    }*/
}