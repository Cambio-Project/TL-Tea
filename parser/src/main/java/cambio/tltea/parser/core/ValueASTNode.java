package cambio.tltea.parser.core;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public final class ValueASTNode extends ASTNode {
    private final @NotNull String value;

    public ValueASTNode(@NotNull String value) {
        super();
        this.value = value;
    }

    public @NotNull String getValue() {
        return value;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public int getTreeWidth() {
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

    @Override
    public String toFormulaString() {
        return value;
    }

    @Override
    public ASTNode clone() {
        return new ValueASTNode(value);
    }


    public boolean containsEventName() {
        return value.startsWith("(") && value.endsWith(")");
    }

    public @NotNull String getEventName() {
        if (containsEventName()) {
            return value.substring(1, value.length() - 1);
        } else {
            throw new IllegalStateException("Value does not contain event name");
        }
    }

    public boolean containsPropertyAccess() {
        return value.contains("$");
    }
}
