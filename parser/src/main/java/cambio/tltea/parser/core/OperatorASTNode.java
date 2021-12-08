package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public abstract class OperatorASTNode extends ASTNode {
    protected final String operator;

    public OperatorASTNode(String operatorImage) {
        super();
        this.operator = TokenImageMap.INSTANCE.getTokenImage(operatorImage);
    }

    public String getOperator() {
        return operator;
    }
}
