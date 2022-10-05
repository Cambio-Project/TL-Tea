package cambio.tltea.interpreter.nodes.consequence.activation

import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import cambio.tltea.parser.mtl.generated.ParseException

abstract class ActivationData<T : Any>(val data: T, val temporalContext: TemporalOperatorInfo)

class EventPreventionData(eventName: String, temporalContext: TemporalOperatorInfo) :
    ActivationData<String>(eventName, temporalContext)

class EventActivationData(eventName: String, temporalContext: TemporalOperatorInfo) :
    ActivationData<String>(eventName, temporalContext)

class ServiceFailureEventData(val serviceName: String, temporalContext: TemporalOperatorInfo) :
    ActivationData<String>(serviceName, temporalContext)

class ServiceStartEventData(val serviceName: String, temporalContext: TemporalOperatorInfo) :
    ActivationData<String>(serviceName, temporalContext)

class ServiceStopEventData(val serviceName: String, temporalContext: TemporalOperatorInfo) :
    ActivationData<String>(serviceName, temporalContext)

class LoadModificationEventData(
    val load_str: String, val modificationValue: Double, val functionType: String, val isFactor: Boolean, temporalContext: TemporalOperatorInfo
) : ActivationData<String>(load_str, temporalContext)


class ValueEventActivationData<T : Any>(
    val targetProperty: String,
    data: T,
    val operator: OperatorToken,
    val active: Boolean,
    temporalContext: TemporalOperatorInfo
) : ActivationData<T>(data, temporalContext) {

    init {
        if (!OperatorToken.ComparisonOperatorTokens.contains(operator)) {
            throw ParseException("Operator must be a comparison operator")
        }
    }
}