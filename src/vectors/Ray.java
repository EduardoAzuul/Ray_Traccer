package vectors;

/**
 * The Ray class represents a 3D ray with an origin and a direction.
 * It is typically used for ray tracing and collision detection in 3D space.
 */
public class Ray {
    // The starting point of the ray
    private Vector3D origin;

    // The direction of the ray (should be normalized)
    private Vector3D direction;

    /**
     * Constructs a Ray with a given origin and direction.
     * The direction is automatically normalized.
     *
     * @param origin The starting point of the ray.
     * @param direction The direction vector of the ray.
     */
    public Ray(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction.normalize(); // Ensure the direction is normalized
    }

    /**
     * Calculates the point along the ray at distance t from the origin.
     *
     * @param t The distance along the ray.
     * @return A Vector3D representing the point at distance t.
     */
    public Vector3D getPoint(double t) {
        return origin.add(direction.multiplyByScalar(t));
    }

    /**
     * Returns the origin of the ray.
     *
     * @return A Vector3D representing the ray's origin.
     */
    public Vector3D getOrigin() {
        return origin;
    }

    /**
     * Returns the (normalized) direction of the ray.
     *
     * @return A Vector3D representing the ray's direction.
     */
    public Vector3D getDirection() {
        return direction;
    }
}
