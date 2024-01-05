package cambio.tltea.parser.core;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public class UnaryOperationASTNode extends OperatorASTNode {

    private ASTNode child;

    public UnaryOperationASTNode(@NotNull OperatorToken operator, ASTNode child) {
        super(operator);
        this.child = child;
        this.children.add(child);
        child.setParent(this);
    }

    public UnaryOperationASTNode(String operator, ASTNode child) {
        super(operator);
        this.child = child;
        this.children.add(child);
        child.setParent(this);
    }

    @Override
    public int getSize() {
        return getChildrenCount() + child.getSize();
    }

    @Override
    public int getTreeWidth() {
        return child.getTreeWidth();
    }

    public ASTNode getChild() {
        return child;
    }

    public void setChild(ASTNode child) {
        this.child = child;
        child.setParent(this);
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
    public String toFormulaString() {
        return operator.getShorthandImage();
    }

    @Override
    public ASTNode clone() {
        return new UnaryOperationASTNode(this.operator, child.clone());
    }
}
