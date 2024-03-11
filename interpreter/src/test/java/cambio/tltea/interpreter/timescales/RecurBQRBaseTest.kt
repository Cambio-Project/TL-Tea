package cambio.tltea.interpreter.timescales

abstract class RecurBQRBaseTest : TimescalesTest() {
    final override fun getMTLFormula(): String {
        return "G(((((\$b:q) == (true)) & (!((\$b:r) == (true)))) & (F((\$b:r) == (true)))) -> ((F[0," + applyFactor(10) + "](((\$b:p) == (true)) | ((\$b:r) == (true))))U((\$b:r) == (true))))"
    }
}