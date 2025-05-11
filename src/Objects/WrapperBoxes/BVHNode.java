package Objects.WrapperBoxes;

import Objects.Cube;

public abstract class BVHNode {
    protected Cube boundingBox;

    public BVHNode(Cube boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Cube getBoundingBox() {
        return boundingBox;
    }

    public abstract boolean isLeaf();
}