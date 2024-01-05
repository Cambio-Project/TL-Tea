package cambio.tltea.interpreter.connector.time

import cambio.tltea.interpreter.nodes.events.EndOfRoundNodeEvent
import cambio.tltea.interpreter.nodes.events.TriggerEndOfExperimentNodeEvent
import cambio.tltea.interpreter.nodes.events.TriggerEndOfRoundNodeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance

class TimeManager() {
    private lateinit var topASTNode: INode<*, *>
    private var latestUpdate = TimeInstance(0)

    fun initialize(topASTNode: INode<*, *>) {
        this.topASTNode = topASTNode
    }

    fun setLatestUpdateTime(time: TimeInstance) {
        latestUpdate = time
    }

    fun triggerTimeInstanceEnded() {
        topASTNode.handle(TriggerEndOfRoundNodeEvent(latestUpdate))
    }

    fun triggerExperimentEnded() {
        topASTNode.handle(TriggerEndOfExperimentNodeEvent(latestUpdate))
    }

    fun triggerExperimentEnded(time: TimeInstance) {
        topASTNode.handle(TriggerEndOfExperimentNodeEvent(time))
    }
}