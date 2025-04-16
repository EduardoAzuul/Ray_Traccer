package Objects;

import vectors.Vector3D;

public abstract class Object3D {
    public Vector3D scale = new Vector3D(0, 0, 0);
    public Vector3D position = new Vector3D(0, 0, 0);
    public Vector3D rotation = new Vector3D(0, 0, 0);
    public Vector3D color = new Vector3D(0, 255, 0);


    public Object3D(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale) {
        this.color = color;
        this.rotation = rotation;
        this.position = position;
        this.scale = scale;
    }

    public Vector3D getColor() {
        return color;
    }


    public int getColorRed() {
        int r = (int) color.x;
        return r;
    }

    public int getColorGreen() {
        int g = (int) color.y;
        return g;
    }

    public int getColorBlue() {
        int b = (int) color.z;
        return b;
    }

    public int getColorInt() {
        int r = (int) color.x;  // Red component
        int g = (int) color.y;  // Green component
        int b = (int) color.z;  // Blue component
        int rgb = (r << 16) + (g << 8) + b; //Moves the bits of the int to make them go into their respective position
        return rgb;
    }

    public Vector3D getScale() {
        return scale;
    }

    public void setScale(Vector3D scale) {
        this.scale = scale;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
    }

    public void setColor(Vector3D color) {
        this.color = color;
    }
}
