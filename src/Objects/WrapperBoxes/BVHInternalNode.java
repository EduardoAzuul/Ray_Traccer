package Objects.WrapperBoxes;

import Objects.Cube;

public class BVHInternalNode extends BVHNode {
    private BVHNode leftChild;
    private BVHNode rightChild;

    public BVHInternalNode(Cube boundingBox, BVHNode leftChild, BVHNode rightChild) {
        super(boundingBox);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public BVHNode getLeftChild() {
        return leftChild;
    }

    public BVHNode getRightChild() {
        return rightChild;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public int getHeight() {
        int leftHeight = (leftChild instanceof BVHInternalNode)
                ? ((BVHInternalNode) leftChild).getHeight()
                : 1;
        int rightHeight = (rightChild instanceof BVHInternalNode)
                ? ((BVHInternalNode) rightChild).getHeight()
                : 1;
        return 1 + Math.max(leftHeight, rightHeight);
    }
}
