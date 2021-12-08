package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public final class ValueASTNode extends ASTNode {
    private final String value;

    public ValueASTNode(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public int getChildrenCount() {
        return 0;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        return "ValueASTNode{" +
               "value='" + value + '\'' +
               '}';
    }
}
