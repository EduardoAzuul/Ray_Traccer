package vectors;

public class Ray {
    private Vector3D origin;
    private Vector3D direction;


    public Ray(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction.normalize(); // Ensure direction is normalized
    }

    public Vector3D getPoint(double t) {
        return origin.add(direction.multiplyByScalar(t));
    }


    public Vector3D getOrigin() {
        return origin;
    }


    public Vector3D getDirection() {
        return direction;
    }


}