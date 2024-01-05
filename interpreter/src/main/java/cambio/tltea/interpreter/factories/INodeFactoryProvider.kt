package cambio.tltea.interpreter.factories

import cambio.tltea.parser.core.ASTNode

interface INodeFactoryProvider {
    fun provideFactory(node : ASTNode): INodeFactory?
}