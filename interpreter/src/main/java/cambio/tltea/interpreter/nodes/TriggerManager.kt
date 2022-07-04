package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.ValueListener
import cambio.tltea.interpreter.nodes.consequence.activation.ActivationData
import java.util.function.Consumer

/**
 * @author Lion Wagner
 */
class TriggerManager : ISubscribableTriggerNotifier {

    internal val eventActivationListeners: MutableCollection<ValueListener<*>> = HashSet()

    private val anySubscribers: MutableCollection<Consumer<ActivationData<*>>> = HashSet()

    private val filteredSubscribers: HashMap<Class<*>, MutableSet<Consumer<ActivationData<*>>>> =
        HashMap()

    override fun subscribeEventListener(listener: Consumer<ActivationData<*>>) {
        anySubscribers.add(listener)
    }

    override fun <T : ActivationData<*>> subscribeEventListenerWithFilter(
        listener: Consumer<T>,
        filter: Class<T>
    ) {
        if (!filteredSubscribers.containsKey(filter)) {
            filteredSubscribers[filter] = HashSet()
        }
        filteredSubscribers[filter]!!.add(listener as Consumer<ActivationData<*>>)
    }

    override fun unsubscribe(listener: Consumer<ActivationData<*>>) {
        anySubscribers.remove(listener)
        filteredSubscribers.values.forEach { it.remove(listener) }
    }

    internal fun trigger(activationData: ActivationData<*>) {
        anySubscribers.forEach { it.accept(activationData) }
        filteredSubscribers[activationData.javaClass]?.forEach { it.accept(activationData) }
    }

    fun getEventActivationListeners(): List<ValueListener<*>> {
        return listOf(*eventActivationListeners.toTypedArray())
    }

}