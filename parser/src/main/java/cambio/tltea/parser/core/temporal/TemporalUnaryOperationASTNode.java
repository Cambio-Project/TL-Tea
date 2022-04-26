package cambio.tltea.parser.core.temporal;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.OperatorToken;
import cambio.tltea.parser.core.UnaryOperationASTNode;

/**
 * @author Lion Wagner
 */
public final class TemporalUnaryOperationASTNode extends UnaryOperationASTNode implements ITemporalExpressionValueHolder, ITemporalOperationInfoHolder {

    ITemporalValue temporalValueExpression;

    public TemporalUnaryOperationASTNode(String operator, ASTNode child) {
        super(operator, child);
        this.setTemporalExpressionValue("[0, ∞]");
    }

    public TemporalUnaryOperationASTNode(OperatorToken operator, ASTNode child) {
        this(operator.image(), child);
    }

    public TemporalUnaryOperationASTNode(TemporalOperatorInfo operatorInfo, ASTNode child) {
        this(operatorInfo.operator(), child);
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
        return super.toFormulaString()  + this.getTemporalValue().toString();
    }
}
