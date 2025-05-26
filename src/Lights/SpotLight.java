package Lights;

import vectors.Vector3D;

/**
 * Represents a spotlight source in a 3D scene.
 * Spotlights emit light in a cone-shaped direction from a specific position (like a flashlight or stage light).
 * The light intensity decreases with distance and is limited to the cone angle.
 *
 * @author José Eduardo Moreno Paredes
 */
public class SpotLight extends Light {
    /** The maximum distance the light can reach */
    private double radius;
    /** The normalized direction vector the spotlight is pointing */
    private Vector3D direction;
    /** The angle of the spotlight's cone in radians */
    private double angle;

    /**
     * Gets the maximum illumination radius of the spotlight
     * @return The radius in world units
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the maximum illumination radius of the spotlight
     * @param radius The new radius in world units (must be positive)
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the normalized direction vector of the spotlight
     * @return The current direction vector
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     * Sets the direction vector of the spotlight (will be normalized automatically)
     * @param direction The new direction vector
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction.normalize();
    }

    /**
     * Gets the spotlight's cone angle
     * @return The angle in radians
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the spotlight's cone angle
     * @param angle The new angle in radians (should be between 0 and π/2)
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Creates a new spotlight with specified properties
     * @param position The 3D position of the light source
     * @param color The RGB color of the light
     * @param intensity The base intensity of the light
     * @param radius The maximum illumination distance
     * @param angle The cone angle in radians
     * @param direction The initial direction vector (will be normalized)
     */
    public SpotLight(Vector3D position, Vector3D color, double intensity,
                     double radius, double angle, Vector3D direction) {
        super(position, color, intensity);
        setRadius(radius);
        setDirection(direction);
        setAngle(angle);
    }
}