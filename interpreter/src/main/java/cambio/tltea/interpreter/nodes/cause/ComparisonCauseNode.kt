package cambio.tltea.interpreter.nodes.cause

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.StateChangeEvent
import cambio.tltea.interpreter.nodes.logic.relational.*
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import java.util.Objects

/**
 * @author Lion Wagner, Sebastian Frank
 */
class ComparisonCauseNode<T : Comparable<T>>(
    operator: OperatorToken,
    temporalContext: TemporalOperatorInfo?,
    private val left: ValueListener<T>,
    private val right: ValueListener<T>
) : CauseNode(temporalContext) {
    private val predicate: IRelationalNodeLogic<T>
    private var lastValue = false

    init {
        predicate = initializePredicate(operator)
        connectToValueListeners()
    }

    private fun connectToValueListeners() {
        left.subscribe { event ->
            val lastValue = lastValue
            super.notifySubscribers(StateChangeEvent(this, currentValue, lastValue, event.`when`()))
        }
        right.subscribe { event ->
            val lastValue = lastValue
            super.notifySubscribers(StateChangeEvent(this, currentValue, lastValue, event.`when`()))
        }
    }

    private fun initializePredicate(operator: OperatorToken): IRelationalNodeLogic<T> {
        require(OperatorToken.ComparisonOperatorTokens.contains(operator)) { "Operator not supported as comparison: $operator" }
        val mapper = OperatorToRelationalLogicMapper<T>(Brokers()) // TODO: pass brokers
        return mapper.map(operator)
    }

    override fun getCurrentValue(): Boolean {
        return compareSides().also { lastValue = it }
    }

    private fun compareSides(): Boolean {
        val val1 = left.currentValue
        val val2 = right.currentValue

        Objects.requireNonNull(val1)
        Objects.requireNonNull(val2)

        return (predicate.setLeftValue(val1).setRightValue(val2) as AbstractRelationalLogic).evaluate()
    }
}