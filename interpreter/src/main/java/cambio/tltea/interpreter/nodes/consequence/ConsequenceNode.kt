@file:JvmName("ConsequenceNode")

package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

/**
 * @author Lion Wagner
 */
abstract class ConsequenceNode(
    protected val triggerManager: TriggerManager,
    protected val temporalContext: TemporalOperatorInfo
) {
    abstract fun activateConsequence()
    abstract fun deactivateConsequence()
}