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
    WEAKUNTIL("<WEAKUNTIL_T>"),
    UNKNOWN("<UNKNOWN>");

    private String image;

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

    private OperatorToken setImage(String image) {
        this.image = image;
        return this;
    }

    public String getImage() {
        return image;
    }

    public String image() {
        return image;
    }

    public static OperatorToken UNKNOWN(String image) {
        return OperatorToken.UNKNOWN.setImage(image);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", this.name(), this.image);
    }
}
