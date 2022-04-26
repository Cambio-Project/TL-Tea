package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public abstract class OperatorASTNode extends ASTNode {
    protected final OperatorToken operator;

    public OperatorASTNode(String operatorImage) {
        this(OperatorTokenImageMap.INSTANCE.getToken(operatorImage));
    }

    public OperatorASTNode(OperatorToken operator) {
        super();
        this.operator = operator;
    }

    public OperatorToken getOperator() {
        return operator;
    }
}
