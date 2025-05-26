package Objects;

import vectors.Vector3D;

/**
 * A wrapper class for an Axis-Aligned Bounding Box (AABB).
 * <p>
 * This is used within {@code ObjObject} to perform spatial checks
 * (like collisions or containment) on the bounding box only, instead of evaluating each triangle.
 * </p>
 *
 * @author José Eduardo Moreno Paredes
 */
public class wrapperCube {
    private Vector3D min;
    private Vector3D max;

    /**
     * Constructs a new wrapperCube with specified minimum and maximum coordinates.
     *
     * @param min The minimum corner (lowest x, y, z values).
     * @param max The maximum corner (highest x, y, z values).
     */
    public wrapperCube(Vector3D min, Vector3D max) {
        setMin(min);
        setMax(max);
    }

    /**
     * Returns the minimum corner of the bounding box.
     *
     * @return The minimum Vector3D.
     */
    public Vector3D getMin() {
        return min;
    }

    /**
     * Sets the minimum corner of the bounding box.
     *
     * @param min The Vector3D to set as minimum.
     */
    public void setMin(Vector3D min) {
        this.min = min;
    }

    /**
     * Returns the maximum corner of the bounding box.
     *
     * @return The maximum Vector3D.
     */
    public Vector3D getMax() {
        return max;
    }

    /**
     * Sets the maximum corner of the bounding box.
     *
     * @param max The Vector3D to set as maximum.
     */
    public void setMax(Vector3D max) {
        this.max = max;
    }
}
