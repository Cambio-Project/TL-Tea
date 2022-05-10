package cambio.tltea.parser.core;

import java.util.EnumSet;
import java.util.Set;

public enum OperatorToken {
    TRUE("<TRUE>", "T"),
    FALSE("<FALSE>", "F"),
    NOT("<NOT>", "¬"),
    AND("<AND>", "∧"),
    OR("<OR>", "∨"),
    IMPLIES("<IMPLIES>", "->"),
    IFF("<IFF>", "<->"),
    EQ("<EQ>", "=="),
    NEQ("<NEQ>", "!="),
    LT("<LT>", "<"),
    LEQ("<LEQ>", "<="),
    GT("<GT>", ">"),
    GEQ("<GEQ>", ">="),
    NEXT("<NEXT_T>", "X"),
    BEFORE("<BEFORE_T>", "B"),
    GLOBALLY("<GLOBALLY_T>", "G"),
    FINALLY("<FINALLY_T>", "F"),
    UNTIL("<UNTIL_T>", "U"),
    RELEASE("<RELEASE_T>", "R"),
    WEAKUNTIL("<WEAKUNTIL_T>", "W"),
    SINCE("<SINCE_T>", "S"),
    PAST("<PAST_T>", "P"),
    PROPHECY("<PROPHECY_T>", "▷"),
    HISTORY("<HISTORY_T>", "H"),
    UNKNOWN("<UNKNOWN>", "<UNKNOWN>");

    public static final Set<OperatorToken> TemporalOperatorTokens = EnumSet.of(OperatorToken.GLOBALLY,
                                                                               OperatorToken.FINALLY,
                                                                               OperatorToken.UNTIL,
                                                                               OperatorToken.SINCE,
                                                                               OperatorToken.RELEASE,
                                                                               OperatorToken.WEAKUNTIL,
                                                                               OperatorToken.BEFORE,
                                                                               OperatorToken.NEXT,
                                                                               OperatorToken.PAST,
                                                                               OperatorToken.HISTORY,
                                                                               OperatorToken.PROPHECY);
    public static final Set<OperatorToken> ComparisonOperatorTokens = EnumSet.of(OperatorToken.EQ,
                                                                                 OperatorToken.NEQ,
                                                                                 OperatorToken.LT,
                                                                                 OperatorToken.LEQ,
                                                                                 OperatorToken.GT,
                                                                                 OperatorToken.GEQ);

    private String image;
    private String shorthandImage;

    OperatorToken(String image, String shorthandString) {
        this.image = image;
        this.shorthandImage = shorthandString;
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
        this.shorthandImage = image;
        return this;
    }

    public String getImage() {
        return image;
    }

    public String image() {
        return image;
    }

    public String getShorthandImage() {
        return shorthandImage;
    }

    public String shorthandImage() {
        return shorthandImage;
    }

    public static OperatorToken UNKNOWN(String image) {
        return OperatorToken.UNKNOWN.setImage(image);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", this.name(), this.image);
    }

    /**
     * If the operator is a comparison operator, returns the corresponding inverted comparison operator token. E.g.
     * {@link OperatorToken#EQ} -> {@link OperatorToken#NEQ} and {@link OperatorToken#LT} -> {@link OperatorToken#GEQ}.
     *
     * @return the inverted comparison operator token
     * @throws IllegalArgumentException if the operator is not invertible operator
     */
    public OperatorToken invert() {
        return switch (this) {
            case EQ -> OperatorToken.NEQ;
            case NEQ -> OperatorToken.EQ;
            case LT -> OperatorToken.GEQ;
            case LEQ -> OperatorToken.GT;
            case GT -> OperatorToken.LEQ;
            case GEQ -> OperatorToken.LT;
            default -> throw new IllegalArgumentException("Cannot invert operator " + this);
        };
    }
}
