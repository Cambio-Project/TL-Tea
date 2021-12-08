package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public final class TemporalUnaryOperationASTNode extends UnaryOperationASTNode implements ITemporalExpressionValueHolder {

    Object temporalValueExpression;

    public TemporalUnaryOperationASTNode(String operator, ASTNode child) {
        super(operator, child);
        this.setTemporalExpressionValue("[0, ∞]");
    }

    public TemporalUnaryOperationASTNode(TemporalOperatorInfo operatorInfo, ASTNode child) {
        this(operatorInfo.operatorImage(), child);
        this.setTemporalExpressionValue(operatorInfo.temporalValueExpression());
    }


    @Override
    public void setTemporalExpressionValue(Object temporalValueExpression) {
        this.temporalValueExpression = temporalValueExpression;
    }

    @Override
    public Object getTemporalExpressionValue() {
        return temporalValueExpression;
    }
}
