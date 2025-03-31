package vectors;

import Objects.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Intersection {
    // Check if a ray intersects with a sphere
    public static Double intersect(Ray ray, Sphere sphere, double nearPlane, double farPlane) {
        Vector3D o = ray.getOrigin().subtract(sphere.position);
        Vector3D dir = ray.getDirection();
        dir.normalize();
        Vector3D c = sphere.getPosition();

        Vector3D L = c.subtract(o);
        Double tca = L.dot(dir);
        Double d = sqrt(L.dot(L) - pow(tca, 2)); // Fixed d calculation

        if (d > sphere.getRadius()) { // Correct collision check
            return null;
        }

        double rad = sphere.getRadius();
        double thc = sqrt(pow(rad, 2) - pow(d, 2));
        double t0 = tca - thc;
        double t1 = tca + thc;

        double minDis=0;
        if (t0 < t1){
            minDis=t0;
        }
        else{
            minDis=t1;
        }
        if (minDis<nearPlane){
            return null;
        }
        // Ensure t0 is the nearest valid intersection
        if (t0 > nearPlane && t0 < farPlane && t0 > 0) {
            System.out.println("Samallest distance is: "+t0);
            return t0;
        }

        // If t0 is out of bounds, check t1
        if (t1 > nearPlane && t1 < farPlane && t1 > 0) {
            System.out.println("Samallest distance is: "+t1);
            return t1;
        }

        return null; // No valid intersection within the range
    }
}
