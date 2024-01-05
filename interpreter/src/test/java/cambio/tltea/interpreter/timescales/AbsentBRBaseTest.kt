package cambio.tltea.interpreter.timescales

abstract class AbsentBRBaseTest : TimescalesTest() {

    final override fun getMTLFormula(): String {
        return "G((F[0," + applyFactor(10) + "]((\$b:r) == (true))) -> ((!((\$b:p) == (true)))U((\$b:r) == (true))))"
    }

}