package cambio.tltea.interpreter.nodes.structure

import cambio.tltea.interpreter.nodes.logic.temporal.ITemporalLogic

class TemporalStateNode(parent: INode<Boolean, Boolean>?, logic: ITemporalLogic) :
    AbstractNode<Boolean, Boolean>(parent, logic)