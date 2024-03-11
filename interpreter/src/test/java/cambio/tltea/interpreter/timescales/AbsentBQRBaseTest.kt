package cambio.tltea.interpreter.timescales


abstract class AbsentBQRBaseTest : TimescalesTest() {
    final override fun getMTLFormula(): String {
        return "G(((((\$b:q) == (true)) & (!((\$b:r) == (true)))) & (F((\$b:r) == (true)))) -> ((!((\$b:p) == (true)))U[" + applyFactor(
            3
        ) + "," + applyFactor(10) + "]((\$b:r) == (true))))"
    }

}