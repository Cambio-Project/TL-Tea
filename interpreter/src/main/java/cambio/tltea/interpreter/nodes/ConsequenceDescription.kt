package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode

class ConsequenceDescription {

    fun activateConsequence() {
        consequenceAST?.activateConsequence()
    }

    var consequenceAST: ConsequenceNode? = null


}
