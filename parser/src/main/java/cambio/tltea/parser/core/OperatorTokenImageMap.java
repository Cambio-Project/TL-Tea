package cambio.tltea.parser.core;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public enum OperatorTokenImageMap {
    INSTANCE;

    static {
        INSTANCE.put(OperatorToken.TRUE, "true", "True", "TRUE");
        INSTANCE.put(OperatorToken.FALSE, "false", "False", "FALSE");

        INSTANCE.put(OperatorToken.NOT, "!", "¬");
        INSTANCE.put(OperatorToken.AND, "&", "∧");
        INSTANCE.put(OperatorToken.OR, "|", "∨");
        INSTANCE.put(OperatorToken.IMPLIES, "->", "→");
        INSTANCE.put(OperatorToken.IFF, "<->", "↔");


        INSTANCE.put(OperatorToken.EQ, "==", "=");
        INSTANCE.put(OperatorToken.NEQ, "!=", "≠");
        INSTANCE.put(OperatorToken.LT, "<", "<");
        INSTANCE.put(OperatorToken.LEQ, "<=", "≤");
        INSTANCE.put(OperatorToken.GT, ">", ">");
        INSTANCE.put(OperatorToken.GEQ, ">=", "≥");

        INSTANCE.put(OperatorToken.NEXT, "X", "x", "○");
        INSTANCE.put(OperatorToken.GLOBALLY, "G", "g", "☐");
        INSTANCE.put(OperatorToken.FINALLY, "F", "f", "◇");
        INSTANCE.put(OperatorToken.UNTIL, "U", "u");
        INSTANCE.put(OperatorToken.RELEASE, "R", "r");
        INSTANCE.put(OperatorToken.WEAKUNTIL, "W", "w");
    }

    private final HashMap<String, OperatorToken> tokenResolveMap = new HashMap<>();

    private void put(@NotNull OperatorToken token, String... images) {
        tokenResolveMap.put(token.image(), token);//identity
        tokenResolveMap.put(token.image().replaceAll("[><]", ""), token);//identity
        for (String image : images) {
            tokenResolveMap.put(image, token);
        }
    }

    /**
     * Tries to grab the token based on the image or token name. also searches for "<" {@code image} ">".
     *
     * @return the token name of the given image in the form of "<" {@code tokenImage} ">" or null if no tokenImage is
     * found.
     */
    public OperatorToken getToken(String image) {
        image = image.replace("\"", "").trim();//remove quotes to get rid of string literals like "\">\""
        OperatorToken token = tokenResolveMap.get(image);
        if (token == null) {
            token = tokenResolveMap.get("<" + image + ">");
        }
        if (token == null) {
            var unknownToken = OperatorToken.UNKNOWN(image);
            INSTANCE.put(unknownToken, image);
            return unknownToken;
        }
        return token;
    }
}
