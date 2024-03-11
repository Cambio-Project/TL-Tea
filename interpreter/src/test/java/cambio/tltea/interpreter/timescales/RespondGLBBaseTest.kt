package cambio.tltea.interpreter.timescales

abstract class RespondGLBBaseTest : TimescalesTest() {
    final override fun getMTLFormula(): String {
        return "G(((\$b:p) == (true))->(F[" + applyFactor(3) + "," + applyFactor(10) + "]((\$b:s) == (true))))"
    }
}