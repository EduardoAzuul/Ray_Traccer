package vectors;

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D(Vector3D vector) {
        setX(vector.getX());
        setY(vector.getY());
        setZ(vector.getZ());
    }

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


    public double getComponent(int index) {
        switch (index) {
            case 0: return getX();
            case 1: return getY();
            case 2: return getZ();
            default:
                throw new IndexOutOfBoundsException("Index must be 0 (x), 1 (y), or 2 (z)");
        }
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
        double y = getZ() * p.getX() - getX() * p.getZ();
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
    public Vector3D normalize() {
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

    public Vector3D scale(Vector3D scale) {
        return new Vector3D(getX() * scale.getX(), getY() * scale.getY(), getZ() * scale.getZ());
    }

    public Vector3D scale(double scale) {
        return new Vector3D(getX() * scale, getY() * scale, getZ() * scale);
    }


    public double length() {
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
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

    public Vector3D rotateVector(Vector3D rotation) {
        double radX = Math.toRadians(rotation.getX());
        double radY = Math.toRadians(rotation.getY());
        double radZ = Math.toRadians(rotation.getZ());

        // Rotation around X axis
        double cosX = Math.cos(radX);
        double sinX = Math.sin(radX);
        double y1 = getY() * cosX - getZ() * sinX;
        double z1 = getY() * sinX + getZ() * cosX;

        // Rotation around Y axis
        double cosY = Math.cos(radY);
        double sinY = Math.sin(radY);
        double x2 = getX() * cosY + z1 * sinY;
        double z2 = -getX() * sinY + z1 * cosY;

        // Rotation around Z axis
        double cosZ = Math.cos(radZ);
        double sinZ = Math.sin(radZ);
        double x3 = x2 * cosZ - y1 * sinZ;
        double y3 = x2 * sinZ + y1 * cosZ;

        return new Vector3D(x3, y3, z2);
    }


    @Override
    public String toString() {
        return "Point: " +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() ;
    }

    public static int vectorToInt(Vector3D vector) {
        return (int) vector.getX()<<16 + (int) vector.getY()<<8 + (int) vector.getZ();
    }

    public int getRGB() {
        return ((int) getX() << 16) | ((int) getY() << 8) | (int) getZ();
    }

}
