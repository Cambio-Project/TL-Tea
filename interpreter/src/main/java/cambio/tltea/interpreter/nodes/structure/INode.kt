package cambio.tltea.interpreter.nodes.structure

import cambio.tltea.interpreter.nodes.events.INodeEvent
import cambio.tltea.interpreter.nodes.logic.ILogic

interface INode<T, U> {
    fun handle(event: INodeEvent)
    fun getChildren(): MutableList<INode<Boolean, Boolean>>
    fun getParent(): INode<Boolean, Boolean>?
    fun getNodeLogic(): ILogic
}