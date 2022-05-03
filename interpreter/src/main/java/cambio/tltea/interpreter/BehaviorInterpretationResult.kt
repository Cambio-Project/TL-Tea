package cambio.tltea.interpreter

import cambio.tltea.interpreter.nodes.ConsequenceDescription
import cambio.tltea.interpreter.nodes.ISubscribableTriggerNotifier
import cambio.tltea.interpreter.nodes.consequence.ActivationData
import cambio.tltea.parser.core.ASTNode
import java.util.function.Consumer

/**
 * @author Lion Wagner
 */
class BehaviorInterpretationResult internal constructor(val modifiedAST: ASTNode, val consequenceDescription: ConsequenceDescription) :
    ISubscribableTriggerNotifier {


    // ----- delegating the subscriptions to the trigger manager-----
    override fun subscribeEventListener(listener: Consumer<ActivationData<*>>) {
        consequenceDescription.triggerNotifier.subscribeEventListener(listener)
    }

    override fun subscribeEventListenerWithFilter(
        listener: Consumer<ActivationData<*>>,
        filter: Class<ActivationData<*>>
    ) {
        consequenceDescription.triggerNotifier.subscribeEventListenerWithFilter(listener, filter)
    }

    override fun unsubscribe(listener: Consumer<ActivationData<*>>) {
        consequenceDescription.triggerNotifier.unsubscribe(listener)
    }
    //---------------------------------------------------------------

}