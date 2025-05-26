package Lights;

import vectors.Vector3D;

/**
 * Represents a point light source in a 3D scene.
 * Point lights emit light equally in all directions from a specific position (like a light bulb).
 * The intensity of the light decreases with distance from the source.
 *
 * @author José Eduardo Moreno Paredes
 */
public class PointLight extends Light {

    /**
     * Creates a new point light with specified position, color and intensity.
     *
     * @param position The 3D position of the light source in world coordinates
     * @param color The color of the light (RGB components as Vector3D)
     * @param intensity The base intensity/brightness of the light before attenuation
     */
    public PointLight(Vector3D position, Vector3D color, double intensity) {
        super(position, color, intensity);
    }

    /**
     * Computes the normalized direction vector from a surface point to this light source.
     *
     * @param surfacePoint The 3D point on the surface receiving light
     * @return Normalized direction vector pointing from the surface to the light
     */
    public Vector3D getDirectionFrom(Vector3D surfacePoint) {
        return position.subtract(surfacePoint).normalize();
    }

    /**
     * Computes the Euclidean distance from a surface point to this light source.
     * This is used for light attenuation calculations.
     *
     * @param surfacePoint The 3D point on the surface receiving light
     * @return Distance from the surface point to the light source in world units
     */
    public double getDistanceFrom(Vector3D surfacePoint) {
        return position.subtract(surfacePoint).magnitude();
    }
}