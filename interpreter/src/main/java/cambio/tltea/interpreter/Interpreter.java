package cambio.tltea.interpreter;

import cambio.tltea.parser.core.ASTNode;

/**
 * @author Lion Wagner
 */
public final class Interpreter {
    public static BehaviorInterpretationResult interpretAsBehavior(ASTNode root) {
        return new BehaviorInterpreter().interpret(root);
    }
}
