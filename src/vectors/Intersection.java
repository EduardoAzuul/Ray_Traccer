package vectors;

import Objects.*;
import org.w3c.dom.DOMImplementation;

import java.awt.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Intersection {
    // Check if a ray intersects with a sphere
    public static double sphere(Ray ray, Sphere sphere, double nearPlane, double farPlane) {
        Vector3D o = ray.getOrigin().subtract(sphere.position);
        Vector3D dir = ray.getDirection();
        dir.normalize();
        Vector3D c = sphere.getPosition();

        Vector3D L = c.subtract(o);
        double tca = L.dot(dir);
        double d = sqrt(L.dot(L) - pow(tca, 2)); // Fixed d calculation

        if (d > sphere.getRadius()) { // Correct collision check
            return -1.0;
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
            return -1.0;
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

        return -1.0; // No valid intersection within the range
    }

    public static double triangle(Ray ray, Triangle triangle, double nearPlane, double farPlane, double epsilon) {
        Vector3D v0 = triangle.getVertex1();
        Vector3D v1 = triangle.getVertex2();
        Vector3D v2 = triangle.getVertex3();
        Vector3D o = ray.getOrigin();
        Vector3D D = ray.getDirection();
        D.normalize();

        Vector3D v2v0 = v2.subtract(v0);
        Vector3D v1v0 = v1.subtract(v0);
        Vector3D P = D.cross(v1v0);
        double determinant = v2v0.dot(P);

        // If ray is parallel to triangle, no intersection
        if (Math.abs(determinant) < epsilon) {
            return -1.0;
        }

        double invDet = 1.0 / determinant;
        Vector3D T = o.subtract(v0);
        double u = T.dot(P) * invDet;

        // Check if u is outside [0, 1] (epsilon only for floating-point tolerance)
        if (u < 0.0 || u > 1.0 + epsilon) {
            return -1.0;
        }

        Vector3D Q = T.cross(v2v0);
        double v = D.dot(Q) * invDet;

        // Check if v is outside [0, 1] or u + v exceeds 1 (with epsilon tolerance)
        if (v < 0.0 || (u + v) > 1.0 + epsilon) {
            return -1.0;
        }

        double t = Q.dot(v1v0) * invDet;

        // Check if t is within the ray's valid range [nearPlane, farPlane]
        if (t < nearPlane || t > farPlane) {
            return -1.0;
        }

        return t;
    }
}
