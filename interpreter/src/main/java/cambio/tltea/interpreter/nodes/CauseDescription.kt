@file:JvmName("CauseDescription")

package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.CauseNode
import cambio.tltea.interpreter.nodes.cause.EventActivationListener

class CauseDescription(
    val causeASTRoot: CauseNode,
    private val listeners: List<EventActivationListener>,
    private val triggerNotifier: TriggerNotifier
) {

    val causeChangePublisher: StateChangedPublisher<Boolean> = causeASTRoot

    fun activateListeners() {
        triggerNotifier.activateListeners(listeners)
    }

}