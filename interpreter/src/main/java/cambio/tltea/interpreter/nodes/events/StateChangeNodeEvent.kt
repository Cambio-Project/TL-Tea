package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

class StateChangeNodeEvent(
    updateTime: TimeInstance
) : AbstractLogicalNodeEvent(updateTime)
