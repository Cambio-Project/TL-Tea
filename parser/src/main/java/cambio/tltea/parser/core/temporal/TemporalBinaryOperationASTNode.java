package cambio.tltea.parser.core.temporal;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.BinaryOperationASTNode;

/**
 * @author Lion Wagner
 */
public final class TemporalBinaryOperationASTNode extends BinaryOperationASTNode implements ITemporalExpressionValueHolder, ITemporalOperationInfoHolder {

    private ITemporalValue temporalValueExpression;

    public TemporalBinaryOperationASTNode(String operator, ASTNode left, ASTNode right) {
        super(operator, left, right);
        this.setTemporalExpressionValue("[0, ∞]");
    }

    public TemporalBinaryOperationASTNode(TemporalOperatorInfo operatorInfo, ASTNode left, ASTNode right) {
        super(operatorInfo.operator(), left, right);
        this.setTemporalExpressionValue(operatorInfo.temporalValueExpression());
    }

    @Override
    public TemporalOperatorInfo toTemporalOperatorInfo() {
        return new TemporalOperatorInfo(this.getOperator(), this.getTemporalValue());
    }

    @Override
    public void setTemporalExpressionValue(ITemporalValue temporalValueExpression) {
        this.temporalValueExpression = temporalValueExpression;
    }

    @Override
    public ITemporalValue getTemporalValue() {
        return temporalValueExpression;
    }

    @Override
    public String toFormulaString() {
        return super.toFormulaString() + this.getTemporalValue().toString();
    }

    @Override
    public ASTNode clone() {
        return new TemporalBinaryOperationASTNode(this.toTemporalOperatorInfo(),
                                                  this.getLeftChild().clone(),
                                                  this.getRightChild().clone());
    }

}