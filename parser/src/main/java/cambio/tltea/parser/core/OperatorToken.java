package cambio.tltea.parser.core;

public enum OperatorToken {
    TRUE("<TRUE>"),
    FALSE("<FALSE>"),
    NOT("<NOT>"),
    AND("<AND>"),
    OR("<OR>"),
    IMPLIES("<IMPLIES>"),
    IFF("<IFF>"),
    EQ("<EQ>"),
    NEQ("<NEQ>"),
    LT("<LT>"),
    LEQ("<LEQ>"),
    GT("<GT>"),
    GEQ("<GEQ>"),
    NEXT("<NEXT_T>"),
    GLOBALLY("<GLOBALLY_T>"),
    FINALLY("<FINALLY_T>"),
    UNTIL("<UNTIL_T>"),
    RELEASE("<RELEASE_T>"),
    WEAKUNTIL("<WEAKUNTIL_T>");

    public final String image;

    OperatorToken(String image) {
        this.image = image;
    }

    public static OperatorToken fromString(String text) {
        if (text != null) {
            text = text.replaceAll("[<>]", "");
            for (OperatorToken operatorToken : OperatorToken.values()) {
                if (text.equalsIgnoreCase(operatorToken.image)) {
                    return operatorToken;
                }
            }
        }
        return null;
    }
}
