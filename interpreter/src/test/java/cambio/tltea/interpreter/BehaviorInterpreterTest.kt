package cambio.tltea.interpreter

import cambio.tltea.interpreter.Interpreter.interpretAsBehavior
import cambio.tltea.parser.core.temporal.TimeInstance
import cambio.tltea.parser.mtl.generated.MTLParser
import cambio.tltea.parser.mtl.generated.ParseException
import org.junit.jupiter.api.Test

internal class BehaviorInterpreterTest {
    @Test
    @Throws(ParseException::class)
    fun debugTest() {
        val input = "((P)&(!!(C))|!(D))->((Q)==1234)"
        val ast = MTLParser(input).parse()
        val result = interpretAsBehavior(ast)
        result.activateProcessing();


        result.triggerManager.subscribeEventListener {
            println("hi $it")
        }

        result.triggerManager.eventActivationListeners
            .filter { !it.eventName.contains("F") }
            .forEach { it.activate(TimeInstance(0.0)) }
        result.triggerManager.eventActivationListeners
            .filter { it.eventName.contains("F") }
            .forEach { it.activate(TimeInstance(0.0)) }

        println(result)
    }
}