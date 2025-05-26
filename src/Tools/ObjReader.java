package Tools;

import Objects.Triangle;
import vectors.Vector3D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for reading and parsing Wavefront OBJ files.
 * Handles vertex positions, normals, faces, smoothing groups and generates triangle data.
 *
 * @author José Eduardo Moreno Paredes
 */
public class ObjReader {

    /**
     * Reads vertex positions from an OBJ file.
     *
     * @param filePath Path to the OBJ file
     * @return Array of Vector3D representing vertices
     */
    private static Vector3D[] readPoints(String filePath) {
        List<Vector3D> pointsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 4) {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        double z = Double.parseDouble(parts[3]);
                        pointsList.add(new Vector3D(x, y, z));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading points: " + e.getMessage());
        }

        return pointsList.toArray(new Vector3D[0]);
    }

    private static Vector3D[] readTextures(String filePath) {
        List<Vector3D> texturesList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("vt ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 3) {  // Changed from 4 to 3
                        double u = Double.parseDouble(parts[1]);
                        double v = Double.parseDouble(parts[2]);
                        // Some OBJ files might include w (3D texture coordinates)
                        double w = parts.length >= 4 ? Double.parseDouble(parts[3]) : 0;
                        texturesList.add(new Vector3D(u, v, w));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading texture coordinates: " + e.getMessage());
        }

        return texturesList.toArray(new Vector3D[0]);
    }

    private static int[][] readTextureIndices(String filePath) {
        List<int[]> textureIndices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("f ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length < 4) continue;

                    int[] indices = new int[parts.length - 1];
                    for (int i = 1; i < parts.length; i++) {
                        String[] data = parts[i].split("/");
                        // Texture coordinate is the second component (data[1])
                        indices[i - 1] = (data.length >= 2 && !data[1].isEmpty()) ? Integer.parseInt(data[1]) : -1;
                    }

                    // Triangulate the face
                    for (int i = 1; i < indices.length - 1; i++) {
                        textureIndices.add(new int[]{
                                indices[0],
                                indices[i],
                                indices[i + 1]
                        });
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading texture indices: " + e.getMessage());
        }

        return textureIndices.toArray(new int[0][]);
    }
    /**
     * Reads vertex normals from an OBJ file.
     *
     * @param filePath Path to the OBJ file
     * @return Array of Vector3D representing normals
     */
    private static Vector3D[] readNormals(String filePath) {
        List<Vector3D> normalsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("vn ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 4) {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        double z = Double.parseDouble(parts[3]);
                        Vector3D normal = new Vector3D(x, y, z);
                        // Ensure the normal is normalized
                        normal = normal.normalize();
                        normalsList.add(normal);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading normals: " + e.getMessage());
        }

        return normalsList.toArray(new Vector3D[0]);
    }

    /**
     * Reads face indices from an OBJ file, supports triangulation of polygons.
     *
     * @param filePath Path to the OBJ file
     * @return 2D array of face vertex indices
     */
    private static int[][] readFaces(String filePath) {
        List<int[]> triangleIndices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("f ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length < 4) continue;

                    int[] vertexIndices = new int[parts.length - 1];
                    for (int i = 1; i < parts.length; i++) {
                        vertexIndices[i - 1] = parseSafeIndex(parts[i]);
                    }

                    // Triangulate the face (fan triangulation for convex polygons)
                    for (int i = 1; i < vertexIndices.length - 1; i++) {
                        triangleIndices.add(new int[]{
                                vertexIndices[0],
                                vertexIndices[i],
                                vertexIndices[i + 1]
                        });
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading faces: " + e.getMessage());
        }

        return triangleIndices.toArray(new int[0][]);
    }

    /**
     * Reads normal indices from face definitions in the OBJ file.
     *
     * @param filePath Path to the OBJ file
     * @return 2D array of normal indices per face
     */
    private static int[][] readNormalIndices(String filePath) {
        List<int[]> normalIndices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("f ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length < 4) continue;

                    int[] indices = new int[parts.length - 1];
                    for (int i = 1; i < parts.length; i++) {
                        String[] data = parts[i].split("/");
                        indices[i - 1] = (data.length >= 3 && !data[2].isEmpty()) ? Integer.parseInt(data[2]) : -1;
                    }

                    // Triangulate the face (maintain corresponding normal indices)
                    for (int i = 1; i < indices.length - 1; i++) {
                        normalIndices.add(new int[]{
                                indices[0],
                                indices[i],
                                indices[i + 1]
                        });
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading normal indices: " + e.getMessage());
        }

        return normalIndices.toArray(new int[0][]);
    }

    /**
     * Reads smoothing groups from an OBJ file.
     * Properly tracks triangulation of polygons and maintains correct face indices.
     *
     * @param filePath Path to the OBJ file
     * @return List of triangle index groups per smoothing group
     */
    public static List<List<Integer>> readSmoothingGroups(String filePath) {
        Map<Integer, List<Integer>> groupMap = new HashMap<>();
        int currentGroup = 0;
        int triangleIndex = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("s ")) {
                    // Parse smoothing group directive
                    String value = line.substring(2).trim();
                    if (value.equalsIgnoreCase("off") || value.equals("0")) {
                        currentGroup = 0; // No smoothing
                    } else {
                        try {
                            currentGroup = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            // Invalid smoothing group, default to 0
                            currentGroup = 0;
                        }
                    }
                } else if (line.startsWith("f ")) {
                    // Process face definition
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length < 4) continue; // Skip invalid faces

                    // Calculate how many triangles this face will generate
                    int vertexCount = parts.length - 1;
                    int trianglesInFace = vertexCount - 2;

                    // Ensure the current group exists in the map
                    if (!groupMap.containsKey(currentGroup)) {
                        groupMap.put(currentGroup, new ArrayList<>());
                    }

                    // Add indices for all triangles generated from this face
                    for (int i = 0; i < trianglesInFace; i++) {
                        groupMap.get(currentGroup).add(triangleIndex++);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading smoothing groups: " + e.getMessage());
        }

        // Sort groups by smoothing group ID and return as a list
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> sortedKeys = new ArrayList<>(groupMap.keySet());
        Collections.sort(sortedKeys);

        for (Integer key : sortedKeys) {
            result.add(groupMap.get(key));
        }

        return result;
    }

    /**
     * Calculates per-vertex smoothed normals using smoothing group information.
     * Properly handles normalization and accounts for quads and larger polygons.
     */
    private static Vector3D[] calculateSmoothedNormals(Vector3D[] vertices, int[][] faces, Vector3D[] normals,
                                                       int[][] normalIndices, List<List<Integer>> smoothingGroups) {
        if (vertices == null || vertices.length == 0) return new Vector3D[0];

        // Initialize with zero vectors
        Vector3D[] smoothedNormals = new Vector3D[vertices.length];
        for (int i = 0; i < smoothedNormals.length; i++) {
            smoothedNormals[i] = new Vector3D(0, 0, 0);
        }

        // Keep track of which vertices have been processed
        boolean[] vertexProcessed = new boolean[vertices.length];

        // Process each smoothing group
        for (List<Integer> group : smoothingGroups) {
            if (group == null || group.isEmpty()) continue;

            // Map to accumulate normals per vertex in this smoothing group
            Map<Integer, Vector3D> vertexToNormal = new HashMap<>();

            // First pass: Calculate and accumulate normals for each vertex in the smoothing group
            for (Integer triangleIndex : group) {
                if (triangleIndex < 0 || triangleIndex >= faces.length) continue;

                int[] face = faces[triangleIndex];
                if (face == null || face.length < 3) continue;

                // Calculate face normal if needed
                Vector3D faceNormal = null;

                // Get normal indices for this triangle, if available
                int[] normalIdx = (normalIndices != null && normalIndices.length > triangleIndex) ?
                        normalIndices[triangleIndex] : null;

                // Process each vertex of the triangle
                for (int i = 0; i < 3; i++) {
                    int vertexIndex = face[i];
                    if (vertexIndex < 0 || vertexIndex >= vertices.length) continue;

                    Vector3D normal;

                    // Use explicit normal if available
                    if (normalIdx != null && normalIdx[i] > 0 && normalIdx[i] <= normals.length) {
                        normal = normals[normalIdx[i] - 1];
                    } else {
                        // Calculate face normal if not done yet
                        if (faceNormal == null) {
                            // Use correct indices for face vertices
                            Vector3D v1 = vertices[face[0]];
                            Vector3D v2 = vertices[face[1]];
                            Vector3D v3 = vertices[face[2]];

                            // Calculate edges
                            Vector3D edge1 = v2.subtract(v1);
                            Vector3D edge2 = v3.subtract(v1);

                            // Calculate face normal using cross product
                            faceNormal = edge1.cross(edge2);
                            double magnitude = faceNormal.magnitude();

                            // Normalize only if magnitude is significant
                            if (magnitude > 1e-6) {
                                faceNormal = faceNormal.scale(1.0 / magnitude);
                            } else {
                                // Use a default normal for degenerate cases
                                faceNormal = new Vector3D(0, 1, 0);
                            }
                        }
                        normal = faceNormal;
                    }

                    // Accumulate normal for this vertex
                    vertexToNormal.merge(vertexIndex, normal, Vector3D::add);
                }
            }

            // Second pass: Normalize accumulated normals and assign to vertices
            for (Map.Entry<Integer, Vector3D> entry : vertexToNormal.entrySet()) {
                int vertexIndex = entry.getKey();
                Vector3D accumulatedNormal = entry.getValue();

                // Normalize the accumulated normal
                double magnitude = accumulatedNormal.magnitude();
                if (magnitude > 1e-6) {
                    accumulatedNormal = accumulatedNormal.scale(1.0 / magnitude);
                } else {
                    // Use a default normal if the accumulated normal is too small
                    accumulatedNormal = new Vector3D(0, 1, 0);
                }

                // Assign the normalized normal to the vertex
                smoothedNormals[vertexIndex] = accumulatedNormal;
                vertexProcessed[vertexIndex] = true;
            }
        }

        // Handle any vertices that weren't part of any smoothing group
        for (int i = 0; i < vertices.length; i++) {
            if (!vertexProcessed[i]) {
                // Find any face that contains this vertex and use its normal
                Vector3D normal = null;

                for (int j = 0; j < faces.length && normal == null; j++) {
                    int[] face = faces[j];
                    boolean containsVertex = false;

                    for (int k = 0; k < face.length; k++) {
                        if (face[k] == i) {
                            containsVertex = true;
                            break;
                        }
                    }

                    if (containsVertex) {
                        Vector3D v1 = vertices[face[0]];
                        Vector3D v2 = vertices[face[1]];
                        Vector3D v3 = vertices[face[2]];

                        Vector3D edge1 = v2.subtract(v1);
                        Vector3D edge2 = v3.subtract(v1);

                        normal = edge1.cross(edge2);
                        double magnitude = normal.magnitude();

                        if (magnitude > 1e-6) {
                            normal = normal.scale(1.0 / magnitude);
                        } else {
                            normal = new Vector3D(0, 1, 0);
                        }
                    }
                }

                if (normal != null) {
                    smoothedNormals[i] = normal;
                } else {
                    // Default normal if no containing face was found
                    smoothedNormals[i] = new Vector3D(0, 1, 0);
                }
            }
        }

        return smoothedNormals;
    }

    /**
     * Generates a list of Triangle objects using smoothed normals.
     */
    public static List<Triangle> triangleList(Vector3D color, Vector3D rotation, Vector3D origin, Vector3D scale,
                                              String filePath) {
        Vector3D[] vertices = readPoints(filePath);
        int[][] faces = readFaces(filePath);
        Vector3D[] normals = readNormals(filePath);
        int[][] normalIndices = readNormalIndices(filePath);
        List<List<Integer>> smoothingGroups = readSmoothingGroups(filePath);
        Vector3D[] textures = readTextures(filePath);
        int[][] textureIndices = readTextureIndices(filePath);  // Added this line

        Vector3D[] smoothedNormals = calculateSmoothedNormals(vertices, faces, normals, normalIndices, smoothingGroups);

        List<Triangle> triangles = new ArrayList<>();

        for (int i = 0; i < faces.length; i++) {
            int[] face = faces[i];
            if (face == null || face.length < 3) continue;

            int v1 = face[0];
            int v2 = face[1];
            int v3 = face[2];

            if (v1 < 0 || v2 < 0 || v3 < 0 || v1 >= vertices.length || v2 >= vertices.length || v3 >= vertices.length)
                continue;

            Vector3D n1 = smoothedNormals[v1];
            Vector3D n2 = smoothedNormals[v2];
            Vector3D n3 = smoothedNormals[v3];

            // Normal normalization code remains the same...

            Triangle triangle = new Triangle(color, rotation, origin, scale,
                    vertices[v1], vertices[v2], vertices[v3], n1, n2, n3);

            // Add texture coordinates if available
            if (textureIndices != null && i < textureIndices.length && textures.length > 0) {
                int[] texIndices = textureIndices[i];
                if (texIndices != null && texIndices.length >= 3) {
                    Vector3D t1 = (texIndices[0] > 0 && texIndices[0] <= textures.length) ?
                            textures[texIndices[0] - 1] : null;
                    Vector3D t2 = (texIndices[1] > 0 && texIndices[1] <= textures.length) ?
                            textures[texIndices[1] - 1] : null;
                    Vector3D t3 = (texIndices[2] > 0 && texIndices[2] <= textures.length) ?
                            textures[texIndices[2] - 1] : null;

                    if (t1 != null && t2 != null && t3 != null) {
                        triangle.setTextures(t1, t2, t3);
                    }
                }
            }

            triangles.add(triangle);
        }

        return triangles;
    }

    /**
     * Safely parses the vertex index from a face component.
     * Returns zero-indexed value (OBJ indices are 1-based).
     */
    private static int parseSafeIndex(String faceComponent) {
        try {
            String[] split = faceComponent.split("/");
            return Integer.parseInt(split[0]) - 1; // Convert to 0-based index
        } catch (Exception e) {
            return -1;
        }
    }
}