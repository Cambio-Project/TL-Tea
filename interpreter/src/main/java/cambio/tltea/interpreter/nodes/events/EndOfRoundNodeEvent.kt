package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

class EndOfRoundNodeEvent(lastUpdateTime: TimeInstance = TimeInstance(0)) : AbstractLogicalNodeEvent(lastUpdateTime) {
}