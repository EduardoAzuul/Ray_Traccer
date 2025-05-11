package Tools;

import Objects.Triangle;
import vectors.Vector3D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                        normalsList.add(new Vector3D(x, y, z));
                    }
                }
            }
            return normalsList.toArray(new Vector3D[0]);
        } catch (IOException e) {
            System.err.println("Error reading normals: " + e.getMessage());
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

    /**
     * Lee las caras del archivo OBJ y extrae los índices de normales
     * @param filePath Ruta del archivo OBJ
     * @return Un array 2D donde cada fila representa los índices de normales para un triángulo
     */
    private static int[][] readNormalIndices(String filePath) {
        List<int[]> normalIndices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("f ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length < 4) continue;

                    // Parse normal indices (1-based in OBJ)
                    int[] indices = new int[parts.length-1];
                    for (int i = 1; i < parts.length; i++) {
                        String[] data = parts[i].split("/");
                        if (data.length >= 3) {
                            indices[i-1] = Integer.parseInt(data[2]);
                        } else {
                            indices[i-1] = -1; // No normal specified
                        }
                    }

                    // Simple triangulation (fan) for n-gons
                    for (int i = 1; i < indices.length - 1; i++) {
                        normalIndices.add(new int[]{
                                indices[0],
                                indices[i],
                                indices[i+1]
                        });
                    }
                }
            }
            return normalIndices.toArray(new int[0][]);
        } catch (IOException e) {
            System.err.println("Error reading normal indices: " + e.getMessage());
            return new int[0][0];
        }
    }

    /**
     * Lee los grupos de suavizado del archivo OBJ
     * @param filePath Ruta del archivo OBJ
     * @return Una lista de listas, donde cada lista interna contiene los índices de triángulos
     *         que pertenecen al mismo grupo de suavizado
     */
    public static List<List<Integer>> readSmoothingGroups(String filePath) {
        // Maps smoothing group ID to a list of face indices (or triangle indices)
        Map<Integer, List<Integer>> groupMap = new HashMap<>();
        List<List<Integer>> result = new ArrayList<>();
        int currentGroup = 0; // 0 = no smoothing

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int faceIndex = 0; // Tracks the current face (or triangle) index

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("s ")) {
                    // Parse smoothing group
                    String value = line.substring(2).trim();
                    if (value.equalsIgnoreCase("off") || value.equals("0")) {
                        currentGroup = 0;
                    } else {
                        try {
                            currentGroup = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            currentGroup = 0;
                        }
                    }
                } else if (line.startsWith("f ")) {
                    // For each face, determine how many triangles it generates (n-2 for an n-gon)
                    int vertexCount = line.trim().split("\\s+").length - 1;
                    int trianglesInFace = vertexCount - 2;

                    // Ensure the smoothing group exists in the map
                    if (!groupMap.containsKey(currentGroup)) {
                        groupMap.put(currentGroup, new ArrayList<>());
                    }

                    // Add each triangle to its smoothing group's list
                    for (int i = 0; i < trianglesInFace; i++) {
                        groupMap.get(currentGroup).add(faceIndex++);
                    }
                }
            }

            groupMap.keySet().stream().sorted()
                    .forEach(group -> result.add(groupMap.get(group)));

        } catch (IOException e) {
            System.err.println("Error reading smoothing groups: " + e.getMessage());
        }

        return result;
    }

    /**
     * Calcula las normales suavizadas para cada vértice basándose en los grupos de suavizado
     * @autor José Eduardo Moreno Paredes
     * @param vertices Array de vértices
     * @param faces Array de caras (índices de vértices)
     * @param normals Array de normales originales
     * @param normalIndices Array de índices de normales para cada cara
     * @param smoothingGroups Grupos de suavizado
     * @return Un array de Vector3D que contiene las normales suavizadas para cada vértice
     */
    private static Vector3D[] calculateSmoothedNormals(
            Vector3D[] vertices,
            int[][] faces,
            Vector3D[] normals,
            int[][] normalIndices,
            List<List<Integer>> smoothingGroups) {

        // Inicializar array para almacenar las normales suavizadas por vértice
        Vector3D[] smoothedNormals = new Vector3D[vertices.length];
        for (int i = 0; i < smoothedNormals.length; i++) {
            smoothedNormals[i] = new Vector3D(0, 0, 0);
        }

        // Para cada grupo de suavizado
        for (List<Integer> group : smoothingGroups) {
            if (group.isEmpty()) continue;

            // Mapa que relaciona índice de vértice con su normal acumulada
            Map<Integer, Vector3D> vertexToNormal = new HashMap<>();

            // Para cada triángulo en este grupo
            for (Integer triangleIndex : group) {
                if (triangleIndex >= faces.length) continue;

                int[] face = faces[triangleIndex];
                int[] normalIdx = normalIndices.length > triangleIndex ? normalIndices[triangleIndex] : null;

                // Para cada vértice del triángulo
                for (int i = 0; i < 3; i++) {
                    int vertexIndex = face[i] - 1; // OBJ es 1-indexed

                    // Obtener la normal para este vértice
                    Vector3D normal;
                    if (normalIdx != null && normalIdx[i] > 0 && normalIdx[i] <= normals.length) {
                        normal = normals[normalIdx[i] - 1]; // Usar la normal especificada en el archivo
                    } else {
                        // Calcular normal del triángulo si no hay normal asignada
                        Vector3D v1 = vertices[face[0] - 1];
                        Vector3D v2 = vertices[face[1] - 1];
                        Vector3D v3 = vertices[face[2] - 1];
                        Vector3D edge1 = v2.subtract(v1);
                        Vector3D edge2 = v3.subtract(v1);
                        normal = edge1.cross(edge2).normalize();
                    }

                    // Acumular normal para este vértice
                    if (!vertexToNormal.containsKey(vertexIndex)) {
                        vertexToNormal.put(vertexIndex, new Vector3D(normal));
                    } else {
                        vertexToNormal.get(vertexIndex).add(normal);
                    }
                }
            }

            // Normalizar las normales acumuladas
            for (Map.Entry<Integer, Vector3D> entry : vertexToNormal.entrySet()) {
                entry.getValue().normalize();
                smoothedNormals[entry.getKey()] = entry.getValue();
            }
        }

        return smoothedNormals;
    }

    /**
     * Crea una lista de triángulos utilizando normales suavizadas
     */
    public static List<Triangle> triangleList(
            Vector3D color, Vector3D rotation, Vector3D origin, Vector3D scale, String filePath) {

        // Leer datos del archivo OBJ
        int[][] faces = readFaces(filePath);
        Vector3D[] vertices = readPoints(filePath);
        Vector3D[] normals = readNormals(filePath);
        int[][] normalIndices = readNormalIndices(filePath);
        List<List<Integer>> smoothingGroups = readSmoothingGroups(filePath);

        // Calcular normales suavizadas
        Vector3D[] smoothedNormals = calculateSmoothedNormals(
                vertices, faces, normals, normalIndices, smoothingGroups);

        List<Triangle> triangles = new ArrayList<>();

        for (int i = 0; i < faces.length; i++) {
            int[] face = faces[i];

            // OBJ usa indexación 1-based
            int v1 = face[0] - 1;
            int v2 = face[1] - 1;
            int v3 = face[2] - 1;

            if (v1 < 0 || v2 < 0 || v3 < 0 ||
                    v1 >= vertices.length || v2 >= vertices.length || v3 >= vertices.length) {
                System.err.printf("Invalid face indices: %d, %d, %d (vertex count: %d)%n",
                        face[0], face[1], face[2], vertices.length);
                continue;
            }

            // Obtener las normales suavizadas para cada vértice
            Vector3D normal1 = smoothedNormals[v1];
            Vector3D normal2 = smoothedNormals[v2];
            Vector3D normal3 = smoothedNormals[v3];

            // Si alguna normal es cero (no se pudo calcular), calcular la normal del triángulo
            if (normal1.magnitude() < 0.0001 || normal2.magnitude() < 0.0001 || normal3.magnitude() < 0.0001) {
                Vector3D edge1 = vertices[v2].subtract(vertices[v1]);
                Vector3D edge2 = vertices[v3].subtract(vertices[v1]);
                Vector3D faceNormal = edge1.cross(edge2).normalize();

                if (normal1.magnitude() < 0.0001) normal1 = faceNormal;
                if (normal2.magnitude() < 0.0001) normal2 = faceNormal;
                if (normal3.magnitude() < 0.0001) normal3 = faceNormal;
            }

            triangles.add(new Triangle(
                    color, rotation, origin, scale,
                    vertices[v1], vertices[v2], vertices[v3],
                    normal1, normal2, normal3));
        }

        return triangles;
    }

    /**
     * Método original
     */
    public static List<Triangle> triangleListWithoutNormals(Vector3D color, Vector3D rotation,
                                              Vector3D origin, Vector3D scale, String filePath) {
        int[][] faces = readFaces(filePath);
        Vector3D[] vertices = readPoints(filePath);
        List<Triangle> triangles = new ArrayList<>();
        Vector3D normal1 = new Vector3D();
        Vector3D normal2 = new Vector3D();
        Vector3D normal3 = new Vector3D();

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
                    vertices[v1], vertices[v2], vertices[v3], normal1, normal2, normal3));
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