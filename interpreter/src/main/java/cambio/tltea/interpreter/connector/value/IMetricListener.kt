package cambio.tltea.interpreter.connector.value

import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Listens to metrics of the data source. A metric listener is responsible for distributing the changes within TlTea.
 */
interface IMetricListener<T> {
    /**
     * Receives a metric value update and the time of the update.
     */
    fun update(value: T, time: TimeInstance)

    /**
     * Subscribes a listener that should be notified of value updates. A listener can only be subscribed once.
     */
    fun subscribe(subscriber: IMetricSubscriber<T>)

    /**
     * Unsubscribes a listener that should no longer be notified of value updates.
     */
    fun unsubscribe(subscriber: IMetricSubscriber<T>)
}