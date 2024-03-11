package cambio.tltea.interpreter.nodes.cause

import cambio.tltea.interpreter.connector.value.IMetricListener
import cambio.tltea.interpreter.connector.value.IMetricSubscriber
import cambio.tltea.interpreter.nodes.StateChangeEvent
import cambio.tltea.interpreter.nodes.StateChangeListener
import cambio.tltea.interpreter.nodes.StateChangedPublisher
import cambio.tltea.parser.core.temporal.ITemporalValue
import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.function.Consumer

open class ValueListener<T : Comparable<T>>(val valueOrEventName: String, @JvmField var currentValue:T) : StateChangedPublisher<T>(), IMetricSubscriber<T> {
    private var isListening = false
    var metricListener: IMetricListener<T>? = null

    protected fun changeStateAndNotify(newValue: T, time: ITemporalValue?) {
        if (!isListening) {
            return
        }
        currentValue = newValue
        subscribers.forEach(Consumer { listener: StateChangeListener<T?> ->
            listener.onEvent(
                StateChangeEvent(
                    this,
                    newValue,
                    currentValue,
                    time
                )
            )
        })
    }

    fun updateValue(value: T, time: ITemporalValue?) {
        changeStateAndNotify(value, time)
    }

    fun startListening() {
        isListening = true
    }

    fun stopListening() {
        isListening = false
    }

    open fun clone(): ValueListener<T> {
        val valueListener = ValueListener<T>(valueOrEventName, currentValue)
        this.metricListener?.subscribe(valueListener)
        valueListener.metricListener = this.metricListener
        return valueListener
    }

    override fun update(value: T, time: TimeInstance) {
        updateValue(value, time)
    }
}