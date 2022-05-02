package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

abstract class ActivationData<T : Any>(val data: T, val temporalContext: TemporalOperatorInfo)

class EventPreventionData(eventName: String, temporalContext: TemporalOperatorInfo) :
    ActivationData<String>(eventName, temporalContext)

class EventActivationData(eventName: String, temporalContext: TemporalOperatorInfo) :
    ActivationData<String>(eventName, temporalContext)

class ValueEventActivationData<T : Any>(
    data: T,
    temporalContext: TemporalOperatorInfo,
    operator: OperatorToken
) : ActivationData<T>(
    data,
    temporalContext
) {

    init {
        if (!OperatorToken.ComparisonOperatorTokens.contains(operator)) {
            throw IllegalArgumentException("Operator must be a comparison operator")
        }
    }
}