@file:JvmName("CauseDescription")

package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.CauseNode
import cambio.tltea.interpreter.nodes.cause.EventActivationListener

class CauseDescription(
    val causeASTRoot: CauseNode,
    private val listeners: List<EventActivationListener>
) {

    val causeChangeListener: StateChangedPublisher<Boolean> = causeASTRoot

    fun getListeners(): List<EventActivationListener> {
        return listeners.toList()
    }

}