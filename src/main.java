import Objects.Camera;
import Objects.Object3D;
import Objects.Sphere;
import vectors.Point;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class main {
    public static void main(String[] args) {
        List<Object3D> object3DList= new ArrayList<>();
        // Create the camera with FOV (e.g., 60 degrees)
        Camera camera = new Camera(
                new Point(0, 0, 0),   // origin
                new Point(0, 0, 0),   // rotation
                1,                    // near plane
                500,                    //Far plane
                800,                  // width
                600,                  // height
                60                    // field of view
        );

        // Create the scene
        Scene scene = new Scene(camera);

        // Add objects to the scene (slightly modify object positions)
        // In main.java:

        Sphere sphere = new Sphere(
                new Point(255, 0, 0),      // red color
                new Point(0, 0, 0),        // rotation
                new Point(0, 0, -5),       // position
                new Point(1, 1, 1),        // scale
                1.0                        // radius (smaller)
        );
        object3DList.add(sphere);

        Sphere sphere2 = new Sphere(
                new Point(70,255, 0),      // red color
                new Point(0, 0, 0),        // rotation
                new Point(2, 1, -7),       // position
                new Point(1, 1, 1),        // scale
                1.5                    // radius (smaller)
        );
        object3DList.add(sphere2);

        for (Object3D object3D : object3DList) {
            scene.addObject(object3D);
        }


        // Render the scene and get the image
        BufferedImage image = scene.render();

        // Save the rendered image to a file
        try {
            ImageIO.write(image, "PNG", new File("scene_output.png"));
            System.out.println("Image saved as scene_output.png");
        } catch (IOException e) {
            System.out.println("Error saving the image: " + e.getMessage());
        }
    }

}