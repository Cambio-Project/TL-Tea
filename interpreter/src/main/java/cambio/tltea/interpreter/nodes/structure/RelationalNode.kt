package cambio.tltea.interpreter.nodes.structure

import cambio.tltea.interpreter.nodes.cause.ValueListener
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.interpreter.nodes.logic.relational.IRelationalNodeLogic
import cambio.tltea.parser.core.temporal.TimeInstance

class RelationalNode<T : Comparable<T>>(
    parent: INode<Boolean, Boolean>,
    private val relationalLogic: IRelationalNodeLogic<T>,
    private val left: ValueListener<T>,
    private val right: ValueListener<T>
) : AbstractNode<Boolean, Boolean>(parent, relationalLogic) {

    init {
        connectToValueListeners()
    }

    private fun connectToValueListeners() {
        left.subscribe { event ->
            handle(
                StateChangeNodeEvent(
                    event.`when`() as TimeInstance
                )
            )
        }
        right.subscribe { event ->
            handle(
                StateChangeNodeEvent(
                    event.`when`() as TimeInstance
                )
            )
        }
    }

    override fun on(event: InitializeNodeEvent) {
        super.on(event)
        left.startListening()
        right.startListening()
    }

    override fun on(event: StateChangeNodeEvent) {
        relationalLogic.setLeftValue(left.currentValue)
        relationalLogic.setRightValue(right.currentValue)
        super.on(event)
    }

}