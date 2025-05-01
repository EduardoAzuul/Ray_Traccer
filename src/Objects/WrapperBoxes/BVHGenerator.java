package Objects.WrapperBoxes;

import Objects.Cube;
import Objects.Triangle;
import vectors.Vector3D;
import java.util.ArrayList;
import java.util.List;

public class BVHGenerator {
    private List<Triangle> triangleList;
    private int maxBVHDepth;
    private final double marginFactor = 0.1; // 10% margin for boxes

    // Statistics properties
    private int minTrianglesInLeaf = Integer.MAX_VALUE;
    private int maxTrianglesInLeaf = 0;
    private int totalLeafNodes = 0;
    private int totalTrianglesInLeaves = 0;
    private int maxDepthReached = 0;
    private int totalNodes = 0;

    public BVHGenerator(List<Triangle> triangleList) {
        this.triangleList = triangleList;
        this.maxBVHDepth = calculateOptimalDepth(triangleList.size());
    }

    public BVHGenerator(List<Triangle> triangleList, int maxDepth) {
        this.triangleList = triangleList;
        this.maxBVHDepth = maxDepth > 0 ? maxDepth : calculateOptimalDepth(triangleList.size());
    }

    private int calculateOptimalDepth(int triangleCount) {
        int trianglesPerLeaf = 4; // Puedes ajustar este valor según rendimiento/precisión deseado
        int maxDepth = 16;
        int minDepth = 2;

        if (triangleCount <= trianglesPerLeaf) {
            return minDepth;
        }

        int estimatedDepth = (int) Math.ceil(Math.log(triangleCount / (double) trianglesPerLeaf) / Math.log(2));
        return Math.max(minDepth, Math.min(maxDepth, estimatedDepth));
    }


    public BVHNode buildBVH() {
        if (triangleList == null || triangleList.isEmpty()) return null;

        List<Integer> triangleIndices = new ArrayList<>();
        for (int i = 0; i < triangleList.size(); i++) {
            triangleIndices.add(i);
        }

        BVHNode root = buildBVHNode(triangleIndices, 0);

        // Print statistics after building
        printBVHStatistics();

        return root;
    }

    private BVHNode buildBVHNode(List<Integer> triangleIndices, int currentDepth) {
        if (triangleIndices.isEmpty()) {
            return null;
        }

        totalNodes++;
        maxDepthReached = Math.max(maxDepthReached, currentDepth);

        Cube nodeBoundingBox = calculateBoundingBox(triangleIndices);

        // If we've reached max depth or have few triangles, create leaf node
        if (currentDepth >= maxBVHDepth || triangleIndices.size() <= 4) {
            // Update leaf statistics
            updateLeafStatistics(triangleIndices.size());
            return new BVHLeafNode(nodeBoundingBox, new ArrayList<>(triangleIndices));
        }

        // Find the optimal split using SAH (Surface Area Heuristic) or simpler approaches
        BVHSplit bestSplit = findOptimalSplit(triangleIndices, nodeBoundingBox);

        // If no good split found, create leaf node
        if (bestSplit == null || bestSplit.cost >= triangleIndices.size() * 1.0) {
            updateLeafStatistics(triangleIndices.size());
            return new BVHLeafNode(nodeBoundingBox, new ArrayList<>(triangleIndices));
        }

        // Build children with the determined split
        BVHNode leftChild = buildBVHNode(bestSplit.leftIndices, currentDepth + 1);
        BVHNode rightChild = buildBVHNode(bestSplit.rightIndices, currentDepth + 1);

        return new BVHInternalNode(nodeBoundingBox, leftChild, rightChild);
    }

    private BVHSplit findOptimalSplit(List<Integer> triangleIndices, Cube nodeBoundingBox) {
        Vector3D dimensions = nodeBoundingBox.getDimensions();
        Vector3D min = nodeBoundingBox.getMin();
        double bestCost = Double.MAX_VALUE;
        BVHSplit bestSplit = null;

        // Get total surface area for cost calculation
        double totalArea = calculateSurfaceArea(nodeBoundingBox);

        // Number of split positions to test along each axis
        final int SPLIT_COUNT = 10;

        // Test splits along each axis
        for (int axis = 0; axis < 3; axis++) {
            double axisLength;
            switch (axis) {
                case 0: axisLength = dimensions.getX(); break;
                case 1: axisLength = dimensions.getY(); break;
                case 2: axisLength = dimensions.getZ(); break;
                default: axisLength = 0; break;
            }

            // Skip axes with minimal length
            if (axisLength < 1e-6) continue;

            // Test multiple split positions along the axis
            for (int splitPos = 1; splitPos < SPLIT_COUNT; splitPos++) {
                double splitValue = min.getComponent(axis) + (axisLength * splitPos / SPLIT_COUNT);

                // Split triangles based on their centroids
                List<Integer> leftIndices = new ArrayList<>();
                List<Integer> rightIndices = new ArrayList<>();

                for (int index : triangleIndices) {
                    Triangle triangle = triangleList.get(index);
                    Vector3D center = getTriangleCenter(triangle);

                    if (center.getComponent(axis) <= splitValue) {
                        leftIndices.add(index);
                    } else {
                        rightIndices.add(index);
                    }
                }

                // Skip unbalanced splits
                if (leftIndices.isEmpty() || rightIndices.isEmpty()) {
                    continue;
                }

                // Calculate bounding boxes and their costs
                Cube leftBox = calculateBoundingBox(leftIndices);
                Cube rightBox = calculateBoundingBox(rightIndices);

                double leftArea = calculateSurfaceArea(leftBox);
                double rightArea = calculateSurfaceArea(rightBox);

                // Simple SAH cost: areaL/areaTotal * nL + areaR/areaTotal * nR
                double cost = (leftArea / totalArea) * leftIndices.size() +
                        (rightArea / totalArea) * rightIndices.size();

                // Keep track of best split
                if (cost < bestCost) {
                    bestCost = cost;
                    bestSplit = new BVHSplit(leftIndices, rightIndices, leftBox, rightBox, cost);
                }
            }
        }

        return bestSplit;
    }

    private double calculateSurfaceArea(Cube box) {
        Vector3D dimensions = box.getDimensions();
        double x = dimensions.getX();
        double y = dimensions.getY();
        double z = dimensions.getZ();
        return 2.0 * (x * y + x * z + y * z);
    }

    private void updateLeafStatistics(int triangleCount) {
        minTrianglesInLeaf = Math.min(minTrianglesInLeaf, triangleCount);
        maxTrianglesInLeaf = Math.max(maxTrianglesInLeaf, triangleCount);
        totalTrianglesInLeaves += triangleCount;
        totalLeafNodes++;
    }

    private void printBVHStatistics() {
        System.out.println("\n--- BVH Statistics ---");
        System.out.println("Total triangles: " + triangleList.size());
        System.out.println("Total nodes: " + totalNodes);
        System.out.println("Total leaf nodes: " + totalLeafNodes);
        System.out.println("Maximum depth reached: " + maxDepthReached);
        System.out.println("Minimum triangles in a leaf: " + minTrianglesInLeaf);
        System.out.println("Maximum triangles in a leaf: " + maxTrianglesInLeaf);

        double avgTrianglesPerLeaf = totalLeafNodes > 0 ?
                (double) totalTrianglesInLeaves / totalLeafNodes : 0;
        System.out.println("Average triangles per leaf: " + String.format("%.2f", avgTrianglesPerLeaf));
        System.out.println("----------------------");
    }

    private int getLongestAxis(Vector3D dimensions) {
        int axis = 0;
        double maxDimension = dimensions.getX();

        if (dimensions.getY() > maxDimension) {
            axis = 1;
            maxDimension = dimensions.getY();
        }
        if (dimensions.getZ() > maxDimension) {
            axis = 2;
        }

        return axis;
    }

    private Cube calculateBoundingBox(List<Integer> triangleIndices) {
        if (triangleIndices.isEmpty()) {
            // Return a tiny default box if no triangles
            return new Cube(new Vector3D(0, 0, 0), new Vector3D(1e-6, 1e-6, 1e-6));
        }

        Vector3D min = new Vector3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Vector3D max = new Vector3D(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);

        for (int index : triangleIndices) {
            Triangle triangle = triangleList.get(index);
            Vector3D[] vertices = { triangle.getVertex1(), triangle.getVertex2(), triangle.getVertex3() };

            for (Vector3D v : vertices) {
                min = new Vector3D(
                        Math.min(min.getX(), v.getX()),
                        Math.min(min.getY(), v.getY()),
                        Math.min(min.getZ(), v.getZ())
                );
                max = new Vector3D(
                        Math.max(max.getX(), v.getX()),
                        Math.max(max.getY(), v.getY()),
                        Math.max(max.getZ(), v.getZ())
                );
            }
        }

        // Apply margin to bounding box
        Vector3D dimensions = new Vector3D(
                max.getX() - min.getX(),
                max.getY() - min.getY(),
                max.getZ() - min.getZ()
        );

        double marginX = dimensions.getX() * marginFactor;
        double marginY = dimensions.getY() * marginFactor;
        double marginZ = dimensions.getZ() * marginFactor;

        Vector3D newMin = new Vector3D(
                min.getX() - marginX,
                min.getY() - marginY,
                min.getZ() - marginZ
        );

        Vector3D newMax = new Vector3D(
                max.getX() + marginX,
                max.getY() + marginY,
                max.getZ() + marginZ
        );

        return new Cube(newMin, newMax);
    }

    private Vector3D getTriangleCenter(Triangle triangle) {
        Vector3D v1 = triangle.getVertex1();
        Vector3D v2 = triangle.getVertex2();
        Vector3D v3 = triangle.getVertex3();
        return new Vector3D(
                (v1.getX() + v2.getX() + v3.getX()) / 3.0,
                (v1.getY() + v2.getY() + v3.getY()) / 3.0,
                (v1.getZ() + v2.getZ() + v3.getZ()) / 3.0
        );
    }

    // Helper class to store split information
    private static class BVHSplit {
        List<Integer> leftIndices;
        List<Integer> rightIndices;
        Cube leftBox;
        Cube rightBox;
        double cost;

        BVHSplit(List<Integer> leftIndices, List<Integer> rightIndices,
                 Cube leftBox, Cube rightBox, double cost) {
            this.leftIndices = leftIndices;
            this.rightIndices = rightIndices;
            this.leftBox = leftBox;
            this.rightBox = rightBox;
            this.cost = cost;
        }
    }
}
