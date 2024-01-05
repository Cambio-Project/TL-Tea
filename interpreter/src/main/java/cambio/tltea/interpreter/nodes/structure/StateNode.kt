package cambio.tltea.interpreter.nodes.structure

import cambio.tltea.interpreter.nodes.logic.bool.IBoolLogic

class StateNode(parent: INode<Boolean, Boolean>?, logic: IBoolLogic) :
    AbstractNode<Boolean, Boolean>(parent, logic)