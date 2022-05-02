package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.EventActivationListener
import cambio.tltea.interpreter.nodes.consequence.ActivationData
import java.util.function.Consumer

/**
 * @author Lion Wagner
 */
class TriggerNotifier {

    private val anySubscribers: MutableCollection<Consumer<ActivationData<*>>> = HashSet()

    private val filteredSubscribers: HashMap<Class<ActivationData<*>>, MutableSet<Consumer<ActivationData<*>>>> =
        HashMap()

    fun subscribeEventListener(listener: Consumer<ActivationData<*>>) {
        anySubscribers.add(listener)
    }

    fun subscribeEventListenerWithFilter(listener: Consumer<ActivationData<*>>, filter: Class<ActivationData<*>>) {
        if (!filteredSubscribers.containsKey(filter)) {
            filteredSubscribers[filter] = HashSet()
        }
        filteredSubscribers[filter]!!.add(listener)
    }

    fun unsubscribe(listener: Consumer<ActivationData<*>>) {
        anySubscribers.remove(listener)
        filteredSubscribers.values.forEach { it.remove(listener) }
    }

    internal fun trigger(activationData: ActivationData<*>) {
        anySubscribers.forEach { it.accept(activationData) }
        filteredSubscribers[activationData.javaClass]?.forEach { it.accept(activationData) }
    }

    private val eventActivationListeners: MutableCollection<Consumer<EventActivationListener>> = HashSet()
    private val listenerActivationQueue: MutableList<EventActivationListener> = ArrayList()


    /**
     * Subscribes a listener that will be notified when a new event listener is activated.
     */
    fun subscribeListenerActivationListener(listener: Consumer<EventActivationListener>) {
        eventActivationListeners.add(listener)
        activateListeners(listenerActivationQueue)
        listenerActivationQueue.clear()
    }

    fun unsubscribeListenerActivationListener(listener: Consumer<EventActivationListener>) {
        eventActivationListeners.remove(listener)
    }

    internal fun activateListeners(listeners: Collection<EventActivationListener>) {
        if (eventActivationListeners.isEmpty()) {
            listenerActivationQueue.addAll(listeners)
        }
        listeners.forEach { eventListener ->
            eventActivationListeners.forEach { it.accept(eventListener) }
        }
    }
}