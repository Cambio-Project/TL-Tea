package cambio.tltea.interpreter.factories

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.cause.ConstantValueProvider
import cambio.tltea.interpreter.nodes.cause.EventActivationListener
import cambio.tltea.interpreter.nodes.cause.ValueListener
import cambio.tltea.interpreter.nodes.logic.bool.ConstantBoolLogic
import cambio.tltea.interpreter.nodes.logic.relational.EQRelationalLogic
import cambio.tltea.interpreter.nodes.structure.RelationalNode
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.interpreter.nodes.structure.StateNode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.ValueASTNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import kotlin.reflect.KClass

/**
 * Currently unused!
 */
@Deprecated("currently value nodes are handled in the higher-level operators")
class ValueASTNodeFactory(private val brokers: Brokers, private val listeners: MutableList<ValueListener<*>>) :
    INodeFactory {
    override fun getSupportedASTNodeType(): KClass<out ASTNode> {
        return ValueASTNode::class
    }

    override fun interpretAsCause(
        unNode: ASTNode,
        interpreterRoot: INode<Boolean, Boolean>,
        temporalContext: TemporalOperatorInfo
    ): INode<Boolean, Boolean> {
        unNode as ValueASTNode
        if (unNode.isBooleanConstant) {
            return StateNode(interpreterRoot, ConstantBoolLogic(unNode.returnBooleanConstant(), brokers))
        }
        if (unNode.containsEventName()) {
            val eventName = unNode.eventName
            val eventActivationListener: ValueListener<Boolean>
            val eventTextLowercase = unNode.eventName.lowercase()

            if (eventTextLowercase.startsWith("event")) {
                // handle named events
                val regex = Regex("event\\[(.+)]", RegexOption.IGNORE_CASE)
                val match = regex.matchEntire(eventName) ?: throw IllegalArgumentException(
                    "Invalid load modification description string '$eventName'.\n" +
                            "Expected format 'load[x<float>[:<type>]:<endpoint name>]'.\n" +
                            "The 'x' in front of the float is optional to select between factor or fixed value. "
                )
                val extractedEventName = match.groupValues[1]
                eventActivationListener = EventActivationListener("event.$extractedEventName")
                listeners.add(eventActivationListener)
            } else {
                eventActivationListener = EventActivationListener(eventName)
                listeners.add(eventActivationListener)
            }
            //wrap event activation in a ==True comparison
            return RelationalNode(
                interpreterRoot,
                EQRelationalLogic(Brokers()),// TODO: pass brokers
                eventActivationListener,
                ConstantValueProvider(true)
            )
        }
        throw UnsupportedOperationException(
            "Value %s cannot be interpreted as event Activation. Try wrapping it in parenthesis like '(%s)'.".format(
                unNode.value,
                unNode.value
            )
        )
    }
}