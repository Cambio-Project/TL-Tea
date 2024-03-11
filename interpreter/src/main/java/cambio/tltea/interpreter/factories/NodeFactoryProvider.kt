package cambio.tltea.interpreter.factories

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.cause.ValueListener
import cambio.tltea.parser.core.ASTNode
import kotlin.reflect.KClass

class NodeFactoryProvider(private val brokers: Brokers, private val listeners: MutableList<ValueListener<*>>) :
    INodeFactoryProvider {
    private val factoryRegistry = HashMap<KClass<out ASTNode>, INodeFactory>()

    init {
        register(TemporalUnaryOperationASTNodeFactory(brokers))
        register(TemporalBinaryOperationASTNodeFactory(brokers))
        register(UnaryOperationASTNodeFactory(brokers))
        register(BinaryOperationASTNodeFactory(brokers, listeners))
        register(ValueASTNodeFactory(brokers, listeners))
    }

    override fun provideFactory(node: ASTNode): INodeFactory? {
        return factoryRegistry[node::class]
    }

    private fun register(nodeFactory: INodeFactory){
        val type = nodeFactory.getSupportedASTNodeType()
        factoryRegistry[type] = nodeFactory
    }

}