package cambio.tltea.parser.mtl.generated;

import cambio.tltea.parser.core.*;
import cambio.tltea.parser.core.temporal.TemporalBinaryOperationASTNode;
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo;
import cambio.tltea.parser.core.temporal.TemporalUnaryOperationASTNode;
import cambio.tltea.parser.testhelper.ASTTreeComparison;
import org.junit.jupiter.api.Test;


/**
 * Regression tests. THis file lists some MTL formulas that ware created with PSP Wizard and tests the parser for
 * correctness.
 *
 * @author Lion Wagner
 */
public class MTLRegressionTests {

    @Test
    void test1() throws ParseException {
        MTLParser parser = new MTLParser("☐((P) → ◇(S))");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode finallyNode = new TemporalUnaryOperationASTNode("◇", value2);
        ASTNode binaryNode = new BinaryOperationASTNode("→", value1, finallyNode);
        ASTNode unaryNode = new TemporalUnaryOperationASTNode("☐", binaryNode);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(unaryNode, root2);
    }

    @Test
    void test2() throws ParseException {
        MTLParser parser = new MTLParser("☐((P) → ◇ (S))");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode finallyNode = new TemporalUnaryOperationASTNode("◇", value2);
        ASTNode binaryNode = new BinaryOperationASTNode("→", value1, finallyNode);
        ASTNode unaryNode = new TemporalUnaryOperationASTNode("☐", binaryNode);

        ASTTreeComparison.compareAST(unaryNode, parser.parse());
    }

    /**
     * Globally, if {P} [has occurred] then in response {S} [eventually holds] followed by {T1} [eventually holds].
     *
     * <p>
     * ☐((P) → (◇ ((S) ∧ ○(◇ ((T1))))))
     *
     * @throws ParseException
     */
    @Test
    void test3() throws ParseException {
        MTLParser parser = new MTLParser("☐((P) → (◇ ((S) ∧ ○(◇ ((T1))))))");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode value3 = new ValueASTNode("(T1)");
        ASTNode finallyNode = new TemporalUnaryOperationASTNode("◇", value3);
        ASTNode unaryNode = new TemporalUnaryOperationASTNode("○", finallyNode);
        ASTNode binaryNode = new BinaryOperationASTNode("∧", value2, unaryNode);
        ASTNode unaryNode2 = new TemporalUnaryOperationASTNode("◇", binaryNode);
        ASTNode binaryNode2 = new BinaryOperationASTNode("→", value1, unaryNode2);
        ASTNode unaryNode3 = new TemporalUnaryOperationASTNode("☐", binaryNode2);

        ASTTreeComparison.compareAST(unaryNode3, parser.parse());
    }


    /**
     * Globally, if {P} [has occurred] then in response {S} [holds] continually.
     *
     * <p>
     * ☐((P) → ☐ (S))
     *
     * @throws ParseException
     */
    @Test
    void test4() throws ParseException {
        MTLParser parser = new MTLParser("☐((P) → ☐ (S))");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode unaryNode = new TemporalUnaryOperationASTNode("☐", value2);
        ASTNode binaryNode = new BinaryOperationASTNode("→", value1, unaryNode);
        ASTNode unaryNode2 = new TemporalUnaryOperationASTNode("☐", binaryNode);

        ASTTreeComparison.compareAST(unaryNode2, parser.parse());
    }

    /**
     * Globally, if {P} followed by {S} [have occurred] then in response {T3} [eventually holds].
     *
     * <p>
     * ☐((P) ∧ ○(◇ ((S) → ◇ * (T3))))
     */
    @Test
    void test5() throws ParseException {
        MTLParser parser = new MTLParser("☐((P) ∧ ○(◇ ((S) → ◇ (T3))))");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode value3 = new ValueASTNode("(T3)");
        ASTNode finallyNode = new TemporalUnaryOperationASTNode("◇", value3);
        ASTNode binaryNode = new BinaryOperationASTNode("→", value2, finallyNode);
        ASTNode unaryNode = new TemporalUnaryOperationASTNode("◇", binaryNode);
        ASTNode unaryNode2 = new TemporalUnaryOperationASTNode("○", unaryNode);
        ASTNode binaryNode2 = new BinaryOperationASTNode("∧", value1, unaryNode2);
        ASTNode unaryNode3 = new TemporalUnaryOperationASTNode("☐", binaryNode2);

        ASTTreeComparison.compareAST(unaryNode3, parser.parse());
    }

    /**
     * Test for complex bracketing and precedence with  of  !(((a)|!(b)) & ((c)|(d)))
     */
    @Test
    void test6() throws ParseException {
        MTLParser parser = new MTLParser("!(((a)|!(b))&((c)|(d)))");

        ASTNode value1 = new ValueASTNode("(a)");
        ASTNode value2 = new ValueASTNode("(b)");
        ASTNode value3 = new ValueASTNode("(c)");
        ASTNode value4 = new ValueASTNode("(d)");
        ASTNode unaryNode = new UnaryOperationASTNode("!", value2);
        ASTNode binaryNode = new BinaryOperationASTNode("|", value1, unaryNode);
        ASTNode binaryNode2 = new BinaryOperationASTNode("|", value3, value4);
        ASTNode binaryNode3 = new BinaryOperationASTNode("&", binaryNode, binaryNode2);
        ASTNode unaryNode2 = new UnaryOperationASTNode("!", binaryNode3);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(unaryNode2, root2);
    }

    /**
     * Test for complex bracketing and precedence with  of ((a)|(b))&((c)|(d))
     */
    @Test
    void test7() throws ParseException {
        MTLParser parser = new MTLParser("((a)|(b)) & ((c)|(d))");

        ASTNode value1 = new ValueASTNode("(a)");
        ASTNode value2 = new ValueASTNode("(b)");
        ASTNode value3 = new ValueASTNode("(c)");
        ASTNode value4 = new ValueASTNode("(d)");
        ASTNode binaryNode = new BinaryOperationASTNode("|", value1, value2);
        ASTNode binaryNode2 = new BinaryOperationASTNode("|", value3, value4);
        ASTNode binaryNode3 = new BinaryOperationASTNode("&", binaryNode, binaryNode2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode3, root2);
    }


    /**
     * Test correct operator precedence with  ((a)) & ((b)|(c))
     */
    @Test
    void test8() throws ParseException {
        MTLParser parser = new MTLParser("((a)) & ((b)|(c))");

        ASTNode value1 = new ValueASTNode("(a)");
        ASTNode value2 = new ValueASTNode("(b)");
        ASTNode value3 = new ValueASTNode("(c)");
        ASTNode binaryNode = new BinaryOperationASTNode("|", value2, value3);
        ASTNode binaryNode2 = new BinaryOperationASTNode("&", value1, binaryNode);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode2, root2);
    }

    /**
     * Test correct operator precedence with (P) U (S)
     */
    @Test
    void test9() throws ParseException {
        MTLParser parser = new MTLParser("(P) U (S)");

        ASTNode value1 = new ValueASTNode("(P)");
        ASTNode value2 = new ValueASTNode("(S)");
        ASTNode binaryNode = new TemporalBinaryOperationASTNode("U", value1, value2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode, root2);
    }

    /**
     * Test correct operator precedence with ☐((T3)U((T2)))
     */
    @Test
    void test10() throws ParseException {
        MTLParser parser = new MTLParser("☐((T3)U((T2)))");

        ASTNode value1 = new ValueASTNode("(T3)");
        ASTNode value2 = new ValueASTNode("(T2)");
        ASTNode binaryNode = new TemporalBinaryOperationASTNode("U", value1, value2);
        ASTNode unaryNode = new TemporalUnaryOperationASTNode("☐", binaryNode);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(unaryNode, root2);
    }

    /**
     * Test correct operator precedence with true U (P).
     */
    @Test
    void test11() throws ParseException {
        MTLParser parser = new MTLParser("true U (P)");

        ASTNode value1 = new ValueASTNode("true");
        ASTNode value2 = new ValueASTNode("(P)");
        ASTNode binaryNode = new TemporalBinaryOperationASTNode("U", value1, value2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode, root2);
    }

    /**
     * Test correct operator precedence with false R (P).
     */
    @Test
    void test12() throws ParseException {
        MTLParser parser = new MTLParser("false R (P)");

        ASTNode value1 = new ValueASTNode("false");
        ASTNode value2 = new ValueASTNode("(P)");
        ASTNode binaryNode = new TemporalBinaryOperationASTNode("R", value1, value2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode, root2);
    }

    /**
     * Test correct operator precedence with (S) W (P).
     */
    @Test
    void test13() throws ParseException {
        MTLParser parser = new MTLParser("(S) W (P)");

        ASTNode value1 = new ValueASTNode("(S)");
        ASTNode value2 = new ValueASTNode("(P)");
        ASTNode binaryNode = new TemporalBinaryOperationASTNode("W", value1, value2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode, root2);
    }

    /**
     * Test correct operator precedence with 3 > 1.
     */
    @Test
    void test14() throws ParseException {
        MTLParser parser = new MTLParser("3 > 1");

        ASTNode value1 = new ValueASTNode("3");
        ASTNode value2 = new ValueASTNode("1");
        ASTNode binaryNode = new BinaryOperationASTNode(">", value1, value2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode, root2);
    }

    /**
     * Test correct operator precedence with 3 < (a).
     */
    @Test
    void test15() throws ParseException {
        MTLParser parser = new MTLParser("3 < (a)");

        ASTNode value1 = new ValueASTNode("3");
        ASTNode value2 = new ValueASTNode("(a)");
        ASTNode binaryNode = new BinaryOperationASTNode("<", value1, value2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode, root2);
    }

    /**
     * Test correct operator precedence with (sdf) <= 42.
     */
    @Test
    void test16() throws ParseException {
        MTLParser parser = new MTLParser("(sdf) <= 42");

        ASTNode value1 = new ValueASTNode("(sdf)");
        ASTNode value2 = new ValueASTNode("42");
        ASTNode binaryNode = new BinaryOperationASTNode("<=", value1, value2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode, root2);
    }


    /**
     * Test correct operator precedence with !((a) != (f)).
     */
    @Test
    void test17() throws ParseException {
        MTLParser parser = new MTLParser("!((a) != (f))");

        ASTNode value1 = new ValueASTNode("(a)");
        ASTNode value2 = new ValueASTNode("(f)");
        ASTNode binaryNode = new BinaryOperationASTNode("!=", value1, value2);
        ASTNode unaryNode = new UnaryOperationASTNode("!", binaryNode);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(unaryNode, root2);
    }


    /**
     * Test correct operator precedence with  (a)<6|85>=42.
     */
    @Test
    void test18() throws ParseException {
        MTLParser parser = new MTLParser("(a)<6|85>=42");

        ASTNode value1 = new ValueASTNode("(a)");
        ASTNode value2 = new ValueASTNode("6");
        ASTNode binaryNode1 = new BinaryOperationASTNode("<", value1, value2);
        ASTNode value3 = new ValueASTNode("85");
        ASTNode value4 = new ValueASTNode("42");
        ASTNode binaryNode2 = new BinaryOperationASTNode(">=", value3, value4);
        ASTNode binaryNode3 = new BinaryOperationASTNode("|", binaryNode1, binaryNode2);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode3, root2);
    }

    /**
     * Test correct operator precedence with  (a)&(b)|(c).
     */
    @Test
    void test19() throws ParseException {
        MTLParser parser = new MTLParser("(a)&(b)|(c)");

        ASTNode value1 = new ValueASTNode("(a)");
        ASTNode value2 = new ValueASTNode("(b)");
        ASTNode binaryNode1 = new BinaryOperationASTNode("&", value1, value2);
        ASTNode value3 = new ValueASTNode("(c)");
        ASTNode binaryNode2 = new BinaryOperationASTNode("|", binaryNode1, value3);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(binaryNode2, root2);
    }

    /**
     * Test correct operator precedence with  G[abc]((a)==(b)).
     */
    @Test
    void test20() throws ParseException {
        MTLParser parser = new MTLParser("G[event]((a)==(b))");

        ASTNode value1 = new ValueASTNode("(a)");
        ASTNode value2 = new ValueASTNode("(b)");
        ASTNode binaryNode = new BinaryOperationASTNode("==", value1, value2);
        ASTNode unaryNode = new TemporalUnaryOperationASTNode(new TemporalOperatorInfo("G","[event]"), binaryNode);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(unaryNode, root2);
    }

    /**
     * Test correct operator precedence with  G(((instances[service_A])<3) -> F[<10]((instances[service_A])>=3)).
     */
    @Test
    void test21() throws ParseException {
        MTLParser parser = new MTLParser("G(((instances[service_A])<3) -> F[<10]((instances[service_A])>=3))");

        ASTNode value1 = new ValueASTNode("(instances[service_A])");
        ASTNode value2 = new ValueASTNode("3");
        ASTNode binaryNode1 = new BinaryOperationASTNode("<", value1, value2);

        ASTNode value3 = new ValueASTNode("(instances[service_A])");
        ASTNode value4 = new ValueASTNode("3");
        ASTNode binaryNode2 = new BinaryOperationASTNode(">=", value3, value4);
        ASTNode unaryNode1 = new TemporalUnaryOperationASTNode(new TemporalOperatorInfo("F","[<10]"), binaryNode2);

        ASTNode binaryNode3 = new BinaryOperationASTNode("->", binaryNode1, unaryNode1);
        ASTNode unaryNode2 = new TemporalUnaryOperationASTNode("G", binaryNode3);

        ASTNode root2 = parser.parse();
        ASTTreeComparison.compareAST(unaryNode2, root2);
    }


    @Test
    void squareBracketsInEventDescription() throws ParseException {
        var result =MTLParser.parse("G(hello[World])");
    }
}
