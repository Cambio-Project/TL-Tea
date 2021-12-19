package cambio.tltea.parser.core.temporal;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.core.UnaryOperationASTNode;

/**
 * @author Lion Wagner
 */
public final class TemporalUnaryOperationASTNode extends UnaryOperationASTNode implements ITemporalExpressionValueHolder {

    ITemporalValue temporalValueExpression;

    public TemporalUnaryOperationASTNode(String operator, ASTNode child) {
        super(operator, child);
        this.setTemporalExpressionValue("[0, ∞]");
    }

    public TemporalUnaryOperationASTNode(TemporalOperatorInfo operatorInfo, ASTNode child) {
        this(operatorInfo.operatorImage(), child);
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
