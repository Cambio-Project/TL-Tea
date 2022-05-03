package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.ActivationData
import java.util.function.Consumer

interface ISubscribableTriggerNotifier {
    fun subscribeEventListener(listener: Consumer<ActivationData<*>>)
    fun subscribeEventListenerWithFilter(listener: Consumer<ActivationData<*>>, filter: Class<ActivationData<*>>)
    fun unsubscribe(listener: Consumer<ActivationData<*>>)
}