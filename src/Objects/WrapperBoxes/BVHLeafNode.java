package Objects.WrapperBoxes;

import Objects.Cube;
import java.util.List;

public class BVHLeafNode extends BVHNode {
    private List<Integer> triangleIndices;

    public BVHLeafNode(Cube boundingBox, List<Integer> triangleIndices) {
        super(boundingBox);
        this.triangleIndices = triangleIndices;

    }

    public List<Integer> getTriangleIndices() {
        return triangleIndices;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}