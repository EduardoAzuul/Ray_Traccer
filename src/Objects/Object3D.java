package Objects;

import Materials.Material;
import vectors.Vector3D;

/**
 * Abstract base class representing a 3D object in a scene.
 * Provides common properties and methods for all 3D objects,
 * including position, rotation, scale, color, and material.
 *
 * @author José Eduardo Moreno
 */
public abstract class Object3D {
    /** The scale of the object along x, y, and z axes */
    public Vector3D scale = new Vector3D(0, 0, 0);

    /** The position of the object in 3D space */
    public Vector3D position = new Vector3D(0, 0, 0);

    /** The rotation of the object around x, y, and z axes (in degrees) */
    public Vector3D rotation = new Vector3D(0, 0, 0);

    /** The RGB color of the object (values 0-255) */
    public Vector3D color = new Vector3D(0, 255, 0);

    /** The material properties of the object */
    private Material material;

    /**
     * Constructs a new 3D object with specified properties.
     *
     * @param color The RGB color of the object (values 0-255)
     * @param rotation The initial rotation in degrees around x, y, z axes
     * @param position The initial position in 3D space
     * @param scale The initial scale along x, y, z axes
     */
    public Object3D(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale) {
        this.color = color;
        this.rotation = rotation;
        this.position = position;
        this.scale = scale;
    }

    /**
     * Gets the color of the object as a Vector3D.
     *
     * @return RGB color vector (x=red, y=green, z=blue) with values 0-255
     */
    public Vector3D getColor() {
        return color;
    }

    /**
     * Gets the red component of the object's color.
     *
     * @return Red component (0-255)
     */
    public int getColorRed() {
        int r = (int) color.x;
        return r;
    }

    /**
     * Gets the green component of the object's color.
     *
     * @return Green component (0-255)
     */
    public int getColorGreen() {
        int g = (int) color.y;
        return g;
    }

    /**
     * Gets the blue component of the object's color.
     *
     * @return Blue component (0-255)
     */
    public int getColorBlue() {
        int b = (int) color.z;
        return b;
    }

    /**
     * Gets the color as a packed integer in 0xRRGGBB format.
     *
     * @return Packed RGB color integer
     */
    public int getColorInt() {
        int r = (int) color.x;  // Red component
        int g = (int) color.y;  // Green component
        int b = (int) color.z;  // Blue component
        int rgb = (r << 16) + (g << 8) + b; // Shift bits to proper positions
        return rgb;
    }

    /**
     * Gets the scale of the object.
     *
     * @return Scale vector (x, y, z components)
     */
    public Vector3D getScale() {
        return scale;
    }

    /**
     * Sets the scale of the object.
     *
     * @param scale New scale vector (x, y, z components)
     */
    public void setScale(Vector3D scale) {
        this.scale = scale;
    }

    /**
     * Gets the position of the object.
     *
     * @return Position vector in 3D space
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the position of the object.
     *
     * @param position New position vector in 3D space
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Gets the rotation of the object.
     *
     * @return Rotation vector in degrees (x, y, z axes)
     */
    public Vector3D getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the object.
     *
     * @param rotation New rotation vector in degrees (x, y, z axes)
     */
    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
    }

    /**
     * Sets the color of the object.
     *
     * @param color New RGB color vector (values 0-255)
     */
    public void setColor(Vector3D color) {
        this.color = color;
    }

    /**
     * Gets the material of the object.
     *
     * @return The material properties
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the object.
     *
     * @param material New material properties
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}