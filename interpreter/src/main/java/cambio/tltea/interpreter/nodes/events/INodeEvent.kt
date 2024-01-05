package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Events that contain updates for and passed between [nodes][cambio.tltea.interpreter.nodes.structure.INode].
 */
sealed interface INodeEvent {
    fun getTime(): TimeInstance
}