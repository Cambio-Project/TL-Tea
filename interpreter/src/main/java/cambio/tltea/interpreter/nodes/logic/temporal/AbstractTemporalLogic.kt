package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.AbstractLogic
import cambio.tltea.interpreter.nodes.logic.util.TimeEventLog
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

sealed class AbstractTemporalLogic(
    protected val temporalInterval: TemporalInterval,
    brokers: Brokers
) : AbstractLogic(brokers), ITemporalLogic {
    protected val duration: Double = temporalInterval.endAsDouble - temporalInterval.startAsDouble
    protected val durationInstance = TimeInstance(duration)

    override fun initialize(node: INode<Boolean, Boolean>) {
        this.node = node
        this.satisfactionState = TimeEventLog()
    }

    override fun on(event: ActivationChangeNodeEvent) {
        // do nothing
    }

    final override fun on(event: EndOfRoundNodeEvent) {
        // do nothing
    }

    override fun on(event: EndOfExperimentNodeEvent) {
        //handleEndOfRound()
        on(EndOfRoundNodeEvent(event.getTime()))
        updateCurrentTime(event.getTime())
    //publishUpdates(event.getTime())
    }

    final override fun forceEvaluate(at: TimeInstance) {
        // do nothing
    }
}