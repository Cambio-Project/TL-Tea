package cambio.tltea.interpreter.nodes.events

import cambio.tltea.parser.core.temporal.TimeInstance

class TriggerInitializeNodeEvent(lastUpdateTime: TimeInstance = TimeInstance(0)) :
    AbstractStructuralNodeEvent(lastUpdateTime) {}