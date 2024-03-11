package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

sealed class AbstractLogicalNodeEvent(private val updateTime: TimeInstance) : ILogicalNodeEvent {
    override fun getTime(): TimeInstance {
        return updateTime
    }
}