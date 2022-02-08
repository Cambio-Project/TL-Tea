package cambio.tltea;

import cambio.tltea.parser.core.ASTNode;
import cambio.tltea.parser.mtl.generated.MTLParser;
import cambio.tltea.parser.mtl.generated.ParseException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Lion Wagner
 */
public class Main {
    public static void main(String[] args) throws ParseException {
        InputStream input = new ByteArrayInputStream(args[0].getBytes(StandardCharsets.UTF_8));
        MTLParser parser = new MTLParser(input);
        parser.enable_tracing();
        ASTNode root = parser.parse();
        parser.disable_tracing();
    }
}