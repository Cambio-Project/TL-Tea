package cambio.tltea.interpreter.nodes

public sealed class KeywordFactory(protected val keywords: List<String>) {

    fun allowsKeyword(keyword: String): Boolean = keywords.contains(keyword)

    fun availableKeywords(): Iterable<String> = listOf(*keywords.toTypedArray())

    fun startsWithKeyword(keyword: String): Boolean = keywords.any { it.startsWith(keyword) }

}