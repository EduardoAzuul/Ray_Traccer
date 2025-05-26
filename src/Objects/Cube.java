package Objects;

import vectors.Vector3D;

/**
 * Represents an Axis-Aligned Bounding Box (AABB) in 3D space.
 * An AABB is a rectangular box that is aligned with the coordinate axes
 * and is defined by its minimum and maximum corner points.
 *
 * @author José Eduardo Moreno
 */
public class Cube {
    private Vector3D min;
    private Vector3D max;

    /**
     * Gets the minimum corner point of the AABB.
     *
     * @return The minimum corner as a Vector3D
     */
    public Vector3D getMin() {
        return min;
    }

    /**
     * Sets the minimum corner point of the AABB.
     *
     * @param min The new minimum corner as a Vector3D
     */
    public void setMin(Vector3D min) {
        this.min = min;
    }

    /**
     * Gets the maximum corner point of the AABB.
     *
     * @return The maximum corner as a Vector3D
     */
    public Vector3D getMax() {
        return max;
    }

    /**
     * Sets the maximum corner point of the AABB.
     *
     * @param max The new maximum corner as a Vector3D
     */
    public void setMax(Vector3D max) {
        this.max = max;
    }

    /**
     * Constructs a new AABB with the given minimum and maximum corners.
     *
     * @param min The minimum corner point
     * @param max The maximum corner point
     */
    public Cube(Vector3D min, Vector3D max) {
        setMin(min);
        setMax(max);
    }

    /**
     * Calculates the center point of the AABB.
     *
     * @return The center point as a Vector3D
     */
    public Vector3D getCenter() {
        return new Vector3D(
                (min.getX() + max.getX()) / 2.0,
                (min.getY() + max.getY()) / 2.0,
                (min.getZ() + max.getZ()) / 2.0
        );
    }

    /**
     * Calculates the dimensions (width, height, depth) of the AABB.
     *
     * @return The dimensions as a Vector3D where:
     *         x = width (x-axis dimension),
     *         y = height (y-axis dimension),
     *         z = depth (z-axis dimension)
     */
    public Vector3D getDimensions() {
        return new Vector3D(
                max.getX() - min.getX(),
                max.getY() - min.getY(),
                max.getZ() - min.getZ()
        );
    }

    /**
     * Checks if a point is contained within this AABB.
     *
     * @param point The point to check
     * @return true if the point is inside or on the boundary of the AABB, false otherwise
     */
    public boolean contains(Vector3D point) {
        return point.getX() >= min.getX() && point.getX() <= max.getX() &&
                point.getY() >= min.getY() && point.getY() <= max.getY() &&
                point.getZ() >= min.getZ() && point.getZ() <= max.getZ();
    }

    /**
     * Checks if this AABB intersects with another AABB.
     *
     * @param other The other AABB to check for intersection
     * @return true if the AABBs intersect (including edge/corner cases), false otherwise
     */
    public boolean intersects(Cube other) {
        return this.max.getX() >= other.min.getX() && this.min.getX() <= other.max.getX() &&
                this.max.getY() >= other.min.getY() && this.min.getY() <= other.max.getY() &&
                this.max.getZ() >= other.min.getZ() && this.min.getZ() <= other.max.getZ();
    }
}