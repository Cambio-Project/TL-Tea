package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.ActivationData
import java.util.function.Consumer

/**
 * @author Lion Wagner
 */
class TriggerNotifier : ISubscribableTriggerNotifier {

    private val anySubscribers: MutableCollection<Consumer<ActivationData<*>>> = HashSet()

    private val filteredSubscribers: HashMap<Class<ActivationData<*>>, MutableSet<Consumer<ActivationData<*>>>> =
        HashMap()

    override fun subscribeEventListener(listener: Consumer<ActivationData<*>>) {
        anySubscribers.add(listener)
    }

    override fun subscribeEventListenerWithFilter(listener: Consumer<ActivationData<*>>, filter: Class<ActivationData<*>>) {
        if (!filteredSubscribers.containsKey(filter)) {
            filteredSubscribers[filter] = HashSet()
        }
        filteredSubscribers[filter]!!.add(listener)
    }

    override fun unsubscribe(listener: Consumer<ActivationData<*>>) {
        anySubscribers.remove(listener)
        filteredSubscribers.values.forEach { it.remove(listener) }
    }

    internal fun trigger(activationData: ActivationData<*>) {
        anySubscribers.forEach { it.accept(activationData) }
        filteredSubscribers[activationData.javaClass]?.forEach { it.accept(activationData) }
    }
}