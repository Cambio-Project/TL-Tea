package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.activation.ActivationData
import java.util.function.Consumer

interface ISubscribableTriggerNotifier {
    fun subscribeEventListener(listener: Consumer<ActivationData<*>>)
    fun <T : ActivationData<*>> subscribeEventListenerWithFilter(
        listener: Consumer<T>,
        filter: Class<T>
    )

    fun unsubscribe(listener: Consumer<ActivationData<*>>)
}