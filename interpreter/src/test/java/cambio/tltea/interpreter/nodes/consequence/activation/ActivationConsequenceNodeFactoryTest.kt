package cambio.tltea.interpreter.nodes.consequence.activation

import cambio.tltea.interpreter.Interpreter
import cambio.tltea.interpreter.nodes.ActivationConsequenceNodeFactory
import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.util.*

internal class ActivationConsequenceNodeFactoryTest {

    private val triggerManager: TriggerManager
    private val temporalContext: TemporalOperatorInfo

    init {
        triggerManager = mock(TriggerManager::class.java)
        temporalContext = mock(TemporalOperatorInfo::class.java)
    }

    private fun checkFactory(
        input: String,
        expectedType: Class<out ActivationConsequenceNode>,
        inverted: Boolean = false
    ) {

        val node = if (inverted) ActivationConsequenceNodeFactory.getActivationConsequenceNode(
            input, triggerManager, temporalContext, inverted
        )
        else ActivationConsequenceNodeFactory.getActivationConsequenceNode(input, triggerManager, temporalContext)
        assertTrue(expectedType.isInstance(node))
    }

    private fun checkParser(input: String, expectedType: Class<out ActivationConsequenceNode>) {
        val interpretationResult = Interpreter.interpretAsBehavior(input)
        assertTrue(expectedType.isInstance(interpretationResult.consequenceDescription.consequenceAST))
    }

    @Test
    internal fun testParsesLoadModification() {
        checkFactory("load[5:endpoint]", LoadModificationConsequenceNode::class.java)

        checkParser("F(load[5:endpoint])", LoadModificationConsequenceNode::class.java)
    }

    @Test
    internal fun testParsesServiceStart() {
        checkFactory("start[service1]", ServiceStartConsequenceNode::class.java)
        //TODO: start does currently not have an inversion, maybe use "force stop" as event type
        //checkType("start[service1]", ServiceStopConsequenceNode::class.java, true)

        checkParser("F(start[service1])", ServiceStartConsequenceNode::class.java)
    }

    @Test
    internal fun testParsesServiceStop() {
        checkFactory("stop[service1]", ServiceStopConsequenceNode::class.java)
        //TODO: stop does currently have no inversion, maybe create "no shutdown" as event type
        //checkType("stop[service1]", ServiceStartConsequenceNode::class.java, true)

        checkParser("F(stop[service1])", ServiceStopConsequenceNode::class.java)
    }

    @Test
    internal fun testParsesServiceKilled() {
        checkFactory("kill[service1]", ServiceFailureConsequenceNode::class.java)
        //TODO: kill does currently have no inversion, maybe create "keep alive" as event type
        //checkType("stop[service1]", ServiceStartConsequenceNode::class.java, true)

        checkParser("F(kill[service1])", ServiceFailureConsequenceNode::class.java)
    }

    @Test
    internal fun testParsesHookEvent() {
        checkFactory("event[test]", HookEventConsequenceNode::class.java)
        checkFactory("event[test,linear]", HookEventConsequenceNode::class.java)
        // TODO: inversion currently not implemented

        checkParser("F(event[test])", HookEventConsequenceNode::class.java)
    }

    @Test
    internal fun testParsesRest() {
        val random = Random().nextInt(500)

        for (i in 0..random) {
            val randomStr = RandomStringUtils.random(200)
            checkFactory(randomStr, EventActivationConsequenceNode::class.java)
            checkFactory(randomStr, EventPreventionConsequenceNode::class.java, true)

            checkParser("F($randomStr)", EventActivationConsequenceNode::class.java)
            checkParser("F!($randomStr)", EventPreventionConsequenceNode::class.java)
        }
    }
}