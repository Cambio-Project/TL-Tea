package cambio.tltea.interpreter.factories

import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import kotlin.reflect.KClass

interface INodeFactory {
    fun getSupportedASTNodeType(): KClass<out ASTNode>
    fun interpretAsCause(
        unNode: ASTNode,
        interpreterRoot: INode<Boolean, Boolean>,
        temporalContext: TemporalOperatorInfo
    ): INode<Boolean, Boolean>
}