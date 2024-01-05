package cambio.tltea.interpreter.timescales

abstract class AlwaysAQBaseTest : TimescalesTest() {

    final override fun getMTLFormula(): String {
        return "G(((\$b:q) == (true)) -> (G[0," + applyFactor(10) + "]((\$b:p) == (true))))"
    }

}