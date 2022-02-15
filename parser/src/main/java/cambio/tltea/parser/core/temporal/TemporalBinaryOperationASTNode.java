package cambio.tltea.parser.core.temporal;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.BinaryOperationASTNode;

/**
 * @author Lion Wagner
 */
public final class TemporalBinaryOperationASTNode extends BinaryOperationASTNode implements ITemporalExpressionValueHolder {

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
    public void setTemporalExpressionValue(String temporalValueExpression) {
                this.temporalValueExpression = TemporalPropositionParser.parse(temporalValueExpression);
    }

    @Override
    public ITemporalValue getTemporalValue() {
        return temporalValueExpression;
    }

}