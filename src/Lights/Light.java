package Lights;

import Objects.Object3D;
import vectors.Vector3D;

public abstract class Light {
    protected Vector3D position;
    protected Vector3D color;      // RGB values (0-255 or normalized 0.0-1.0 depending on your renderer)
    protected double intensity;

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getColor() {
        return color;
    }

    public int getColorint(){
        Vector3D color = getColor();
        int red = (int)Math.min(255, Math.max(0, color.x));
        int green = (int)Math.min(255, Math.max(0, color.y));
        int blue = (int)Math.min(255, Math.max(0, color.z));
        return (red<<16) + (green<<8) + blue;
    }

    public void setColor(Vector3D color) {
        this.color = color;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public Light(Vector3D position, Vector3D color, double intensity) {
        setPosition(position);
        setColor(color);
        setIntensity(intensity);
    }




    @Override
    public String toString() {
        return "PointLight{" +
                "position=" + position +
                ", color=" + color +
                ", intensity=" + intensity +
                '}';
    }
}
