@file:JvmName("CauseDescription")

package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.CauseNode
import cambio.tltea.interpreter.nodes.cause.EventActivationListener

class CauseDescription(
    val causeASTRoot: CauseNode,
    private val listeners: List<EventActivationListener>
) {

    val causeChangePublisher: StateChangedPublisher<Boolean> = causeASTRoot

    fun activateListeners() {
        listeners.forEach { it.startListening() }
    }

    fun deactivateListeners() {
        listeners.forEach { it.stopListening() }
    }

}