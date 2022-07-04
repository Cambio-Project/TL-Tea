package cambio.tltea.interpreter.nodes.cause

sealed class ServiceListener<T>(valueOrEventName: String, serviceName: String) : ValueListener<T>(valueOrEventName)

class InstanceCountListener(valueOrEventName: String, serviceName: String) :
    ServiceListener<Number>(valueOrEventName, serviceName)

class ServiceStateListener(valueOrEventName: String, serviceName: String) :
    ServiceListener<String>(valueOrEventName, serviceName)


class LoadListener(valueOrEventName: String, serviceName: String) :
    ServiceListener<Number>(valueOrEventName, serviceName)