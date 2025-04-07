package vectors;

public class Ray {
    public Vector3D origin;
    public Vector3D end;
    public double hAngle;
    public double vAngle;
    public double distance;
    public Vector3D direction;


    // Constructor
    public Ray(Vector3D origin, double hAngle, double vAngle) {
        this.origin = origin;
        this.hAngle = hAngle;
        this.vAngle = vAngle;
        this.distance = 0;
    }


    public Ray(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction= direction;

    }


    public void normalize(Vector3D p) {
        double mag = magnitud(p);
        if (mag != 0) {
            p.x /= mag;
            p.y /= mag;
            p.z /= mag;
        }
    }

    public double magnitud(Vector3D p) {
        return Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
    }

    //Define the angles given the origin and end of the point
    private void defineAngles() {
        double dx = end.x - origin.x;
        double dy = end.y - origin.y;
        double dz = end.z - origin.z;

        this.hAngle = Math.atan2(dz, dx);
        this.vAngle = Math.atan2(dy, Math.sqrt(dx * dx + dz * dz));
    }

    // Get the final point of the ray
    public Vector3D getPoint(int t) {
        double x = origin.x + t * Math.cos(vAngle) * Math.cos(hAngle);
        double y = origin.y + t * Math.sin(vAngle);
        double z = origin.z + t * Math.cos(vAngle) * Math.sin(hAngle);
        return new Vector3D(x, y, z);
    }

    // Get the origin of the ray
    public Vector3D getOrigin() {
        return origin;
    }

    // Get the direction of the ray as a unit vector
    public Vector3D getDirection() {
        return direction;
    }
}