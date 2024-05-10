package cambio.tltea.interpreter.simulator

import cambio.tltea.interpreter.Interpreter2
import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.logic.temporal.ITemporalLogic
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.temporal.TimeInstance
import cambio.tltea.parser.mtl.generated.MTLParser
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals

abstract class SimulationTest {
    protected lateinit var simulator: TestEventSimulator
    private lateinit var parsedTopNode: ASTNode
    private lateinit var topNodes: List<INode<*, *>>
    private lateinit var brokers: Brokers

    @BeforeEach
    fun prepareSimulator() {
        brokers = Brokers()
        simulator = TestEventSimulator(brokers)
        brokers.metricBroker.listenerFactory.registrationStrategy = TestMetricRegistrationStrategy(simulator)
    }

    protected fun load(mtlFormula: String) {
        parsedTopNode = MTLParser.parse(mtlFormula)
        val interpretAsBehavior = Interpreter2.interpretAsBehavior(parsedTopNode, brokers)
        topNodes = listOf(interpretAsBehavior.treeDescription.causeASTRoot)
        interpretAsBehavior.activateProcessing()
    }

    protected fun load(mtlFormula: List<String>) {
        val interpretAsBehaviors = Interpreter2.interpretAllAsBehavior(mtlFormula, brokers)
        val allTopNodes = mutableListOf<INode<*, *>>()
        for(interpretAsBehavior in interpretAsBehaviors){
            allTopNodes.add(interpretAsBehavior.treeDescription.causeASTRoot)
            interpretAsBehavior.activateProcessing()
        }
        topNodes = allTopNodes
    }

    protected fun assertStateEquals(expected: Boolean) {
        assertEquals(expected, topNodes[0].getNodeLogic().getLatestState())
    }

    protected fun assertStateEquals(index: Int, expected: Boolean) {
        assertEquals(expected, topNodes[index].getNodeLogic().getLatestState())
    }

    protected fun assertStateEquals(expected: Boolean, time: Double) {
        assertEquals(expected, (topNodes[0].getNodeLogic() as ITemporalLogic).getState(TimeInstance(time)))
    }

}