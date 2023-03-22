package cambio.tltea.interpreter

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.ConsequenceDescription
import cambio.tltea.interpreter.nodes.ISubscribableTriggerNotifier
import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.interpreter.nodes.consequence.activation.ActivationData
import cambio.tltea.parser.core.ASTNode
import java.util.function.Consumer

/**
 * @author Lion Wagner
 */
class BehaviorInterpretationResult internal constructor(
    val modifiedAST: ASTNode,
    val consequenceDescription: ConsequenceDescription,
    val brokers: Brokers,
    val triggerManager: TriggerManager = consequenceDescription.triggerManager
) :
    ISubscribableTriggerNotifier {


    fun activateProcessing() {
        consequenceDescription.activateConsequence()
    }

    // ----- delegating the subscriptions to the trigger manager-----
    override fun subscribeEventListener(listener: Consumer<ActivationData<*>>) {
        consequenceDescription.triggerManager.subscribeEventListener(listener)
    }

    override fun <T : ActivationData<*>> subscribeEventListenerWithFilter(
        listener: Consumer<T>,
        filter: Class<T>
    ) {
        consequenceDescription.triggerManager.subscribeEventListenerWithFilter(listener, filter)
    }

    override fun unsubscribe(listener: Consumer<ActivationData<*>>) {
        consequenceDescription.triggerManager.unsubscribe(listener)
    }
    //---------------------------------------------------------------

}