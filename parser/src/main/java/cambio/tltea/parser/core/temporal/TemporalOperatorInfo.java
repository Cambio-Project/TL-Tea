package cambio.tltea.parser.core.temporal;

import cambio.tltea.parser.core.IOperatorInfo;
import cambio.tltea.parser.core.OperatorToken;
import cambio.tltea.parser.core.OperatorTokenImageMap;
import org.jetbrains.annotations.Contract;

import java.util.*;

public final class TemporalOperatorInfo implements IOperatorInfo {
    private final OperatorToken operator;
    private final ITemporalValue temporalValueExpression;


    public TemporalOperatorInfo(OperatorToken operator, ITemporalValue temporalValueExpression) {
        if (!OperatorToken.TemporalOperatorTokens.contains(operator)) {
            throw new IllegalArgumentException("Operator " + operator + " is not allowed");
        }
        this.operator = operator;
        this.temporalValueExpression = temporalValueExpression;
    }

    public TemporalOperatorInfo(String operatorImage, String temporalValueExpression) {
        this(OperatorTokenImageMap.INSTANCE.getToken(operatorImage), temporalValueExpression);
    }

    public TemporalOperatorInfo(String operator, ITemporalValue temporalValueExpression) {
        this(OperatorTokenImageMap.INSTANCE.getToken(operator), temporalValueExpression);
    }

    public TemporalOperatorInfo(OperatorToken operator, String temporalValueExpression) {
        this(operator, TemporalPropositionParser.parse(temporalValueExpression));
    }
@Contract
    public OperatorToken operator() {
        return operator;
    }

    public ITemporalValue temporalValueExpression() {
        return temporalValueExpression;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (TemporalOperatorInfo) obj;
        return Objects.equals(this.operator, that.operator) &&
               Objects.equals(this.temporalValueExpression, that.temporalValueExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, temporalValueExpression);
    }

    @Override
    public String toString() {
        return "TemporalOperatorInfo[" +
               "operator=" + operator + ", " +
               "temporalValueExpression=" + temporalValueExpression + ']';
    }

}
