package Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmoothingGroups {

    public static int[][]smoothedNormals(String filePath, int[] normals) {
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
                        vertexIndices[i-1] = Integer.parseInt(data[2]);
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


}
