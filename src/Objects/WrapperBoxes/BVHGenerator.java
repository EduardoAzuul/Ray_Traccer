package Objects.WrapperBoxes;

import Objects.Cube;
import Objects.Triangle;
import vectors.Vector3D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Bounding Volume Hierarchy (BVH) generator that constructs a hierarchical structure
 * of bounding boxes for efficient spatial queries and collision detection.
 * @author José Eduardo Moreno Paredes
 * This implementation uses parallel processing with Fork/Join framework to optimize
 * construction time for large sets of triangles. The BVH is built using Surface Area
 * Heuristic (SAH) for optimal splitting decisions.
 */
public class BVHGenerator {
    /** List of triangles to build the BVH from */
    private List<Triangle> triangleList;

    /** Maximum depth of the BVH tree */
    private int maxBVHDepth;

    /** Margin factor (percentage) to add to bounding boxes */
    private final double marginFactor = 0.05; // 5% margin for boxes

    /** Maximum number of triangles allowed in a leaf node */
    private final int trianglesPerLeaf = 4;

    /** Number of split positions to test along each axis */
    private final int splitCount = 8;

    /** ForkJoinPool for parallel processing of BVH construction */
    private final ForkJoinPool forkJoinPool;

    // Thread-safe statistics counters
    private final AtomicInteger minTrianglesInLeaf = new AtomicInteger(Integer.MAX_VALUE);
    private final AtomicInteger maxTrianglesInLeaf = new AtomicInteger(0);
    private final AtomicInteger totalLeafNodes = new AtomicInteger(0);
    private final AtomicInteger totalTrianglesInLeaves = new AtomicInteger(0);
    private final AtomicInteger maxDepthReached = new AtomicInteger(0);
    private final AtomicInteger totalNodes = new AtomicInteger(0);

    /** Pre-computed centroids of all triangles for faster splitting */
    private Vector3D[] triangleCentroids;

    /**
     * Constructs a BVHGenerator with automatic depth calculation.
     *
     * @param triangleList List of triangles to build the BVH from
     */
    public BVHGenerator(List<Triangle> triangleList) {
        this.triangleList = triangleList;
        this.maxBVHDepth = calculateOptimalDepth(triangleList.size());
        this.forkJoinPool = new ForkJoinPool();
        precomputeCentroids();
    }

    /**
     * Constructs a BVHGenerator with specified maximum depth.
     *
     * @param triangleList List of triangles to build the BVH from
     * @param maxDepth Maximum depth of the BVH tree (if <= 0, calculates optimal depth)
     */
    public BVHGenerator(List<Triangle> triangleList, int maxDepth) {
        this.triangleList = triangleList;
        this.maxBVHDepth = maxDepth > 0 ? maxDepth : calculateOptimalDepth(triangleList.size());
        this.forkJoinPool = new ForkJoinPool();
        precomputeCentroids();
    }

    /**
     * Builds the BVH structure from the provided triangles.
     *
     * @return The root node of the constructed BVH, or null if no triangles are present
     */
    public BVHNode buildBVH() {
        if (triangleList == null || triangleList.isEmpty()) return null;

        List<Integer> triangleIndices = new ArrayList<>(triangleList.size());
        for (int i = 0; i < triangleList.size(); i++) {
            triangleIndices.add(i);
        }

        // Use ForkJoinPool to parallelize BVH construction
        BVHNode root = forkJoinPool.invoke(new BVHBuildTask(triangleIndices, 0));

        // Ensure we shut down the ForkJoinPool
        forkJoinPool.shutdown();

        return root;
    }

    /**
     * Pre-computes centroids of all triangles for faster splitting decisions.
     */
    private void precomputeCentroids() {
        triangleCentroids = new Vector3D[triangleList.size()];
        for (int i = 0; i < triangleList.size(); i++) {
            triangleCentroids[i] = triangleList.get(i).getCenter();
        }
    }

    /**
     * Calculates the optimal depth for the BVH tree based on triangle count.
     *
     * @param triangleCount Number of triangles in the scene
     * @return Optimal depth for the BVH tree
     */
    private int calculateOptimalDepth(int triangleCount) {
        int maxDepth = 16;
        int minDepth = 2;

        if (triangleCount <= this.trianglesPerLeaf) {
            return minDepth;
        }

        int estimatedDepth = (int) Math.ceil(Math.log(triangleCount / (double) this.trianglesPerLeaf) / Math.log(2));
        return Math.max(minDepth, Math.min(maxDepth, estimatedDepth));
    }

    /**
     * RecursiveTask for parallel BVH construction using Fork/Join framework.
     */
    private class BVHBuildTask extends RecursiveTask<BVHNode> {
        /** List of triangle indices to process in this task */
        private final List<Integer> triangleIndices;

        /** Current depth in the BVH tree */
        private final int currentDepth;

        /** Threshold for sequential processing (small subproblems) */
        private static final int SEQUENTIAL_THRESHOLD = 256;

        /**
         * Creates a new BVHBuildTask.
         *
         * @param triangleIndices List of triangle indices to process
         * @param currentDepth Current depth in the BVH tree
         */
        public BVHBuildTask(List<Integer> triangleIndices, int currentDepth) {
            this.triangleIndices = triangleIndices;
            this.currentDepth = currentDepth;
        }

        /**
         * The main computation performed by this task.
         *
         * @return The BVHNode constructed by this task
         */
        @Override
        protected BVHNode compute() {
            if (triangleIndices.isEmpty()) {
                return null;
            }

            totalNodes.incrementAndGet();
            maxDepthReached.updateAndGet(d -> Math.max(d, currentDepth));

            Cube nodeBoundingBox = calculateBoundingBox(triangleIndices);

            // If we've reached max depth or have few triangles, create leaf node
            if (currentDepth >= maxBVHDepth || triangleIndices.size() <= trianglesPerLeaf) {
                updateLeafStatistics(triangleIndices.size());
                return new BVHLeafNode(nodeBoundingBox, new ArrayList<>(triangleIndices));
            }

            // Find the optimal split
            BVHSplit bestSplit = findOptimalSplit(triangleIndices, nodeBoundingBox);

            // If no good split found, create leaf node
            if (bestSplit == null || bestSplit.cost >= triangleIndices.size() * 1.0) {
                updateLeafStatistics(triangleIndices.size());
                return new BVHLeafNode(nodeBoundingBox, new ArrayList<>(triangleIndices));
            }

            // Process children - only fork if the subproblems are large enough
            BVHBuildTask leftTask = new BVHBuildTask(bestSplit.leftIndices, currentDepth + 1);
            BVHBuildTask rightTask = new BVHBuildTask(bestSplit.rightIndices, currentDepth + 1);

            // For small tasks, just compute sequentially
            if (triangleIndices.size() <= SEQUENTIAL_THRESHOLD) {
                BVHNode leftChild = leftTask.compute();
                BVHNode rightChild = rightTask.compute();
                return new BVHInternalNode(nodeBoundingBox, leftChild, rightChild);
            } else {
                // For larger tasks, compute in parallel
                leftTask.fork();
                BVHNode rightChild = rightTask.compute();
                BVHNode leftChild = leftTask.join();
                return new BVHInternalNode(nodeBoundingBox, leftChild, rightChild);
            }
        }
    }

    /**
     * Finds the optimal split for a set of triangles using Surface Area Heuristic.
     *
     * @param triangleIndices List of triangle indices to split
     * @param nodeBoundingBox Bounding box of the current node
     * @return Best split found, or null if no good split exists
     */
    private BVHSplit findOptimalSplit(List<Integer> triangleIndices, Cube nodeBoundingBox) {
        Vector3D dimensions = nodeBoundingBox.getDimensions();
        Vector3D min = nodeBoundingBox.getMin();
        double bestCost = Double.MAX_VALUE;
        BVHSplit bestSplit = null;

        // Get total surface area for cost calculation
        double totalArea = calculateSurfaceArea(nodeBoundingBox);

        // Determine which axis to split based on the longest dimension
        int primaryAxis = getLongestAxis(dimensions);

        // First try the primary axis, then the others if needed
        for (int axisOffset = 0; axisOffset < 3; axisOffset++) {
            int axis = (primaryAxis + axisOffset) % 3;
            double axisLength = dimensions.getComponent(axis);

            // Skip axes with minimal length
            if (axisLength < 1e-6) continue;

            // Adaptive number of splits based on triangle count
            int actualSplits = Math.min(this.splitCount, 1 + triangleIndices.size() / 10);

            // Test multiple split positions along the axis
            for (int splitPos = 1; splitPos < actualSplits; splitPos++) {
                double splitValue = min.getComponent(axis) + (axisLength * splitPos / actualSplits);

                // Split triangles based on their pre-computed centroids
                List<Integer> leftIndices = new ArrayList<>();
                List<Integer> rightIndices = new ArrayList<>();

                for (int index : triangleIndices) {
                    Vector3D center = triangleCentroids[index];
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

                // SAH cost: areaL/areaTotal * nL + areaR/areaTotal * nR
                double cost = (leftArea / totalArea) * leftIndices.size() +
                        (rightArea / totalArea) * rightIndices.size();

                // Keep track of best split
                if (cost < bestCost) {
                    bestCost = cost;
                    bestSplit = new BVHSplit(leftIndices, rightIndices, leftBox, rightBox, cost);
                }

                // Early termination if we found a good enough split
                if (bestCost < triangleIndices.size() * 0.7) {
                    return bestSplit;
                }
            }

            // If we found a reasonable split on the primary axis, use it
            if (bestSplit != null && axisOffset == 0 && bestCost < triangleIndices.size() * 0.85) {
                break;
            }
        }

        return bestSplit;
    }

    /**
     * Determines the longest axis of a 3D vector.
     *
     * @param dimensions The dimensions to analyze
     * @return 0 for X-axis, 1 for Y-axis, 2 for Z-axis
     */
    private int getLongestAxis(Vector3D dimensions) {
        double x = dimensions.getX();
        double y = dimensions.getY();
        double z = dimensions.getZ();

        if (x >= y && x >= z) return 0;
        if (y >= x && y >= z) return 1;
        return 2;
    }

    /**
     * Calculates the surface area of a bounding box.
     *
     * @param box The bounding box to measure
     * @return Surface area of the box
     */
    private double calculateSurfaceArea(Cube box) {
        Vector3D dimensions = box.getDimensions();
        double x = dimensions.getX();
        double y = dimensions.getY();
        double z = dimensions.getZ();
        return 2.0 * (x * y + x * z + y * z);
    }

    /**
     * Updates leaf node statistics in a thread-safe manner.
     *
     * @param triangleCount Number of triangles in the current leaf node
     */
    private void updateLeafStatistics(int triangleCount) {
        minTrianglesInLeaf.updateAndGet(min -> Math.min(min, triangleCount));
        maxTrianglesInLeaf.updateAndGet(max -> Math.max(max, triangleCount));
        totalTrianglesInLeaves.addAndGet(triangleCount);
        totalLeafNodes.incrementAndGet();
    }

    /**
     * Calculates a bounding box that contains all specified triangles.
     *
     * @param triangleIndices List of triangle indices to include
     * @return A bounding box containing all specified triangles with margin
     */
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

    /**
     * Helper class to store information about a potential BVH split.
     */
    private static class BVHSplit {
        /** Indices of triangles in the left partition */
        List<Integer> leftIndices;

        /** Indices of triangles in the right partition */
        List<Integer> rightIndices;

        /** Bounding box of left partition */
        Cube leftBox;

        /** Bounding box of right partition */
        Cube rightBox;

        /** Cost of this split according to SAH */
        double cost;

        /**
         * Creates a new BVHSplit record.
         *
         * @param leftIndices Triangle indices in left partition
         * @param rightIndices Triangle indices in right partition
         * @param leftBox Bounding box of left partition
         * @param rightBox Bounding box of right partition
         * @param cost SAH cost of this split
         */
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