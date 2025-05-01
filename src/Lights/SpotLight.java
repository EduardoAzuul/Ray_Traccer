package Lights;

import vectors.Vector3D;

public class SpotLight extends Light {
    double radius;
    Vector3D direction;
    double angle;


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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public SpotLight(Vector3D position, Vector3D color, double intensity, double radius, double angle, Vector3D direction) {
        super(position, color, intensity);
        setRadius(radius);
        setDirection(direction);
        setAngle(angle);
    }

}
