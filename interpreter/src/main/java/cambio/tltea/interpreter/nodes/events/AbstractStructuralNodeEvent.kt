package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

sealed class AbstractStructuralNodeEvent(private val lastUpdateTime: TimeInstance) : IStructuralNodeEvent {
    override fun getTime(): TimeInstance {
        return lastUpdateTime
    }
}