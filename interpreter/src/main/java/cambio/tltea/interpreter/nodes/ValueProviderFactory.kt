package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.InstanceCountListener
import cambio.tltea.interpreter.nodes.cause.LoadListener
import cambio.tltea.interpreter.nodes.cause.ServiceStateListener
import java.util.regex.Pattern

object ValueProviderFactory : KeywordFactory(listOf("instance_count", "service_state","load")) {

    private val pattern :Pattern = Pattern.compile("^(.*)\\[(.*)]")


    fun createValueProvider(propositionText: String): StateChangedPublisher<*> {
        if (!startsWithKeyword(propositionText))
            throw IllegalArgumentException("Invalid value provider proposition: $propositionText")

        val match = pattern.matcher(propositionText)

        val keyword = match.group(1)
        val value = match.group(2)

        return when(keyword) {
            "instance_count" -> InstanceCountListener(propositionText, value)
            "service_state" -> ServiceStateListener(propositionText,value)
            "load" -> LoadListener(propositionText,value)
            else -> throw IllegalArgumentException("Invalid value provider proposition: $propositionText")
        }
    }
}