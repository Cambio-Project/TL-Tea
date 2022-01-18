package cambio.tltea.parser.core;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public class UnaryOperationASTNode extends OperatorASTNode {

    private ASTNode child;

    public UnaryOperationASTNode(@NotNull OperatorToken operator, ASTNode child) {
        this(operator.image(), child);
    }

    public UnaryOperationASTNode(String operator, ASTNode child) {
        super(operator);
        this.child = child;
        child.setParent(this);
    }

    @Override
    public int getSize() {
        return getChildrenCount() + child.getSize();
    }

    public ASTNode getChild() {
        return child;
    }

    public void setChild(ASTNode child) {
        this.child = child;
    }

    @Override
    public int getChildrenCount() {
        return 1;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public ASTNode clone() {
        return new UnaryOperationASTNode(this.operator, child.clone());
    }
}
