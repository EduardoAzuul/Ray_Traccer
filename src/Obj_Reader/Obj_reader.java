package Obj_Reader;

import vectors.Vector3D;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Obj_reader {

    public static void main(String[] args){
        readPoints("C:/Users/josem/OneDrive/Escritorio/UP/Multimedios/RayTracer/RayTracer_v0.1 - copia/Objs/objStar.obj");

    }

    public static Vector3D[] readPoints(String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<Vector3D> pointsList = new ArrayList<Vector3D>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] parts = line.trim().split("\\s+"); // Split by any whitespace

                    if (parts.length >= 4) { // parts[0] = "v", then x y z
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        double z = Double.parseDouble(parts[3]);

                        pointsList.add(new Vector3D(x, y, z));
                    }
                }

                System.out.println(line);
            }
            printPointList(pointsList);

        }
        catch (IOException e) {
            return null;
        }
        return null;
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


}
