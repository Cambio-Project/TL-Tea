package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

class TriggerEndOfRoundNodeEvent(lastUpdateTime: TimeInstance) : AbstractStructuralNodeEvent(lastUpdateTime)