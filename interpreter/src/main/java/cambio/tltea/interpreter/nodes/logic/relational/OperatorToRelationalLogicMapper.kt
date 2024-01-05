package cambio.tltea.interpreter.nodes.logic.relational

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.parser.core.OperatorToken

class OperatorToRelationalLogicMapper<T : Comparable<T>>(private val brokers: Brokers) {
    fun map(operator: OperatorToken): IRelationalNodeLogic<T> {
        require(OperatorToken.ComparisonOperatorTokens.contains(operator)) { "Operator not supported as comparison: $operator" }
        return when (operator) {
            OperatorToken.EQ -> {
                EQRelationalLogic(brokers)
            }

            OperatorToken.NEQ -> {
                NEQRelationalLogic(brokers)
            }

            OperatorToken.GT -> {
                GTRelationalLogic(brokers)
            }

            OperatorToken.GEQ -> {
                GEQRelationalLogic(brokers)
            }

            OperatorToken.LT -> {
                LTRelationalLogic(brokers)
            }

            OperatorToken.LEQ -> {
                LEQRelationalLogic(brokers)
            }

            else -> throw IllegalStateException("Operator not supported as comparison: $operator")
        }
    }
}