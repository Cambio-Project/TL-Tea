package cambio.tltea.interpreter.nodes.logic.relational.deprecated

interface IPredicate<T: Comparable<T>>{
    fun evaluate():Boolean
}