package cambio.tltea.interpreter;

import cambio.tltea.interpreter.nodes.cause.EventActivationListener;
import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.temporal.TimeInstance;
import cambio.tltea.parser.mtl.generated.MTLParser;
import cambio.tltea.parser.mtl.generated.ParseException;
import org.junit.jupiter.api.Test;

class BehaviorInterpreterTest {
    @Test
    void debugTest() throws ParseException {
        String input = "((P)&(C)&(D))->(Q)";
        ASTNode ast = new MTLParser(input).parse();
        BehaviorInterpreter behaviorInterpreter = new BehaviorInterpreter();
        var result = behaviorInterpreter.interpret(ast);

        result.getTriggerNotifier().subscribe((eventName, args) -> {
            System.out.println(eventName);
        });

        result.getListeners().forEach(eventActivationListener -> eventActivationListener.activate(new TimeInstance(0)));

        System.out.println(result);
    }
}