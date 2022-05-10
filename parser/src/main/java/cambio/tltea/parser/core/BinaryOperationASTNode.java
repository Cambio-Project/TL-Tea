package cambio.tltea.parser.core;

import cambio.tltea.parser.core.temporal.TemporalBinaryOperationASTNode;
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo;

/**
 * @author Lion Wagner
 */
public class BinaryOperationASTNode extends OperatorASTNode {

    protected ASTNode left;
    protected ASTNode right;
    protected final int operatorPriority;

    public BinaryOperationASTNode(OperatorToken operator, ASTNode left, ASTNode right) {
        this(operator.image(), left, right);
    }

    public BinaryOperationASTNode(String operatorImage, ASTNode left, ASTNode right) {
        super(operatorImage);
        this.left = left;
        this.right = right;

        if (left != null) {
            this.left.setParent(this);
        }
        if (right != null) {
            this.right.setParent(this);
        }

        this.operatorPriority = BinaryOperatorPrecedenceMap.INSTANCE.getPrecedence(this.operator);
    }

    public BinaryOperationASTNode(LiteralOperatorInfo operator, ASTNode left, ASTNode right) {
        this(operator.operator(), left, right);
    }


    /**
     * Handels the reading of the next operator and node und placing it in the AST below or even with this node.
     *
     * @param operatorImage
     * @param leftNode
     * @return the updated root node of this (sub-)tree
     */
    public final BinaryOperationASTNode seepIn(IOperatorInfo operatorInfo, ASTNode leftNode) {

        if (operatorPriority > BinaryOperatorPrecedenceMap.INSTANCE.getPrecedence(operatorInfo.operator())
            || this.isBracketed()) {
            BinaryOperationASTNode newParent = null;
            if (operatorInfo instanceof LiteralOperatorInfo info) {
                newParent = new BinaryOperationASTNode(info.operator(), leftNode, this);
            } else if (operatorInfo instanceof TemporalOperatorInfo info) {
                newParent = new TemporalBinaryOperationASTNode(info, leftNode, this);
            }

            assert newParent != null : "Could not resolve Info type.";

            this.setParent(newParent);
            return newParent;
        } else {
            if (this.left instanceof BinaryOperationASTNode) {
                ((BinaryOperationASTNode) left).seepIn(operatorInfo, leftNode);
            } else {
                BinaryOperationASTNode newChild = null;
                if (operatorInfo instanceof LiteralOperatorInfo info) {
                    newChild = new BinaryOperationASTNode(info.operator(), leftNode, left);
                }
                if (operatorInfo instanceof TemporalOperatorInfo info) {
                    newChild = new TemporalBinaryOperationASTNode(info, leftNode, left);
                }
                this.left = newChild;
                this.left.setParent(this);
            }
            return this;
        }
    }

    @Override
    public String toString() {
        return "BinaryOperatorTreeNode{" +
               "op=" + operator + " ; " +
               "size=" + getSize() +
               '}';
    }

    @Override
    public String toFormulaString() {
        return operator.getShorthandImage();
    }

    @Override
    public ASTNode clone() {
        return new BinaryOperationASTNode(operator, left.clone(), right.clone());
    }

    @Override
    public int getSize() {
        return 1 + left.getSize() + right.getSize();
    }

    @Override
    public int getTreeWidth() {
        return left.getTreeWidth() + right.getTreeWidth();
    }

    @Override
    public int getChildrenCount() {
        return 2;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public ASTNode getLeftChild() {
        return left;
    }

    public ASTNode getRightChild() {
        return right;
    }

    public void setLeftChild(ASTNode left) {
        this.left = left;
        left.setParent(this);
    }

    public void setRightChild(ASTNode right) {
        this.right = right;
        right.setParent(this);
    }
}

