@file:JvmName("TreeDescription")

package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.ValueListener
import cambio.tltea.interpreter.nodes.structure.INode

class TreeDescription (
    val causeASTRoot: INode<Boolean,*>,
    val listeners: List<ValueListener<*>>
) {

    //val causeChangePublisher: StateChangedPublisher<Boolean> = causeASTRoot

    fun activateListeners() {
        listeners.forEach { it.startListening() }
    }

    fun deactivateListeners() {
        listeners.forEach { it.stopListening() }
    }

}