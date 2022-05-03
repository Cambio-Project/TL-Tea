package cambio.tltea.interpreter

import cambio.tltea.interpreter.nodes.ConsequenceInterpreter
import cambio.tltea.parser.core.ASTNode

/**
 * @author Lion Wagner
 */
object Interpreter {
    fun interpretAsBehavior(root: ASTNode): BehaviorInterpretationResult {
        val clone = root.clone()
        val consequenceDescription = ConsequenceInterpreter().interpretAsMTL(clone)
        return BehaviorInterpretationResult(clone, consequenceDescription)
    }
}