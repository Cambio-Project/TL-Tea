package cambio.tltea.interpreter.nodes.logic

import cambio.tltea.interpreter.nodes.events.ILogicalNodeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance

interface ILogic {
    fun initialize(node: INode<Boolean, Boolean>)
    fun handle(event: ILogicalNodeEvent)
    fun isSatisfiable(): Boolean
    fun getLatestState(): Boolean
    fun getCurrentTime(): TimeInstance
    fun getState(time: TimeInstance): Boolean
    fun getStateChanges(from: TimeInstance, to: TimeInstance): List<TimeEvent>
    fun getStateChange(at: TimeInstance): TimeEvent?

}