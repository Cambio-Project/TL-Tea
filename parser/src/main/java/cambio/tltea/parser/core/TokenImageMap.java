package cambio.tltea.parser.core;

import java.util.HashMap;


public enum TokenImageMap {
    INSTANCE;

    static {
        INSTANCE.put("<TRUE>", "true", "True", "TRUE");
        INSTANCE.put("<FALSE>", "false", "False", "FALSE");

        INSTANCE.put("<NOT>", "!", "¬");
        INSTANCE.put("<AND>", "&", "∧");
        INSTANCE.put("<OR>", "|", "∨");
        INSTANCE.put("<NOT>", "!", "¬");
        INSTANCE.put("<IMPLIES>", "->", "→");
        INSTANCE.put("<IFF>", "<->", "↔");


        INSTANCE.put("<EQ>", "==", "=");
        INSTANCE.put("<NEQ>", "!=", "≠");
        INSTANCE.put("<LT>", "<", "<");
        INSTANCE.put("<LEQ>", "<=", "≤");
        INSTANCE.put("<GT>", ">", ">");
        INSTANCE.put("<GEQ>", ">=", "≥");

        INSTANCE.put("<NEXT_T>", "X", "x", "○");
        INSTANCE.put("<GLOBALLY_T>", "G", "g", "☐");
        INSTANCE.put("<FINALLY_T>", "F", "f", "◇");
        INSTANCE.put("<UNTIL_T>", "U", "u");
        INSTANCE.put("<RELEASE_T>", "R", "r");
        INSTANCE.put("<WEAKUNTIL_T>", "W", "w");
    }

    private final HashMap<String, String[]> tokenMap = new HashMap<>();
    private final HashMap<String, String> imageMap = new HashMap<>();

    private void put(String token, String... images) {
        tokenMap.put(token, images);

        imageMap.put(token, token);//identity
        imageMap.put(token.replaceAll("[><]",""), token);//identity
        for (String image : images) {
            imageMap.put(image, token);
        }
    }

    /**
     * Tries to grab the token based on the image or token name. also searches for "<" {@code image} ">".
     *
     * @return the token name of the given image in the form of "<" {@code tokenImage} ">" or null if no tokenImage is
     * found.
     */
    public String getTokenImage(String image) {
        image = image.replace("\"", "");//remove quotes to get rid of string literals like "\">\""
        String token = imageMap.get(image);
        if (token == null) {
            token = imageMap.get("<" + image + ">");
        }
        return token;
    }

    /**
     * Gets all potential token images for the given token.
     *
     * @param token
     * @return
     */
    public String[] getImages(String token) {
        return tokenMap.get(token);
    }
}
