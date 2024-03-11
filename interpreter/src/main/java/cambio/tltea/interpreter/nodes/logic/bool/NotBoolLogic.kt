package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.parser.core.temporal.TimeInstance

class NotBoolLogic(brokers: Brokers) : AbstractBoolLogic(1, brokers) {

    override fun evaluateBool(at: TimeInstance) {
        val children = node.getChildren()
        val a = children[0].getNodeLogic().getState(at)
        val state = !a
        updateState(state, at)
    }

}