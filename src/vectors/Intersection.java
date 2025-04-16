package vectors;

import Objects.*;
import Objects.ObjObject;

import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Intersection {
    // Check if a ray intersects with a sphere
    public static double sphere(Ray ray, Sphere sphere, double nearPlane, double farPlane) {
        Vector3D o = ray.getOrigin();
        Vector3D c = sphere.getPosition();
        Vector3D dir = ray.getDirection().normalized();  // Ensure direction is normalized

        Vector3D L = c.subtract(o);
        double tca = L.dot(dir);
        double d2 = L.dot(L) - tca * tca; // Squared distance from sphere center to ray
        double radius2 = pow(sphere.getRadius(), 2);

        if (d2 > radius2) {
            return -1.0; // No intersection
        }

        double thc = sqrt(radius2 - d2);
        double t0 = tca - thc;
        double t1 = tca + thc;

        double minDis = (t0 < t1) ? t0 : t1;

        if (minDis < nearPlane) {
            return -1.0;
        }

        // Ensure t0 is the nearest valid intersection
        if (t0 > nearPlane && t0 < farPlane && t0 > 0) {
            return t0;
        }

        // If t0 is out of bounds, check t1
        if (t1 > nearPlane && t1 < farPlane && t1 > 0) {
            return t1;
        }

        return -1.0; // No valid intersection within the range
    }

    public static double triangle(Ray ray, Triangle triangle, double nearPlane, double farPlane, double epsilon) {
        // Apply position transformation to triangle vertices (i.e., move the vertices based on the object's position)
        Vector3D v0 = triangle.getVertex1().add(triangle.getOrigin());  // Apply position (translation)
        Vector3D v1 = triangle.getVertex2().add(triangle.getOrigin());
        Vector3D v2 = triangle.getVertex3().add(triangle.getOrigin());

        Vector3D o = ray.getOrigin();
        Vector3D D = ray.getDirection().normalized();

        Vector3D edge1 = v1.subtract(v0);
        Vector3D edge2 = v2.subtract(v0);
        Vector3D P = D.cross(edge2);
        double determinant = edge1.dot(P);

        // If ray is parallel to triangle, no intersection
        if (Math.abs(determinant) < epsilon) {
            return -1.0;
        }

        double invDet = 1.0 / determinant;
        Vector3D T = o.subtract(v0);
        double u = T.dot(P) * invDet;

        // Check if u is outside [0, 1]
        if (u < 0.0 || u > 1.0 + epsilon) {
            return -1.0;
        }

        Vector3D Q = T.cross(edge1);
        double v = D.dot(Q) * invDet;

        // Check if v is outside [0, 1] or u + v exceeds 1
        if (v < 0.0 || (u + v) > 1.0 + epsilon) {
            return -1.0;
        }

        double t = edge2.dot(Q) * invDet;

        // Check if t is within the ray's valid range
        if (t < nearPlane || t > farPlane) {
            return -1.0;
        }

        return t;
    }

    // Intersection for the object from an OBJ file
    public static double obj(Ray ray, ObjObject objObject, double nearPlane, double farPlane, double epsilon) {

        List<Triangle> triangleList = objObject.getTriangleList();
        double closestT = Double.MAX_VALUE;

        for (Triangle triangle : triangleList) {
            // Apply movement (position) to each triangle's vertices
            double t = triangle(ray, triangle, nearPlane, farPlane, epsilon);

            if (t > nearPlane && t < closestT) {
                closestT = t;
            }
        }

        if (closestT < Double.MAX_VALUE && closestT < farPlane) {
            return closestT;
        }

        return -1.0;
    }
}
