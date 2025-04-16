package Obj_Reader;

import Objects.Triangle;
import vectors.Vector3D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjReader {

    //Reads the point line, and gives them as vector3D
    private static Vector3D[] readPoints(String filePath, Vector3D origin) {
        List<Vector3D> pointsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            double originX = origin.getX();
            double originY = origin.getY();
            double originZ = origin.getZ();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 4) {
                        double x = Double.parseDouble(parts[1]) + originX;
                        double y = Double.parseDouble(parts[2]) + originY;
                        double z = Double.parseDouble(parts[3]) + originZ;
                        pointsList.add(new Vector3D(x, y, z));
                    }
                }
            }

            System.out.println("Loaded " + pointsList.size() + " vertices.");
            return pointsList.toArray(new Vector3D[0]);

        } catch (IOException e) {
            System.err.println("Error reading points: " + e.getMessage());
            return new Vector3D[0];
        }
    }

    /***************************************************************************************/

    private static int[][] readFaces(String filePath) {
        System.out.println("Reading faces from " + filePath);
        List<int[]> triangleIndices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("f ")) { // If the read line is a face
                    String[] parts = line.trim().split("\\s+");

                    // Fan triangulation: f v1 v2 v3 v4 → (v1,v2,v3), (v1,v3,v4)
                    for (int i = 1; i < parts.length - 2; i++) {
                        int v1 = parseSafeIndex(parts[1]);
                        int v2 = parseSafeIndex(parts[i + 1]);
                        int v3 = parseSafeIndex(parts[i + 2]);

                        // Only add if all indices are valid
                        if (v1 != -1 && v2 != -1 && v3 != -1) {
                            triangleIndices.add(new int[]{v1, v2, v3});
                        }
                    }
                }
            }
            System.out.println("Loaded " + triangleIndices.size() + " triangles.");
            return triangleIndices.toArray(new int[triangleIndices.size()][]);

        } catch (IOException e) {
            System.err.println("Error reading faces: " + e.getMessage());
            return new int[0][0];
        }

    }



    public static List<Triangle> triangleList(Vector3D color, Vector3D rotation, Vector3D origin, Vector3D scale , String filePath) {
        int[][] faces =readFaces(filePath);
        Vector3D[] vertices = readPoints(filePath, origin);

        List<Triangle> triangles = new ArrayList<>();
        //Vector3D color, Vector3D rotation, Vector3D origin,
        //Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3
        for (int[] face : faces) {
            if (face[0] < 0 || face[1] < 0 || face[2] < 0 ||
                    face[0] >= vertices.length || face[1] >= vertices.length || face[2] >= vertices.length) {
                System.err.println("Skipping invalid face: " + face[0] + ", " + face[1] + ", " + face[2]);
                continue;
            }

            Vector3D vertex = vertices[face[0]];
            Vector3D vertex2 = vertices[face[1]];
            Vector3D vertex3 = vertices[face[2]];

            Vector3D randColor= Vector3D.randomColor();

            Triangle triangle = new Triangle(randColor, rotation, origin, scale, vertex, vertex2, vertex3);
            triangles.add(triangle);
            System.out.println("Points " + vertex + " |  " + vertex2 + "  |  " + vertex3);
        }


        System.out.println("Loaded " + triangles.size() + " triangles.");

        return triangles;
    }


    /*******************************************************************************/
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
        }
        for (int i = 0; i < listFaces.size(); i++) {
            System.out.println("Face " + (i + 1) + ": " + listFaces.get(i));
        }
    }



    //This method is needed in case the parsing the face to int with the obj file goes wrong
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
