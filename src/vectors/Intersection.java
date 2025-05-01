package vectors;

import Objects.*;
import Objects.WrapperBoxes.*;

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
        // Triangle vertices
        Vector3D v0 = triangle.getVertex1();
        Vector3D v1 = triangle.getVertex2();
        Vector3D v2 = triangle.getVertex3();

        // Ray origin and direction (normalized)
        Vector3D O = ray.getOrigin();
        Vector3D D = ray.getDirection().normalize();

        // Edges of the triangle
        Vector3D v2v0 = v2.subtract(v0);
        Vector3D v1v0 = v1.subtract(v0);

        // P = D x v1v0
        Vector3D P = D.cross(v1v0);

        // Determinant = v2v0 . P
        double determinant = v2v0.dot(P);
        double invDet = 1.0 / determinant;

        // If determinant is near zero, ray is parallel to triangle plane
        if (Math.abs(determinant) < 1e-5) {
            return -1.0;
        }

        // Vector from vertex v0 to ray origin
        Vector3D T = O.subtract(v0);

        // Calculate u parameter
        double u = invDet * T.dot(P);

        // Check u bounds
        if (u < 0.0 || u > 1.0) {
            return -1.0;
        }

        // Q = T x v2v0
        Vector3D Q = T.cross(v2v0);

        // Calculate v parameter
        double v = invDet * D.dot(Q);

        // Check v bounds and u+v
        if (v < 0.0 || (u + v) > (1.0 + epsilon)) {
            return -1.0;
        }

        // Calculate t
        double t = invDet * Q.dot(v1v0);

        // Check if t is within ray range
        if (t < nearPlane || t > farPlane) {
            return -1.0;
        }

        return t;
    }

    /**
     * Checks if a ray intersects with an OBJ object using BVH acceleration.
     *
     * @param ray       The ray to test intersection with
     * @param objObject The OBJ object to test intersection with
     * @param nearPlane The minimum valid distance
     * @param farPlane  The maximum valid distance
     * @param epsilon   Tolerance value for floating point comparisons
     * @return The distance to closest intersection point, or -1.0 if no intersection
     */
    public static double obj(Ray ray, ObjObject objObject, double nearPlane, double farPlane, double epsilon) {
        // First check against the root AABB
        if (!cube(ray, objObject.getCube())) {
            return -1.0;
        }

        // Now traverse the BVH
        BVHNode root = objObject.getBvhRoot();
        if (root == null) {
            return -1.0; // No BVH built
        }

        return traverseBVHIterative(ray, root, objObject, nearPlane, farPlane, epsilon);
    }

    /**
     * Gets the specific triangle intersected by a ray in an OBJ object using optimized BVH traversal.
     */
    public static Triangle objTriangleIntersected(Ray ray, ObjObject objObject,
                                                  double nearPlane, double farPlane, double epsilon) {
        // First check against the root AABB
        if (!cube(ray, objObject.getCube())) {
            return null;
        }

        // Now traverse the BVH
        BVHNode root = objObject.getBvhRoot();
        if (root == null) {
            return null; // No BVH built
        }

        return traverseBVHForTriangleIterative(ray, root, objObject, nearPlane, farPlane, epsilon);
    }

    /**
     * Checks if a ray intersects with a cube/AABB.
     *
     * @param ray  The ray to test intersection with
     * @param cube The cube to test intersection with
     * @return true if intersection occurs, false otherwise
     */
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

    /**
     * Traverses the BVH to find the closest triangle intersection.
     */


    /**
     * Traverses the BVH iteratively to find the closest triangle intersection.
     * This avoids stack overflow and allows for early termination.
     */
    private static double traverseBVHIterative(Ray ray, BVHNode root, ObjObject objObject,
                                               double nearPlane, double farPlane, double epsilon) {
        // Stack-based traversal to avoid recursion
        // Using a simple array as stack with max size to avoid dynamic allocation
        BVHNode[] nodeStack = new BVHNode[64]; // Adjust size based on expected BVH depth
        double[] distStack = new double[64];   // Distance to node's AABB
        int stackSize = 0;

        // Start with root node
        nodeStack[stackSize] = root;
        distStack[stackSize] = 0.0;
        stackSize++;

        double closestT = Double.MAX_VALUE;

        while (stackSize > 0) {
            // Pop node from stack
            stackSize--;
            BVHNode node = nodeStack[stackSize];
            double nodeEntryDist = distStack[stackSize];

            // Skip this node if we already found a closer intersection
            if (nodeEntryDist >= closestT) {
                continue;
            }

            if (node.isLeaf()) {
                // Leaf node - check all triangles
                BVHLeafNode leaf = (BVHLeafNode) node;

                for (int index : leaf.getTriangleIndices()) {
                    Triangle triangle = objObject.getTriangleList().get(index);
                    double t = triangle(ray, triangle, nearPlane, Math.min(farPlane, closestT), epsilon);

                    if (t > 0 && t < closestT) {
                        closestT = t;
                    }
                }
            } else {
                // Internal node - push children to stack
                BVHInternalNode internal = (BVHInternalNode) node;
                BVHNode left = internal.getLeftChild();
                BVHNode right = internal.getRightChild();

                // Calculate distances to child nodes
                double leftDist = getNodeEntryDistance(ray, left);
                double rightDist = getNodeEntryDistance(ray, right);

                // Push children to stack in far-to-near order
                // so we process nearer nodes first
                if (leftDist < 0 && rightDist < 0) {
                    // No intersection with either child
                    continue;
                }

                if (leftDist < 0) {
                    // Only right child intersects
                    if (rightDist < closestT) {
                        nodeStack[stackSize] = right;
                        distStack[stackSize] = rightDist;
                        stackSize++;
                    }
                } else if (rightDist < 0) {
                    // Only left child intersects
                    if (leftDist < closestT) {
                        nodeStack[stackSize] = left;
                        distStack[stackSize] = leftDist;
                        stackSize++;
                    }
                } else {
                    // Both children intersect, push farther one first
                    if (leftDist > rightDist) {
                        // Right is closer, push left first
                        if (leftDist < closestT) {
                            nodeStack[stackSize] = left;
                            distStack[stackSize] = leftDist;
                            stackSize++;
                        }
                        if (rightDist < closestT) {
                            nodeStack[stackSize] = right;
                            distStack[stackSize] = rightDist;
                            stackSize++;
                        }
                    } else {
                        // Left is closer, push right first
                        if (rightDist < closestT) {
                            nodeStack[stackSize] = right;
                            distStack[stackSize] = rightDist;
                            stackSize++;
                        }
                        if (leftDist < closestT) {
                            nodeStack[stackSize] = left;
                            distStack[stackSize] = leftDist;
                            stackSize++;
                        }
                    }
                }
            }
        }

        return closestT < Double.MAX_VALUE ? closestT : -1.0;
    }

    /**
     * Traverses the BVH iteratively to find the specific intersected triangle.
     */
    private static Triangle traverseBVHForTriangleIterative(Ray ray, BVHNode root, ObjObject objObject,
                                                            double nearPlane, double farPlane, double epsilon) {
        // Stack-based traversal
        BVHNode[] nodeStack = new BVHNode[64];
        double[] distStack = new double[64];
        int stackSize = 0;

        // Start with root node
        nodeStack[stackSize] = root;
        distStack[stackSize] = 0.0;
        stackSize++;

        double closestT = Double.MAX_VALUE;
        Triangle closestTriangle = null;

        while (stackSize > 0) {
            // Pop node from stack
            stackSize--;
            BVHNode node = nodeStack[stackSize];
            double nodeEntryDist = distStack[stackSize];

            // Skip this node if we already found a closer intersection
            if (nodeEntryDist >= closestT) {
                continue;
            }

            if (node.isLeaf()) {
                // Leaf node - check all triangles
                BVHLeafNode leaf = (BVHLeafNode) node;

                for (int index : leaf.getTriangleIndices()) {
                    Triangle triangle = objObject.getTriangleList().get(index);
                    double t = triangle(ray, triangle, nearPlane, Math.min(farPlane, closestT), epsilon);

                    if (t > 0 && t < closestT) {
                        closestT = t;
                        closestTriangle = triangle;
                    }
                }
            } else {
                // Internal node - push children to stack
                BVHInternalNode internal = (BVHInternalNode) node;
                BVHNode left = internal.getLeftChild();
                BVHNode right = internal.getRightChild();

                // Calculate distances to child nodes
                double leftDist = getNodeEntryDistance(ray, left);
                double rightDist = getNodeEntryDistance(ray, right);

                // Push children to stack in far-to-near order
                if (leftDist < 0 && rightDist < 0) {
                    continue;
                }

                if (leftDist < 0) {
                    if (rightDist < closestT) {
                        nodeStack[stackSize] = right;
                        distStack[stackSize] = rightDist;
                        stackSize++;
                    }
                } else if (rightDist < 0) {
                    if (leftDist < closestT) {
                        nodeStack[stackSize] = left;
                        distStack[stackSize] = leftDist;
                        stackSize++;
                    }
                } else {
                    if (leftDist > rightDist) {
                        if (leftDist < closestT) {
                            nodeStack[stackSize] = left;
                            distStack[stackSize] = leftDist;
                            stackSize++;
                        }
                        if (rightDist < closestT) {
                            nodeStack[stackSize] = right;
                            distStack[stackSize] = rightDist;
                            stackSize++;
                        }
                    } else {
                        if (rightDist < closestT) {
                            nodeStack[stackSize] = right;
                            distStack[stackSize] = rightDist;
                            stackSize++;
                        }
                        if (leftDist < closestT) {
                            nodeStack[stackSize] = left;
                            distStack[stackSize] = leftDist;
                            stackSize++;
                        }
                    }
                }
            }
        }

        return closestTriangle;
    }

    /**
     * Helper method to calculate the entry distance to a node's bounding box.
     * Returns -1.0 if no intersection.
     */
    private static double getNodeEntryDistance(Ray ray, BVHNode node) {
        Vector3D rayOrigin = ray.getOrigin();
        Vector3D rayDirection = ray.getDirection();

        Cube box = node.getBoundingBox();
        Vector3D min = box.getMin();
        Vector3D max = box.getMax();

        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        // X axis slab
        if (Math.abs(rayDirection.getX()) < 1e-8) {
            // Ray is parallel to slab
            if (rayOrigin.getX() < min.getX() || rayOrigin.getX() > max.getX()) {
                return -1.0;
            }
        } else {
            double invD = 1.0 / rayDirection.getX();
            double t1 = (min.getX() - rayOrigin.getX()) * invD;
            double t2 = (max.getX() - rayOrigin.getX()) * invD;

            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax) {
                return -1.0;
            }
        }

        // Y axis slab
        if (Math.abs(rayDirection.getY()) < 1e-8) {
            if (rayOrigin.getY() < min.getY() || rayOrigin.getY() > max.getY()) {
                return -1.0;
            }
        } else {
            double invD = 1.0 / rayDirection.getY();
            double t1 = (min.getY() - rayOrigin.getY()) * invD;
            double t2 = (max.getY() - rayOrigin.getY()) * invD;

            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax) {
                return -1.0;
            }
        }

        // Z axis slab
        if (Math.abs(rayDirection.getZ()) < 1e-8) {
            if (rayOrigin.getZ() < min.getZ() || rayOrigin.getZ() > max.getZ()) {
                return -1.0;
            }
        } else {
            double invD = 1.0 / rayDirection.getZ();
            double t1 = (min.getZ() - rayOrigin.getZ()) * invD;
            double t2 = (max.getZ() - rayOrigin.getZ()) * invD;

            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax) {
                return -1.0;
            }
        }

        // Return entry distance, but ensure it's not behind the ray
        return tMin < 0 && tMax > 0 ? 0.0 : tMin;
    }
}