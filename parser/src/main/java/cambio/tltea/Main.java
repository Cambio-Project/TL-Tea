package cambio.tltea;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.ltl.generated.LTLParser;
import cambio.tltea.parser.ltl.generated.ParseException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Lion Wagner
 */
public class Main {
    public static void main(String[] args) throws ParseException {
        InputStream input = new ByteArrayInputStream(args[0].getBytes(StandardCharsets.UTF_8));
        LTLParser parser = new LTLParser(input);
        parser.enable_tracing();
        ASTNode root = parser.LTL_Formula_File();
        parser.disable_tracing();
    }
}