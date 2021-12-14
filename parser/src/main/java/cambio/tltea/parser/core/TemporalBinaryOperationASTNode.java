package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public final class TemporalBinaryOperationASTNode extends BinaryOperationASTNode implements ITemporalExpressionValueHolder {

    private Object temporalValueExpression;

    public TemporalBinaryOperationASTNode(String operator, ASTNode left, ASTNode right) {
        super(operator, left, right);
        this.setTemporalExpressionValue("[0, ∞]");
    }

    public TemporalBinaryOperationASTNode(TemporalOperatorInfo operatorInfo, ASTNode left, ASTNode right) {
        super(operatorInfo.operatorImage(), left, right);
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