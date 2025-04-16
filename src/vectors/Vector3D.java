package vectors;

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    // Constructor
    public Vector3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public Vector3D() {
        setX(0);
        setY(0);
        setZ(0);
    }

    // Dot product (returns scalar)
    public double dot(Vector3D p) {
        return (
            getX() * p.getX() +
            getY() * p.getY() +
            getZ() * p.getZ()
        );
    }

    // Cross product (returns new Point)
    public Vector3D cross(Vector3D p) {
        double x = getY() * p.getZ() - getZ() * p.getY();
        double y = getZ() * p.getX() - getX() * getZ();
        double z = getX() * p.getY() - getY() * p.getX();
        return new Vector3D(x, y, z);
    }

    // Magnitude of the point (interpreted as a vector)
    public double magnitude() {
        return Math.sqrt(
            Math.pow(getX(),2) +
            Math.pow(getY(),2) +
            Math.pow(getZ(),2)
        );
    }

    // Normalize the point (turn it into a unit vector)
    public Vector3D normalized() {
        double mag = magnitude();
        if (mag == 0) return new Vector3D(0, 0, 0);
        return new Vector3D(getX() / mag, getY() / mag, getZ() / mag);
    }

    public static Vector3D randomColor() {
        double red = getRandomNumber(0,255);
        double green = getRandomNumber(0,255);
        double blue = getRandomNumber(0,255);
        return new Vector3D(red, green, blue);
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // Angle amplitude (angle with respect to the X axis in radians)
    public double amplitude() {
        return Math.acos(this.x / magnitude());
    }

    // Add two points (vector sum)
    public Vector3D add(Vector3D p) {
        return new Vector3D(this.x + p.x, this.y + p.y, this.z + p.z);
    }

    // Subtract two points (vector difference)
    public Vector3D subtract(Vector3D p) {
        return new Vector3D(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    // Scalar multiplication (multiply the point by a scalar)
    public Vector3D multiplyByScalar(double scalar) {
        return new Vector3D(getX() * scalar, getY() * scalar, getZ() * scalar);
    }


    @Override
    public String toString() {
        return "Point: " +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() ;
    }
}
