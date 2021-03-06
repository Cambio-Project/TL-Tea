package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode

class ConsequenceDescription(val triggerManager: TriggerManager) {

    internal fun activateConsequence() {
        consequenceAST?.activateConsequence()
    }

    internal fun deactivateConsequence() {
        consequenceAST?.deactivateConsequence()
    }

    var consequenceAST: ConsequenceNode? = null

}
