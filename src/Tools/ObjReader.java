package Tools;

import Objects.Triangle;
import vectors.Vector3D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjReader {

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
            return pointsList.toArray(new Vector3D[0]);
        } catch (IOException e) {
            System.err.println("Error reading points: " + e.getMessage());
            return new Vector3D[0];
        }
    }

    private static int[][] readFaces(String filePath) {
        List<int[]> triangleIndices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("f ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length < 4) continue;

                    // Parse vertex indices (1-based in OBJ)
                    int[] vertexIndices = new int[parts.length-1];
                    for (int i = 1; i < parts.length; i++) {
                        String[] data = parts[i].split("/");
                        vertexIndices[i-1] = Integer.parseInt(data[0]);
                    }

                    // Simple triangulation (fan)
                    for (int i = 1; i < vertexIndices.length - 1; i++) {
                        triangleIndices.add(new int[]{
                                vertexIndices[0],
                                vertexIndices[i],
                                vertexIndices[i+1]
                        });
                    }
                }
            }
            return triangleIndices.toArray(new int[0][]);
        } catch (IOException e) {
            System.err.println("Error reading faces: " + e.getMessage());
            return new int[0][0];
        }
    }

    public static List<Triangle> triangleList(Vector3D color, Vector3D rotation,
                                              Vector3D origin, Vector3D scale, String filePath) {
        int[][] faces = readFaces(filePath);
        Vector3D[] vertices = readPoints(filePath);
        List<Triangle> triangles = new ArrayList<>();

        for (int[] face : faces) {
            // OBJ uses 1-based indexing
            int v1 = face[0] - 1;
            int v2 = face[1] - 1;
            int v3 = face[2] - 1;

            if (v1 < 0 || v2 < 0 || v3 < 0 ||
                    v1 >= vertices.length || v2 >= vertices.length || v3 >= vertices.length) {
                System.err.printf("Invalid face indices: %d, %d, %d (vertex count: %d)%n",
                        face[0], face[1], face[2], vertices.length);
                continue;
            }

            triangles.add(new Triangle(color, rotation, origin, scale,
                    vertices[v1], vertices[v2], vertices[v3]));
        }
        return triangles;
    }

    public static void printPointList(List<Vector3D> listPoints) {
        if (listPoints == null || listPoints.isEmpty()) {
            System.out.println("The list is empty or null.");
            return;
        }

        for (int i = 0; i < listPoints.size(); i++) {
            System.out.println("Point " + (i + 1) + ": " + listPoints.get(i));
        }
    }

    public static void printFaceList(List<int[]> listFaces) {
        if (listFaces == null || listFaces.isEmpty()) {
            System.out.println("The list is empty or null.");
            return;
        }

        for (int i = 0; i < listFaces.size(); i++) {
            int[] face = listFaces.get(i);
            System.out.printf("Face %d: [%d, %d, %d]%n", i + 1, face[0], face[1], face[2]);
        }
    }

    private static int parseSafeIndex(String faceComponent) {
        try {
            String[] split = faceComponent.split("/");
            return Integer.parseInt(split[0]) - 1; // OBJ is 1-indexed
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Invalid face index: " + faceComponent);
            return -1;
        }
    }
}
