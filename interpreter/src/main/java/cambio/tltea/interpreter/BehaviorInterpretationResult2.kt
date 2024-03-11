package cambio.tltea.interpreter

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.ISubscribableTriggerNotifier
import cambio.tltea.interpreter.nodes.TreeDescription
import cambio.tltea.interpreter.nodes.consequence.activation.ActivationData
import cambio.tltea.interpreter.nodes.events.TriggerInitializeNodeEvent
import cambio.tltea.parser.core.ASTNode
import java.util.function.Consumer

/**
 * @author Lion Wagner
 */
class BehaviorInterpretationResult2 internal constructor(
    val modifiedAST: ASTNode,
    val treeDescription: TreeDescription,
    val brokers: Brokers,
    //val triggerManager: TriggerManager = treeDescription.triggerManager
) :
    ISubscribableTriggerNotifier {


    fun activateProcessing() {
        treeDescription.causeASTRoot.handle(TriggerInitializeNodeEvent())
        // treeDescription.activateConsequence()
    }

    // ----- delegating the subscriptions to the trigger manager-----
    override fun subscribeEventListener(listener: Consumer<ActivationData<*>>) {
        // treeDescription.triggerManager.subscribeEventListener(listener)
    }

    override fun <T : ActivationData<*>> subscribeEventListenerWithFilter(
        listener: Consumer<T>,
        filter: Class<T>
    ) {
        // treeDescription.triggerManager.subscribeEventListenerWithFilter(listener, filter)
    }

    override fun unsubscribe(listener: Consumer<ActivationData<*>>) {
        // treeDescription.triggerManager.unsubscribe(listener)
    }
    //---------------------------------------------------------------

}