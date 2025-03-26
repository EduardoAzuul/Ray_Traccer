package vectors;

public class Point {
    public double x;
    public double y;
    public double z;

    // Constructor
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Dot product (returns scalar)
    public double dot(Point p) {
        return this.x * p.x + this.y * p.y + this.z * p.z;
    }

    // Cross product (returns new Point)
    public Point cross(Point p) {
        double x = this.y * p.z - this.z * p.y;
        double y = this.z * p.x - this.x * p.z;
        double z = this.x * p.y - this.y * p.x;
        return new Point(x, y, z);
    }

    // Magnitude of the point (interpreted as a vector)
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    // Normalize the point (turn it into a unit vector)
    public void normalize() {
        double mag = magnitude();
        if (mag != 0) {
            this.x /= mag;
            this.y /= mag;
            this.z /= mag;
        }
    }

    // Angle amplitude (angle with respect to the X axis in radians)
    public double amplitude() {
        return Math.acos(this.x / magnitude());
    }

    // Add two points (vector sum)
    public Point add(Point p) {
        return new Point(this.x + p.x, this.y + p.y, this.z + p.z);
    }

    // Subtract two points (vector difference)
    public Point subtract(Point p) {
        return new Point(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    // Scalar multiplication (multiply the point by a scalar)
    public Point multiplyByScalar(double scalar) {
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }
}
