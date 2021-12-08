package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public class UnaryOperationASTNode extends OperatorASTNode {

    private final ASTNode child;


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

    @Override
    public int getChildrenCount() {
        return 1;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
