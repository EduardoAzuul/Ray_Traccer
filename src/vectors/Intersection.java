package vectors;

import Objects.*;
import static java.lang.Math.sqrt;

public class Intersection {
    // Check if a ray intersects with a sphere

    public static Double intersect(Ray ray,Sphere sphere) {
        Point oc = ray.getOrigin().subtract(sphere.position);
        Point dir = ray.getDirection();

        double a = dir.dot(dir);
        double b = 2.0 * oc.dot(dir);
        double c = oc.dot(oc) - sphere.getRadius()* sphere.getRadius();

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null; // No hay intersección
        }

        // Calcular la intersección más cercana
        double t = (-b - Math.sqrt(discriminant)) / (2.0 * a);

        if (t < 0) {
            t = (-b + Math.sqrt(discriminant)) / (2.0 * a);
            if (t < 0) {
                return null; // Las intersecciones están detrás del rayo
            }
        }

        return t;
    }


}
