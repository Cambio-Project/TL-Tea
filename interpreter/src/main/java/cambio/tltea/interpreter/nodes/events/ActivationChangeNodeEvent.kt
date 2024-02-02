package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

@Deprecated("Activation might not be needed any longer")
class ActivationChangeNodeEvent(lastUpdateTime: TimeInstance) : AbstractLogicalNodeEvent(lastUpdateTime)