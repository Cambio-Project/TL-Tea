package cambio.tltea.interpreter

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.ConsequenceInterpreter
import cambio.tltea.interpreter.nodes.GenericInterpreter
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.mtl.generated.MTLParser
import cambio.tltea.parser.mtl.generated.ParseException
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author Lion Wagner
 */
object Interpreter2 {

    fun interpretAllAsBehavior(
        mtlLoc: String,
        brokers: Brokers,
        isDebugOn: Boolean = false
    ): List<BehaviorInterpretationResult2> {
        Objects.requireNonNull(mtlLoc)
        try {
            if (!Paths.get(mtlLoc).toFile().exists()) {
                throw FileNotFoundException("[WARNING] Did not find MTL formula file at $mtlLoc")
            }
            val lines = Files.readAllLines(Paths.get(mtlLoc))
            return interpretAllAsBehavior(lines, brokers, isDebugOn)
        } catch (e: IOException) {
            if (isDebugOn) {
                e.printStackTrace()
            }
        } catch (e: ParseException) {
            if (isDebugOn) {
                e.printStackTrace()
            }
        }
        return mutableListOf()
    }

    fun interpretAllAsBehavior(
        formulas: Collection<String>,
        brokers: Brokers,
        isDebugOn: Boolean = false
    ): List<BehaviorInterpretationResult2> {
        Objects.requireNonNull(formulas)
        val results = mutableListOf<BehaviorInterpretationResult2>()
        for (formula in formulas) {
            results.add(interpretAsBehavior(formula, brokers))
        }
        return results
    }

    fun interpretAsBehavior(
        mlt_formula: String,
        brokers: Brokers
    ): BehaviorInterpretationResult2 {
        return interpretAsBehavior(MTLParser.parse(mlt_formula), brokers)
    }

    fun interpretAsBehavior(root: ASTNode, brokers: Brokers): BehaviorInterpretationResult2 {
        val clone = root.clone()
        val treeDescription = GenericInterpreter(brokers).interpretMTLCause(clone)
        return BehaviorInterpretationResult2(clone, treeDescription, brokers)
    }

}