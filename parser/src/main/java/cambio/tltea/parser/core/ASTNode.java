package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public abstract class ASTNode implements Cloneable {

    protected OperatorASTNode parent;

    protected boolean isBracketed = false;

    public ASTNode() {
    }

    public final OperatorASTNode getParent() {
        return parent;
    }

    protected final void setParent(OperatorASTNode parent) {
        this.parent = parent;
    }

    public abstract int getSize();

    /**
     * Get children count.
     */
    public abstract int getChildrenCount();

    /**
     * Is Leaf?
     */
    public abstract boolean isLeaf();

    /**
     * Is Root?
     */
    public final boolean isRoot() {
        return parent == null;
    }

    @Override
    public String toString() {
        return "ASTNode{" +
               "type=" + this.getClass().getSimpleName() +
               '}';
    }

    public boolean isBracketed() {
        return isBracketed;
    }

    public void setBracketed(boolean bracketed) {
        isBracketed = bracketed;
    }

    /**
     * Creates a deep copy of this node.
     *
     * @return a deep copy of this node.
     */
    @Override
    public abstract ASTNode clone();

}
