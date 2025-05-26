package Objects;

import Materials.Material;
import vectors.Vector3D;

/**
 * Represents a 3D sphere object that extends the base Object3D class.
 * Provides sphere-specific properties and calculations including radius,
 * surface area, volume, and geometric operations.
 *
 * @author José Eduardo Moreno
 */
public class Sphere extends Object3D {
    /** The radius of the sphere */
    private double radius;

    /** The material properties of the sphere */
    private Material material;

    /**
     * Constructs a new Sphere with specified properties.
     *
     * @param color The RGB color of the sphere (values 0-255)
     * @param rotation The initial rotation in degrees around x, y, z axes
     * @param position The initial position in 3D space
     * @param scale The initial scale along x, y, z axes
     * @param radius The radius of the sphere
     */
    public Sphere(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, double radius) {
        super(color, rotation, position, scale);
        this.radius = radius;
    }

    /**
     * Gets the material of the sphere.
     *
     * @return The material properties of the sphere
     */
    @Override
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the sphere.
     *
     * @param material The new material properties to apply
     */
    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Calculates the surface area of the sphere.
     *
     * @return The surface area (4πr²)
     */
    public double getSurfaceArea() {
        return 4 * Math.PI * Math.pow(radius, 2);
    }

    /**
     * Calculates the volume of the sphere.
     *
     * @return The volume ((4/3)πr³)
     */
    public double getVolume() {
        return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
    }

    /**
     * Moves the sphere by a given offset vector.
     *
     * @param delta The translation vector to apply to the sphere's position
     */
    public void translate(Vector3D delta) {
        position.x += delta.x;
        position.y += delta.y;
        position.z += delta.z;
    }

    /**
     * Scales the sphere uniformly by a given factor.
     *
     * @param factor The scaling factor to apply to the radius
     */
    public void scale(double factor) {
        this.radius *= factor;
    }

    /**
     * Gets the radius of the sphere.
     *
     * @return The current radius of the sphere
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the sphere.
     *
     * @param radius The new radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the position of the sphere in 3D space.
     *
     * @return The position vector of the sphere
     */
    @Override
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Calculates the normal vector at a given hit point on the sphere's surface.
     *
     * @param hit The point on the sphere's surface where the normal is needed
     * @return The normalized normal vector at the hit point
     */
    public Vector3D getNormal(Vector3D hit) {
        return hit.subtract(position).normalize();
    }
}