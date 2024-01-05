package cambio.tltea.interpreter.nodes.structure

import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.ILogic

sealed class AbstractNode<T, U>(private val parent: INode<Boolean, Boolean>?, protected val logic: ILogic) :
    INode<Boolean, Boolean> {
    private var isInitialized = false
    private var initializeCount = 0
    private var isFinalized = false
    private var finalizeCount = 0
    private var roundEndedCount = 0
    private val children: MutableList<INode<Boolean, Boolean>> = ArrayList()

    init {
        parent?.getChildren()?.add(this)
        logic.initialize(this)
    }

    override fun getParent(): INode<Boolean, Boolean>? {
        return parent
    }

    override fun getNodeLogic(): ILogic {
        return logic
    }

    override fun getChildren(): MutableList<INode<Boolean, Boolean>> {
        return children
    }

    final override fun handle(event: INodeEvent) {
        println("") // TODO: remove
        println(this.logic.javaClass.simpleName + " --> Node received event") // TODO: remove
        println(event.getTime().time.toString() + " - " + event.javaClass.simpleName) // TODO: remove
        when (event) {
            is IStructuralNodeEvent -> handle(event)
            is ILogicalNodeEvent -> handle(event)
        }
    }

    private fun handle(event: IStructuralNodeEvent) {
        when (event) {
            is TriggerInitializeNodeEvent -> on(event)
            is TriggerEndOfExperimentNodeEvent -> on(event)
            is TriggerEndOfRoundNodeEvent -> on(event)
        }
    }

    private fun handle(event: ILogicalNodeEvent) {
        when (event) {
            is InitializeNodeEvent -> on(event)
            is StateChangeNodeEvent -> on(event)
            is EndOfRoundNodeEvent -> on(event)
            is EndOfExperimentNodeEvent -> on(event)
            is ActivationChangeNodeEvent -> on(event)
        }
    }

    open protected fun on(event: StateChangeNodeEvent) {
        if (canBeUpdated()) {
            logic.handle(event)
        }
    }

    protected fun on(event: EndOfRoundNodeEvent) {
        if (canBeUpdated()) {
            roundEndedCount++
            val allChildrenEndedRound = children.size <= roundEndedCount
            if (allChildrenEndedRound) {
                roundEndedCount = 0
                logic.handle(event)
                notifyParent(event)
            }
        }
    }

    protected fun on(event: TriggerInitializeNodeEvent) {
        if (children.size > 0) {
            notifyChildren(event)
        } else {
            handle(InitializeNodeEvent(event.getTime()))
        }
    }

    open protected fun on(event: InitializeNodeEvent) {
        if (!isInitialized) {
            initializeCount++
            val allChildrenInitialized = children.size <= initializeCount
            if (allChildrenInitialized) {
                initializeCount = 0
                logic.handle(event)
                isInitialized = true
                notifyParent(event)
            }
        }
    }

    protected fun on(event: TriggerEndOfExperimentNodeEvent) {
        if (children.size > 0) {
            notifyChildren(event)
        } else {
            handle(EndOfExperimentNodeEvent(event.getTime()))
        }
    }

    protected fun on(event: TriggerEndOfRoundNodeEvent) {
        if (children.size > 0) {
            notifyChildren(event)
        } else {
            handle(EndOfRoundNodeEvent(event.getTime()))
        }
    }

    protected fun on(event: EndOfExperimentNodeEvent) {
        if (canBeUpdated()) {
            finalizeCount++
            val allChildrenFinalized = children.size <= finalizeCount
            if (allChildrenFinalized) {
                finalizeCount = 0
                logic.handle(event)
                isFinalized = true
                notifyParent(event)
            }
        }
    }

    protected fun on(event: ActivationChangeNodeEvent) {
        // do nothing
    }

    fun notifyChildren(event: INodeEvent) {
        for (child in children) {
            child.handle(event)
        }
    }

    fun notifyParent(event: INodeEvent) {
        parent?.handle(event)
    }

    private fun canBeUpdated(): Boolean {
        return isInitialized && !isFinalized
    }

}