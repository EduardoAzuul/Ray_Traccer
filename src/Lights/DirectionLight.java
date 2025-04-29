package Lights;

import vectors.Vector3D;

public class DirectionLight extends Light {
    double radius;
    Vector3D direction;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public DirectionLight(Vector3D position, Vector3D color, double intensity, double radius, Vector3D direction) {
        super(position, color, intensity);
        setRadius(radius);
        setDirection(direction);
    }

}
