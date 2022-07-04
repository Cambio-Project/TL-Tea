package cambio.tltea.interpreter

import cambio.tltea.interpreter.nodes.ConsequenceInterpreter
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.mtl.generated.MTLParser

/**
 * @author Lion Wagner
 */
object Interpreter {

    fun interpretAsBehavior(mlt_formula: String): BehaviorInterpretationResult {
        return interpretAsBehavior(MTLParser.parse(mlt_formula))
    }

    fun interpretAsBehavior(root: ASTNode): BehaviorInterpretationResult {
        val clone = root.clone()
        val consequenceDescription = ConsequenceInterpreter().interpretAsMTL(clone)
        return BehaviorInterpretationResult(clone, consequenceDescription)
    }
}