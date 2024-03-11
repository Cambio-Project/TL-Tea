package cambio.tltea.interpreter.nodes.cause

import cambio.tltea.parser.core.temporal.ITemporalValue

/**
 * @author Lion Wagner
 */
class EventActivationListener(eventName: String) : ValueListener<Boolean>(eventName, false) {
    fun isActivated(): Boolean{
        return currentValue!!
    }

    fun setActivated(time: ITemporalValue?) {
        activate(time)
    }

    fun activate(time: ITemporalValue?) {
        changeStateAndNotify(true, time)
    }

    fun reset(time: ITemporalValue?) {
        deactivate(time)
    }

    fun deactivate(time: ITemporalValue?) {
        changeStateAndNotify(false, time)
    }

    override fun clone(): EventActivationListener {
        return EventActivationListener(valueOrEventName)
    }
}