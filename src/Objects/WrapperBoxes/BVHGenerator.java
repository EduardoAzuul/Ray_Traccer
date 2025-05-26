package Objects.WrapperBoxes;

import Objects.Cube;
import Objects.Triangle;
import vectors.Vector3D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class BVHGenerator {
    private List<Triangle> triangleList;
    private int maxBVHDepth;
    private final double marginFactor = 0.05; // 5% margin for boxes
    private final int trianglesPerLeaf = 4;
    private final int splitCount = 8;

    // Use a ForkJoinPool for parallel processing
    private final ForkJoinPool forkJoinPool;

    // Thread-safe statistics using atomic integers
    private final AtomicInteger minTrianglesInLeaf = new AtomicInteger(Integer.MAX_VALUE);
    private final AtomicInteger maxTrianglesInLeaf = new AtomicInteger(0);
    private final AtomicInteger totalLeafNodes = new AtomicInteger(0);
    private final AtomicInteger totalTrianglesInLeaves = new AtomicInteger(0);
    private final AtomicInteger maxDepthReached = new AtomicInteger(0);
    private final AtomicInteger totalNodes = new AtomicInteger(0);

    // Pre-compute triangle centroids to avoid repeated calculations
    private Vector3D[] triangleCentroids;

    public BVHGenerator(List<Triangle> triangleList) {
        this.triangleList = triangleList;
        this.maxBVHDepth = calculateOptimalDepth(triangleList.size());
        this.forkJoinPool = new ForkJoinPool();
        precomputeCentroids();
    }

    public BVHGenerator(List<Triangle> triangleList, int maxDepth) {
        this.triangleList = triangleList;
        this.maxBVHDepth = maxDepth > 0 ? maxDepth : calculateOptimalDepth(triangleList.size());
        this.forkJoinPool = new ForkJoinPool();
        precomputeCentroids();
    }


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

        // Print statistics after building
        //printBVHStatistics();

        return root;
    }

    // Pre-compute all triangle centroids to avoid redundant calculations
    private void precomputeCentroids() {
        triangleCentroids = new Vector3D[triangleList.size()];
        for (int i = 0; i < triangleList.size(); i++) {
            triangleCentroids[i] = triangleList.get(i).getCenter();
        }
    }

    private int calculateOptimalDepth(int triangleCount) {
        int maxDepth = 16;
        int minDepth = 2;

        if (triangleCount <= this.trianglesPerLeaf) {
            return minDepth;
        }

        int estimatedDepth = (int) Math.ceil(Math.log(triangleCount / (double) this.trianglesPerLeaf) / Math.log(2));
        return Math.max(minDepth, Math.min(maxDepth, estimatedDepth));
    }

    // RecursiveTask for parallel BVH construction using Fork/Join framework
    private class BVHBuildTask extends RecursiveTask<BVHNode> {
        private final List<Integer> triangleIndices;
        private final int currentDepth;
        private static final int SEQUENTIAL_THRESHOLD = 256; // Process small subproblems sequentially

        public BVHBuildTask(List<Integer> triangleIndices, int currentDepth) {
            this.triangleIndices = triangleIndices;
            this.currentDepth = currentDepth;
        }

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
                // Update leaf statistics
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
                leftTask.fork(); // Submit left task to be executed asynchronously
                BVHNode rightChild = rightTask.compute(); // Compute right task directly
                BVHNode leftChild = leftTask.join(); // Wait for left task result
                return new BVHInternalNode(nodeBoundingBox, leftChild, rightChild);
            }
        }
    }

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

            // Adaptive number of splits based on triangle count to avoid excessive tests
            int actualSplits = Math.min(this.splitCount, 1 + triangleIndices.size() / 10);

            // Test multiple split positions along the axis
            for (int splitPos = 1; splitPos < actualSplits; splitPos++) {
                double splitValue = min.getComponent(axis) + (axisLength * splitPos / actualSplits);

                // Split triangles based on their pre-computed centroids
                List<Integer> leftIndices = new ArrayList<>();
                List<Integer> rightIndices = new ArrayList<>();

                for (int index : triangleIndices) {
                    // Use pre-computed centroids
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

    // Find the axis with the longest dimension
    private int getLongestAxis(Vector3D dimensions) {
        double x = dimensions.getX();
        double y = dimensions.getY();
        double z = dimensions.getZ();

        if (x >= y && x >= z) return 0; // X-axis
        if (y >= x && y >= z) return 1; // Y-axis
        return 2; // Z-axis
    }

    private double calculateSurfaceArea(Cube box) {
        Vector3D dimensions = box.getDimensions();
        double x = dimensions.getX();
        double y = dimensions.getY();
        double z = dimensions.getZ();
        return 2.0 * (x * y + x * z + y * z);
    }

    private void updateLeafStatistics(int triangleCount) {
        // Use atomic operations for thread safety
        minTrianglesInLeaf.updateAndGet(min -> Math.min(min, triangleCount));
        maxTrianglesInLeaf.updateAndGet(max -> Math.max(max, triangleCount));
        totalTrianglesInLeaves.addAndGet(triangleCount);
        totalLeafNodes.incrementAndGet();
    }

    private void printBVHStatistics() {
        System.out.println("\n--- BVH Statistics ---");
        System.out.println("Total triangles: " + triangleList.size());
        System.out.println("Total nodes: " + totalNodes.get());
        System.out.println("Total leaf nodes: " + totalLeafNodes.get());
        System.out.println("Maximum depth reached: " + maxDepthReached.get());
        System.out.println("Minimum triangles in a leaf: " + minTrianglesInLeaf.get());
        System.out.println("Maximum triangles in a leaf: " + maxTrianglesInLeaf.get());

        double avgTrianglesPerLeaf = totalLeafNodes.get() > 0 ?
                (double) totalTrianglesInLeaves.get() / totalLeafNodes.get() : 0;
        System.out.println("Average triangles per leaf: " + String.format("%.2f", avgTrianglesPerLeaf));
        System.out.println("----------------------");
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