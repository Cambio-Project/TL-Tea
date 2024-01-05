package cambio.tltea.interpreter.nodes.events

import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance

class StateChangeNodeEvent(
    val publishingNode: INode<*, *>,
    //val newValue: Boolean,
    //val oldValue: Boolean,
    val delayed: Boolean = false,
    lastUpdateTime: TimeInstance
) : AbstractLogicalNodeEvent(lastUpdateTime) {
    /*
    fun valueChanged(): Boolean {
        return newValue != oldValue
    }
    */
}
