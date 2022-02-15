package cambio.tltea.parser.core.temporal;

import cambio.tltea.parser.core.IOperatorInfo;
import cambio.tltea.parser.core.OperatorToken;
import cambio.tltea.parser.core.OperatorTokenImageMap;

public record TemporalOperatorInfo(OperatorToken operator, String temporalValueExpression) implements IOperatorInfo {
    public TemporalOperatorInfo(String operatorImage, String temporalValueExpression) {
        this(OperatorTokenImageMap.INSTANCE.getToken(operatorImage), temporalValueExpression);
    }
}
