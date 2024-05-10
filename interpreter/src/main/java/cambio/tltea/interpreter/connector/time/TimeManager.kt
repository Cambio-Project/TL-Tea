package cambio.tltea.interpreter.connector.time

import cambio.tltea.interpreter.nodes.events.TriggerEndOfExperimentNodeEvent
import cambio.tltea.interpreter.nodes.events.TriggerEndOfRoundNodeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance

class TimeManager() {
    private var topASTNodes = mutableListOf<INode<*, *>>()
    private var latestUpdate = TimeInstance(0)

    fun initialize(topASTNode: INode<*, *>) {
        this.topASTNodes.add(topASTNode)
    }

    fun setLatestUpdateTime(time: TimeInstance) {
        latestUpdate = time
    }

    fun triggerTimeInstanceEnded() {
        for (topASTNode in topASTNodes) {
            topASTNode.handle(TriggerEndOfRoundNodeEvent(latestUpdate))
        }
    }

    fun triggerExperimentEnded() {
        for (topASTNode in topASTNodes) {
            topASTNode.handle(TriggerEndOfExperimentNodeEvent(latestUpdate))
        }
    }

    fun triggerExperimentEnded(time: TimeInstance) {
        for (topASTNode in topASTNodes) {
            topASTNode.handle(TriggerEndOfExperimentNodeEvent(time))
        }
    }
}