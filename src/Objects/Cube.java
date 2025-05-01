package Objects;

import vectors.Vector3D;

//Axis-Aligned Bounding Box - AABB
public class Cube {
    private Vector3D min;
    private Vector3D max;

    public Vector3D getMin() {
        return min;
    }

    public void setMin(Vector3D min) {
        this.min = min;
    }

    public Vector3D getMax() {
        return max;
    }

    public void setMax(Vector3D max) {
        this.max = max;
    }

    public Cube(Vector3D min, Vector3D max) {
        setMin(min);
        setMax(max);
    }

    // Calculate the center of the AABB
    public Vector3D getCenter() {
        return new Vector3D(
                (min.getX() + max.getX()) / 2.0,
                (min.getY() + max.getY()) / 2.0,
                (min.getZ() + max.getZ()) / 2.0
        );
    }

    // Calculate the dimensions (width, height, depth) of the AABB
    public Vector3D getDimensions() {
        return new Vector3D(
                max.getX() - min.getX(),
                max.getY() - min.getY(),
                max.getZ() - min.getZ()
        );
    }

    // Check if a point is inside the AABB
    public boolean contains(Vector3D point) {
        return point.getX() >= min.getX() && point.getX() <= max.getX() &&
                point.getY() >= min.getY() && point.getY() <= max.getY() &&
                point.getZ() >= min.getZ() && point.getZ() <= max.getZ();
    }

    // Check if this AABB intersects with another AABB
    public boolean intersects(Cube other) {
        return this.max.getX() >= other.min.getX() && this.min.getX() <= other.max.getX() &&
                this.max.getY() >= other.min.getY() && this.min.getY() <= other.max.getY() &&
                this.max.getZ() >= other.min.getZ() && this.min.getZ() <= other.max.getZ();
    }
}