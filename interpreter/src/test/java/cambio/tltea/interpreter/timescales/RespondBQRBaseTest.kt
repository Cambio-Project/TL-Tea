package cambio.tltea.interpreter.timescales

abstract class RespondBQRBaseTest : TimescalesTest() {
    final override fun getMTLFormula(): String {
        return "G(((((\$b:q) == (true)) & (!((\$b:r) == (true)))) & (F((\$b:r) == (true)))) -> ((((\$b:p) == (true)) -> ((!((\$b:r) == (true)))U[" + applyFactor(
            3
        ) + "," + applyFactor(10) + "]((!((\$b:r) == (true))) & ((\$b:s) == (true)))))U((\$b:r) == (true))))"
    }
}