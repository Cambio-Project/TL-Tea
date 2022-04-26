package cambio.tltea.parser.core

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ASTNodePrinterTest
{
    @Test
    fun testPrint()
    {
        //create ASTTree for "(!B) & (A | C)"
        val leaf1 = ValueASTNode("B")
        val leaf2 = ValueASTNode("A")
        val leaf3 = ValueASTNode("C")
        val node1 = UnaryOperationASTNode(OperatorToken.NOT, leaf1)
        val node2 = BinaryOperationASTNode(OperatorToken.OR, leaf2, leaf3)
        val root = BinaryOperationASTNode(OperatorToken.AND,node1, node2)
        ASTNodePrinter.print(root)
    }
}

