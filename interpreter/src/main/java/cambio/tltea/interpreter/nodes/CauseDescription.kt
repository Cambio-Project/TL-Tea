@file:JvmName("CauseDescription")

package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.CauseNode
import cambio.tltea.interpreter.nodes.cause.EventActivationListener
import cambio.tltea.interpreter.nodes.cause.ValueListener

class CauseDescription(
    val causeASTRoot: CauseNode,
    val listeners: List<ValueListener<*>>
) {

    val causeChangePublisher: StateChangedPublisher<Boolean> = causeASTRoot

    fun activateListeners() {
        listeners.forEach { it.startListening() }
    }

    fun deactivateListeners() {
        listeners.forEach { it.stopListening() }
    }

}