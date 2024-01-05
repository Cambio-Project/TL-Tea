package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

class EndOfExperimentNodeEvent(lastUpdateTime: TimeInstance) : AbstractLogicalNodeEvent(lastUpdateTime) {
}