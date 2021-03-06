package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.ValueEventActivationData
import cambio.tltea.parser.mtl.generated.MTLParser
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ConsequenceInterpreterTest {

    @Test
    fun interpretSimpleImplication() {
        val formula = "(a) -> (b)"
        val ast = MTLParser.parse(formula)
        val interpretationResult = ConsequenceInterpreter().interpretAsMTL(ast)

        println(interpretationResult.consequenceAST)
    }
}