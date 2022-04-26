@file:JvmName("ConsequenceNode")

package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

/**
 * @author Lion Wagner
 */
abstract class ConsequenceNode(
    protected val triggerNotifier: TriggerNotifier,
    protected val temporalContext: TemporalOperatorInfo
) {
    abstract fun activateConsequence()
}