package Lights;

import vectors.Vector3D;

public class PointLight extends Light {
        // Scalar multiplier (e.g., 1.0 = full intensity)

    public PointLight(Vector3D position, Vector3D color, double intensity) {
        super(position, color, intensity);
    }


    //Computes the light direction vector from a surface point to this light.
    public Vector3D getDirectionFrom(Vector3D surfacePoint) {
        return position.subtract(surfacePoint).normalize();
    }


    //Computes the distance from a surface point to this light.

    public double getDistanceFrom(Vector3D surfacePoint) {
        return position.subtract(surfacePoint).magnitude();
    }


}
