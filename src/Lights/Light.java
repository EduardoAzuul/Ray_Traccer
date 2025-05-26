/**
 * Light.java
 *
 * This abstract class represents a light source in a 3D scene.
 * It defines common properties such as position, color, and intensity
 * that all specific types of lights should inherit.
 *
 * Author: José Eduardo Moreno Paredes
 * Date: 2025
 */

package Lights;

import Objects.Object3D;
import vectors.Vector3D;

public abstract class Light {
    protected Vector3D position;
    protected Vector3D color;      // RGB values (either 0–255 or normalized 0.0–1.0 depending on the renderer)
    protected double intensity;

    // Getter for the light's position
    public Vector3D getPosition() {
        return position;
    }

    // Setter for the light's position
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    // Getter for the light's color
    public Vector3D getColor() {
        return color;
    }

    /**
     * Returns the color of the light encoded as an integer in RGB format.
     * Each component (R, G, B) is clamped to the [0, 255] range.
     *
     * @return An integer representing the RGB color.
     */
    public int getColorint() {
        Vector3D color = getColor();
        int red = (int) Math.min(255, Math.max(0, color.x));
        int green = (int) Math.min(255, Math.max(0, color.y));
        int blue = (int) Math.min(255, Math.max(0, color.z));
        return (red << 16) + (green << 8) + blue;
    }

    // Setter for the light's color
    public void setColor(Vector3D color) {
        this.color = color;
    }

    // Getter for the light's intensity
    public double getIntensity() {
        return intensity;
    }

    // Setter for the light's intensity
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    /**
     * Constructor for a generic Light.
     *
     * @param position The position of the light in 3D space.
     * @param color The RGB color of the light.
     * @param intensity The brightness or strength of the light.
     */
    public Light(Vector3D position, Vector3D color, double intensity) {
        setPosition(position);
        setColor(color);
        setIntensity(intensity);
    }

    /**
     * Returns a string representation of the light.
     */
    @Override
    public String toString() {
        return "PointLight{" +
                "position=" + position +
                ", color=" + color +
                ", intensity=" + intensity +
                '}';
    }
}
