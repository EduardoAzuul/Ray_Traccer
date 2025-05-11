package Objects;

import vectors.Vector3D;

public class Sphere extends Object3D {
    private double radius;

    // Constructor
    public Sphere(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, double radius) {
        super(color, rotation, position, scale);
        this.radius = radius;

    }

    // Get the surface area of the sphere
    public double getSurfaceArea() {
        return 4 * Math.PI * Math.pow(radius, 2);
    }

    // Get the volume of the sphere
    public double getVolume() {
        return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
    }

    // Move the sphere by a given offset
    public void translate(Vector3D delta) {
        position.x += delta.x;
        position.y += delta.y;
        position.z += delta.z;
    }

    // Scale the sphere uniformly
    public void scale(double factor) {
        this.radius *= factor;
    }

    // Getters and Setters
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


    public Vector3D getPosition() {
        return position;
    }

}
