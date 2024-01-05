package cambio.tltea.interpreter.nodes.logic

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.temporal.TimeEventLog
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.*

abstract class AbstractLogic(
    protected val brokers: Brokers
) : ILogic {
    protected lateinit var satisfactionState: TimeEventLog
    protected lateinit var node: INode<Boolean, Boolean>
    private var lastUpdateReceived: TimeInstance = TimeInstance(0)
    private var lastUpdateSent = TimeInstance(0)
    private var currentTime = TimeInstance(-1)
    private var handledChildrenUntil = TimeInstance(-1)

    override fun initialize(node: INode<Boolean, Boolean>) {
        this.node = node
        this.satisfactionState = TimeEventLog()
    }

    final override fun handle(event: ILogicalNodeEvent) {
        updateTimes(event.getTime())
        when (event) {
            is ActivationChangeNodeEvent -> on(event)
            is EndOfExperimentNodeEvent -> on(event)
            is EndOfRoundNodeEvent -> on(event)
            is InitializeNodeEvent -> on(event)
            is StateChangeNodeEvent -> on(event)
        }
    }

    final override fun getCurrentTime(): TimeInstance {
        return currentTime
    }

    protected open fun updateCurrentTime(currentTime: TimeInstance) {
        if (currentTime >= this.currentTime) {
            this.currentTime = currentTime
            node.getParent()?.handle(
                StateChangeNodeEvent(
                    node,
                    false,
                    currentTime
                )
            )
        }
//update.start, !update.start, delayed, update.time))
    }

    final override fun getState(time: TimeInstance): Boolean {
        return satisfactionState.evaluate(time)
    }

    // TODO: the "current time" is not really clear atm
    final override fun getLatestState(): Boolean {
        return satisfactionState.evaluate(getCurrentTime())
    }

    override fun isSatisfiable(): Boolean {
        TODO("Not yet implemented")
    }

    protected abstract fun on(event: ActivationChangeNodeEvent)

    protected abstract fun on(event: EndOfExperimentNodeEvent)

    protected abstract fun on(event: EndOfRoundNodeEvent)

    protected abstract fun on(event: InitializeNodeEvent)

    protected fun evaluate(from: TimeInstance, to: TimeInstance) {
        /*
        val changePoints: TreeSet<TimeInstance> = TreeSet()
        changePoints.add(to) // force update at the end
        for (child in node.getChildren()) {
            val changes = child.getNodeLogic().getStateChanges(from, to)
            for (change in changes) {
                changePoints.add(change.time)
            }
        }

        for (time in changePoints.sorted()) {
            evaluate(time)
        }
        */
        val changePoints: TreeSet<TimeInstance> = TreeSet()
        changePoints.add(to) // force update at the end
        for (child in node.getChildren()) {
            val changes = child.getNodeLogic().getStateChanges(from, to)
            for (change in changes) {
                changePoints.add(change.time)
            }
        }

        for (time in changePoints.sorted()) {
            evaluate(time)
        }
    }

    protected abstract fun evaluate(at: TimeInstance)

    private fun on(event: StateChangeNodeEvent) {
        val until = getSmallestChildUpdateTime(event.getTime())
        if (evaluationNecessary(until)) {
            val from = handledChildrenUntil//getCurrentTime()
            evaluate(from, until)
            this.handledChildrenUntil = until
        }
    }

    protected fun updateTimes(time: TimeInstance) {
        if (time > this.lastUpdateReceived) {
            this.lastUpdateReceived = time
        }
    }

    protected open fun publishUpdates(toTime: TimeInstance) {
        val fromTime = this.lastUpdateSent
        publishUpdates(fromTime, toTime)
    }

    protected open fun publishUpdates(fromTime: TimeInstance, toTime: TimeInstance) {
        val updatesToPublish = satisfactionState.findRangeTimeInstances(fromTime, toTime)
        for (update in updatesToPublish) {
            val delayed = (update.start && !update.including) || (!update.start && update.including)
            node.getParent()?.handle(
                StateChangeNodeEvent(
                    node,
                    delayed,
                    update.time
                )
            )//update.start, !update.start, delayed, update.time))
        }
        if (toTime > this.lastUpdateSent) {
            this.lastUpdateSent = toTime
        }
    }

    private fun evaluationNecessary(to: TimeInstance): Boolean {
        return to > this.handledChildrenUntil
    }

    private fun getSmallestChildUpdateTime(receivedTime: TimeInstance): TimeInstance {
        var time = receivedTime
        for (child in this.node.getChildren()) {
            val childUpdateTime = child.getNodeLogic().getCurrentTime()
            time = minOf(time, childUpdateTime)
        }
        return time
    }

    override fun getStateChanges(from: TimeInstance, to: TimeInstance): List<TimeEventLog.RangeTimeInstance> {
        return satisfactionState.findRangeTimeInstances(from, to)
    }

    override fun getStateChange(at: TimeInstance): TimeEventLog.RangeTimeInstance? {
        return satisfactionState.findRangeTimeInstance(at)
    }

}