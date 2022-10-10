package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.interpreter.nodes.consequence.activation.HookEventConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.LoadModificationConsequenceNode
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import kotlin.test.assertEquals

internal class EventActivationDataHoldersTests {

    @Test
    internal fun testLoadModificationEventDataParse() {

        val test = { load_str: String, endpointName: String, type: String, isFactor: Boolean, loadModifier: Double ->

            val triggerNotifier = mock(TriggerManager::class.java)

            val eventData = LoadModificationConsequenceNode(
                load_str,
                triggerNotifier,
                TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, 10.0))
            )
            assertEquals(isFactor, eventData.isFactor)
            assertEquals(loadModifier, eventData.loadModifier)
            assertEquals(endpointName, eventData.endpointName)

        }
        test("load[x3:test.endpoint]", "test.endpoint", "", true, 3.0)
        test("load[x3:constant:test.endpoint]", "test.endpoint", "constant", true, 3.0)
        test("load[x-3:test]", "test", "", true, -3.0)
        test("LOAD[200:testENDpoint]", "testENDpoint", "", false, 200.0)
        test("LOAD[-200:test.endpoint.anotherendpoint]", "test.endpoint.anotherendpoint", "", false, -200.0)
        test("LOAD[8:5]", "5", "", false, 8.0)
    }

    @Test
    internal fun testHookEventDataParse() {

        val test = { event_str: String, eventName: String ->
            val triggerNotifier = mock(TriggerManager::class.java)
            val eventData = HookEventConsequenceNode(
                event_str,
                triggerNotifier,
                TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, 10.0))
            )
            assertEquals(eventName, eventData.name)
        }
        test("event[test]", "test")
    }

    @Test
    internal fun testLoadModificationEventDataThrowsExceptionOnMalformedInput() {
        val malformedInput = listOf(
            "sdgserta", "load[asdfas]", "load[test.endpoint:x-200]", "load[x3.df:test.endpoint]",
            "load[sdf:Asdfa]", "LAOD[5:5]"
        )
        val triggerNotifier = mock(TriggerManager::class.java)

        malformedInput.forEach {
            println("Testing '$it' for failure...")
            assertThrows<IllegalArgumentException> {
                LoadModificationConsequenceNode(
                    it,
                    triggerNotifier,
                    TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, 10.0))
                )
            }
        }

    }
}