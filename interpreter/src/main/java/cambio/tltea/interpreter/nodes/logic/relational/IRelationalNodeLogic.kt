package cambio.tltea.interpreter.nodes.logic.relational

import cambio.tltea.interpreter.nodes.logic.ILogic

sealed interface IRelationalNodeLogic<T : Comparable<T>> : ILogic {
    fun setLeftValue(value: T): IRelationalNodeLogic<T>
    fun setRightValue(value: T): IRelationalNodeLogic<T>
}