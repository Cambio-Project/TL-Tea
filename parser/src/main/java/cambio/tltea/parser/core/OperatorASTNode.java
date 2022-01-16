package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public abstract class OperatorASTNode extends ASTNode {
    protected final OperatorToken operator;

    public OperatorASTNode(String operatorImage) {
        super();
        this.operator = OperatorTokenImageMap.INSTANCE.getToken(operatorImage);
    }

    public OperatorToken getOperator() {
        return operator;
    }
}
