import Objects.Camera;
import Objects.Object3D;
import Objects.Sphere;
import Objects.Triangle;
import vectors.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Raytracer {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now() );
        List<Object3D> object3DList= new ArrayList<>();
        // Create the camera with FOV (e.g., 60 degrees)
        Camera camera = new Camera(
                new Vector3D(0, 0, 0),   // origin
                new Vector3D(0, 0, 0),   // rotation
                1.5,                    // near plane
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
                new Vector3D(255, 0, 0),      // red color
                new Vector3D(0, 0, 0),        // rotation
                new Vector3D(0, 0, -1.2),       // position
                new Vector3D(1, 1, 1),        // scale
                1                       // radius
        );
        //object3DList.add(sphere);

        Sphere sphere2 = new Sphere(
                new Vector3D(70,255, 0),      // red color
                new Vector3D(0, 0, 0),        // rotation
                new Vector3D(2, 1, -12),       // position
                new Vector3D(1, 1, 1),        // scale
                1.5                    // radius
        );
        //object3DList.add(sphere2);
        //Vector3D color, Vector3D rotation, Vector3D origin,
        // Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3
        Triangle triangle = new Triangle(
                new Vector3D(70,255, 0),      //color
                new Vector3D(),
                new Vector3D(0,0,-50),
                new Vector3D(),
                new Vector3D(0,10,0),
                new Vector3D(10,0,0),
                new Vector3D(0,0,0)
        );
        object3DList.add(triangle);

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
        System.out.println(LocalDateTime.now() );
    }

}