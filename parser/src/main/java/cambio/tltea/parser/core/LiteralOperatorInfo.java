package cambio.tltea.parser.core;

public record LiteralOperatorInfo(OperatorToken operator) implements IOperatorInfo {

    public LiteralOperatorInfo(String operatorImage) {
        this(OperatorTokenImageMap.INSTANCE.getToken(operatorImage));
    }
}
