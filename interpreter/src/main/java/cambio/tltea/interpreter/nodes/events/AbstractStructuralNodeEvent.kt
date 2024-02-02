package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

sealed class AbstractStructuralNodeEvent(private val updateTime: TimeInstance) : IStructuralNodeEvent {
    override fun getTime(): TimeInstance {
        return updateTime
    }
}