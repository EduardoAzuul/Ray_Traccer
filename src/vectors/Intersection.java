package vectors;

import Objects.Cube;
import Objects.ObjObject;
import Objects.Sphere;
import Objects.Triangle;

import java.util.List;

import static java.lang.Math.sqrt;

/**
 * Handles ray intersection calculations with various geometric objects.
 */
public class Intersection {
    private static final double DEFAULT_EPSILON = 1e-8;

    /**
     * Checks if a ray intersects with a sphere.
     *
     * @param ray       The ray to test intersection with
     * @param sphere    The sphere to test intersection with
     * @param nearPlane The minimum valid distance
     * @param farPlane  The maximum valid distance
     * @return The distance to intersection point, or -1.0 if no intersection
     */
    public static double sphere(Ray ray, Sphere sphere, double nearPlane, double farPlane) {
        Vector3D rayOrigin = ray.getOrigin();
        Vector3D sphereCenter = sphere.getPosition();
        Vector3D rayDir = ray.getDirection().normalize();
        double radius = sphere.getRadius();

        Vector3D oc = rayOrigin.subtract(sphereCenter);

        // Since rayDir is normalized, a = 1.0
        double b = 2.0 * oc.dot(rayDir);
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * c;

        if (discriminant < 0) {
            return -1.0; // No intersection
        }

        double sqrtDiscriminant = sqrt(discriminant);
        double t1 = (-b - sqrtDiscriminant) / 2.0;
        double t2 = (-b + sqrtDiscriminant) / 2.0;

        // Return closest valid intersection
        if (t1 > nearPlane && t1 < farPlane) {
            return t1;
        }
        if (t2 > nearPlane && t2 < farPlane) {
            return t2;
        }

        return -1.0;
    }

    /**
     * Checks if a ray intersects with a triangle using the Möller–Trumbore algorithm.
     * Takes into account which side of the triangle is being hit.
     *
     * @param ray The ray to test intersection with
     * @param triangle The triangle to test intersection with
     * @param nearPlane The minimum valid distance
     * @param farPlane The maximum valid distance
     * @param epsilon Tolerance value for floating point comparisons
     * @return The distance to intersection point, or -1.0 if no intersection
     */
    public static double triangle(Ray ray, Triangle triangle, double nearPlane, double farPlane, double epsilon) {
        // Vértices del triángulo
        Vector3D v0 = triangle.getVertex1();
        Vector3D v1 = triangle.getVertex2();
        Vector3D v2 = triangle.getVertex3();

        // Origen y dirección del rayo (normalizada)
        Vector3D O = ray.getOrigin();
        Vector3D D = ray.getDirection().normalize();

        // Edges (bordes) del triángulo siguiendo tu fórmula: v2v0 y v1v0
        Vector3D v2v0 = v2.subtract(v0);
        Vector3D v1v0 = v1.subtract(v0);

        // P = D x v1v0
        Vector3D P = D.cross(v1v0);

        // Determinante = v2v0 . P
        double determinant = v2v0.dot(P);
        double invDet = 1.0 / determinant;

        // Si el determinante es muy cercano a cero, no hay intersección (ray paralelo al triángulo)
        if (Math.abs(determinant) < 1e-5) {
            return -1.0;
        }

        // Vector desde el vértice v0 al origen del rayo
        Vector3D T = O.subtract(v0);

        // u = invDet * (T . P)
        double u = invDet * T.dot(P);

        // Validación de u
        if (u < 0.0 || u > 1.0) {
            return -1.0;
        }

        // Q = T x v2v0
        Vector3D Q = T.cross(v2v0);

        // v = invDet * (D . Q)
        double v = invDet * D.dot(Q);

        // Validación de v y de (u + v)
        if (v < 0.0 || (u + v) > (1.0 + epsilon)) {
            return -1.0;
        }

        // t = invDet * (Q . v1v0)
        double t = invDet * Q.dot(v1v0);

        // Verificar que t esté dentro del rango del plano cercano y lejano
        if (t < nearPlane || t > farPlane) {
            return -1.0;
        }

        // Retornar la distancia t (intersección encontrada)
        return t;
    }


    /**
     * Checks if a ray intersects with an OBJ object consisting of multiple triangles.
     *
     * @param ray       The ray to test intersection with
     * @param objObject The OBJ object to test intersection with
     * @param nearPlane The minimum valid distance
     * @param farPlane  The maximum valid distance
     * @param epsilon   Tolerance value for floating point comparisons
     * @return The distance to closest intersection point, or -1.0 if no intersection
     */
    public static double obj(Ray ray, ObjObject objObject, double nearPlane, double farPlane, double epsilon) {
        List<Triangle> triangleList = objObject.getTriangleList();
        double closestT = Double.MAX_VALUE;
        boolean hasIntersection = false;

        if (Intersection.cube(ray, objObject.getCube())) {  //Sí intersecta con el AABB
            for (Triangle triangle : triangleList) {
                double t = Intersection.triangle(ray, triangle, nearPlane, farPlane, epsilon);

                if (t > 0 && t < closestT) {
                    closestT = t;
                    hasIntersection = true;
                }
            }
        }
        return hasIntersection ? closestT : -1.0;
    }

    public static Triangle objTriangleIntersected(Ray ray, ObjObject objObject, double nearPlane, double farPlane, double epsilon) {
        List<Triangle> triangleList = objObject.getTriangleList();
        double closestT = Double.MAX_VALUE;
        boolean hasIntersection = false;
        Triangle triangleHit = null;

        if (Intersection.cube(ray, objObject.getCube())) {  //Sí intersecta con el AABB
            for (Triangle triangle : triangleList) {
                double t = Intersection.triangle(ray, triangle, nearPlane, farPlane, epsilon);

                if (t > 0 && t < closestT) {
                    closestT = t;
                    hasIntersection = true;
                    triangleHit = triangle;
                }
            }
        }
        return hasIntersection ? triangleHit : null;
    }


    public static boolean cube(Ray ray, Cube cube) {
        Vector3D rayOrigin = ray.getOrigin();
        Vector3D rayDirection = ray.getDirection();

        Vector3D min = cube.getMin();
        Vector3D max = cube.getMax();

        double tMin = (min.getX() - rayOrigin.getX()) / rayDirection.getX();
        double tMax = (max.getX() - rayOrigin.getX()) / rayDirection.getX();

        if (tMin > tMax) {
            double temp = tMin;
            tMin = tMax;
            tMax = temp;
        }

        double tyMin = (min.getY() - rayOrigin.getY()) / rayDirection.getY();
        double tyMax = (max.getY() - rayOrigin.getY()) / rayDirection.getY();

        if (tyMin > tyMax) {
            double temp = tyMin;
            tyMin = tyMax;
            tyMax = temp;
        }

        if ((tMin > tyMax) || (tyMin > tMax))
            return false;

        if (tyMin > tMin)
            tMin = tyMin;
        if (tyMax < tMax)
            tMax = tyMax;

        double tzMin = (min.getZ() - rayOrigin.getZ()) / rayDirection.getZ();
        double tzMax = (max.getZ() - rayOrigin.getZ()) / rayDirection.getZ();

        if (tzMin > tzMax) {
            double temp = tzMin;
            tzMin = tzMax;
            tzMax = temp;
        }

        if ((tMin > tzMax) || (tzMin > tMax))
            return false;

        if (tzMin > tMin)
            tMin = tzMin;
        if (tzMax < tMax)
            tMax = tzMax;

        return tMax >= Math.max(tMin, 0.0);
    }

}