package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

/**
 * @author Lion Wagner
 */
class ImplicationNode(
    val causeDescription: CauseDescription,
    val consequence: ConsequenceDescription,
    temporalContext: TemporalOperatorInfo,
    triggerManager: TriggerManager
) : ConsequenceNode(triggerManager, temporalContext), StateChangeListener<Boolean> {

    override fun activateConsequence() {
        //activate listeners to input of cause
        causeDescription.activateListeners()

        //activate self listening to changes
        causeDescription.causeChangePublisher.subscribe(this)
    }

    override fun deactivateConsequence() {
        causeDescription.deactivateListeners()
        causeDescription.causeChangePublisher.unsubscribe(this)
    }

    override fun onEvent(event: StateChangeEvent<Boolean>) {
        //activate consequence if the new value carried by the event is "true"
        if (event.newValue) {
            consequence.activateConsequence()
        }
        else {
            consequence.deactivateConsequence()
        }
    }

    /**
     * @return whether the cause expression is satisfied
     */
    val currentValue: Boolean
        get() = causeDescription.causeASTRoot.currentValue

}