package cambio.tltea.interpreter.timescales

abstract class RecurGLBBaseTest : TimescalesTest() {
    final override fun getMTLFormula(): String {
        return "(G(F[0," + applyFactor(10) + "]((\$b:p) == (true))))"
    }
}