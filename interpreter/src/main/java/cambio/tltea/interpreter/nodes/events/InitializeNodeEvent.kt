package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

class InitializeNodeEvent(lastUpdateTime: TimeInstance) : AbstractLogicalNodeEvent(lastUpdateTime)