package cambio.tltea.interpreter.nodes.cause

sealed class ServiceListener<T: Comparable<T>>(valueOrEventName: String, serviceName: String,
                                               currentValue: T
) : ValueListener<T>(valueOrEventName, currentValue)

class InstanceCountListener(valueOrEventName: String, serviceName: String) :
    ServiceListener<Int>(valueOrEventName, serviceName, 0)

class ServiceStateListener(valueOrEventName: String, serviceName: String) :
    ServiceListener<String>(valueOrEventName, serviceName, "")

class LoadListener(valueOrEventName: String, serviceName: String) :
    ServiceListener<Int>(valueOrEventName, serviceName,0)