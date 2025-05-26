package Lights;

import vectors.Vector3D;

/**
 * Represents a directional light source in a 3D scene.
 * Directional lights have a specific direction but no position (like sunlight).
 * The light is assumed to come from infinitely far away.
 *
 * @author José Eduardo Moreno Paredes
 */
public class DirectionalLight extends Light {
    /** The normalized direction vector of the light */
    private Vector3D Direction;

    /**
     * Gets the direction vector of the light
     * @return The normalized direction vector
     */
    public Vector3D getDirection() {
        return Direction;
    }

    /**
     * Sets the direction vector of the light.
     * Note: The direction vector should be normalized.
     * @param direction The new direction vector for the light
     */
    public void setDirection(Vector3D direction) {
        Direction = direction;
    }

    /**
     * Creates a new directional light with specified color, intensity and direction.
     * The direction vector will be normalized automatically.
     * @param color The color of the light (RGB components as Vector3D)
     * @param intensity The intensity/brightness of the light
     * @param direction The direction the light is pointing (will be normalized)
     */
    public DirectionalLight(Vector3D color, double intensity, Vector3D direction) {
        super(new Vector3D(), color, intensity);
        setDirection(direction.normalize());
    }
}