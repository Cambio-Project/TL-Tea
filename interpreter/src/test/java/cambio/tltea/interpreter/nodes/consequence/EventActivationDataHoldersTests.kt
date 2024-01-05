package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.interpreter.nodes.consequence.activation.HookEventConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.LoadModificationConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.ServiceFailureConsequenceNode
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class EventActivationDataHoldersTests {

    @Test
    internal fun testServiceKillEventDataParse(){
        val test = {data:String, serviceName:String, instanceCount:Int ->
            val triggerNotifier = mock(TriggerManager::class.java)

            val eventData = ServiceFailureConsequenceNode(
                data,
                triggerNotifier,
                TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, 10.0))
            )
            assertEquals(serviceName, eventData.serviceName)
            assertEquals(instanceCount, eventData.count)
        }
        test("kill[test.endpoint,1]", "test.endpoint", 1)
        test("kill[test.endpoint]", "test.endpoint", Int.MAX_VALUE)
    }

    @Test
    internal fun testLoadModificationEventDataParse() {

        val test =
            { load_str: String, endpointName: String, type: String, isFactor: Boolean, loadModifier: Double, start: Double, end: Double ->

                val triggerNotifier = mock(TriggerManager::class.java)

                val eventData = LoadModificationConsequenceNode(
                    load_str,
                    triggerNotifier,
                    TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, 10.0))
                )
                assertEquals(isFactor, eventData.isFactor)
                assertEquals(loadModifier, eventData.loadModifier)
                assertEquals(endpointName, eventData.endpointName)
                assertTrue(eventData.duration is TemporalInterval)
                assertEquals(start, eventData.duration.startAsDouble)
                assertEquals(end, eventData.duration.endAsDouble)

            }
        test("load[0,10][x3:test.endpoint]", "test.endpoint", "", true, 3.0, 0.0, 10.0)
        test("load[1,10][x3:constant:test.endpoint]", "test.endpoint", "constant", true, 3.0, 1.0, 10.0)
        test("load[1,1][x-3:test]", "test", "", true, -3.0, 1.0, 1.0)
        test("LOAD[0,10][200:testENDpoint]", "testENDpoint", "", false, 200.0, 0.0, 10.0)
        test(
            "LOAD[0,10][-200:test.endpoint.anotherendpoint]",
            "test.endpoint.anotherendpoint",
            "",
            false,
            -200.0,
            0.0,
            10.0
        )
        test("LOAD[0,10][8:5]", "5", "", false, 8.0, 0.0, 10.0)
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