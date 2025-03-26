package Objects;

import vectors.Point;

public abstract class Object3D {
    public Point scale = new Point(0,0,0);
    public Point position = new Point(0,0,0);
    public Point rotation = new Point(0,0,0);
    public Point color = new Point(0,255,0);


    public Object3D(Point color, Point rotation, Point position, Point scale) {
        this.color = color;
        this.rotation = rotation;
        this.position = position;
        this.scale = scale;
    }

    public Point getColor() {
        return color;
    }


    public int getColorRed(){
        int r = (int) color.x;
        return r;
    }
    public int getColorGreen(){
        int g = (int) color.y;
        return g;
    }
    public int getColorBlue(){
        int b = (int) color.z;
        return b;
    }

    public int getColorInt(){
        int r = (int) color.x;  // Red component
        int g = (int) color.y;  // Green component
        int b = (int) color.z;  // Blue component
        int rgb = (r << 16) + (g << 8) + b; //Moves the bits of the int to make them go into their respective position
        return rgb;
    }

}
