package Lights;

import vectors.Vector3D;

public class DirectionalLight extends Light{
    private Vector3D Direction;

    public Vector3D getDirection() {
        return Direction;
    }

    public void setDirection(Vector3D direction) {
        Direction = direction;
    }

    public DirectionalLight(Vector3D color, double intensity, Vector3D direction) {
        super(new Vector3D(), color, intensity);
        setDirection(direction.normalize());
    }
}
