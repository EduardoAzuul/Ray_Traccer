import Lights.DirectionLight;
import Lights.Light;
import Objects.*;
import Objects.ObjObject;
import vectors.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Raytracer {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Starting render at: " + LocalDateTime.now().format(formatter));

        try {
            List<Object3D> object3DList = new ArrayList<>();

            // Create the camera with FOV
            Camera camera = new Camera(
                    new Vector3D(0, 0, 0),   // origin
                    new Vector3D(0, 0, 0),   // rotation
                    1.5,                    // near plane
                    500,                    // far plane
                    800,                    // width
                    600,                    // height
                    60                      // field of view
            );

            // Create the scene
            Scene scene = new Scene(camera);

            // Add objects to the scene
            String pathObj = "Objs/SmallTeapot.obj";
            File objFile = new File(pathObj);
            if (!objFile.exists()) {
                System.err.println("Warning: OBJ file not found at " + objFile.getAbsolutePath());
            }

            Vector3D color = new Vector3D(20.0, 100.0, 185.0);
            Vector3D rotation = new Vector3D(0,35,0);
            Vector3D position = new Vector3D(0, -5, -15);
            Vector3D scale = new Vector3D(2.0, 2.0, 2.0);


            // Add OBJ object
            ObjObject objObject = new ObjObject(color, rotation, position, scale, pathObj);
            object3DList.add(objObject);

            pathObj="Objs/square.obj";
            color = Vector3D.randomColor();

            position = new Vector3D(0,0, -25);
            scale = new Vector3D(8.0, 8.0, 1.0);
            ObjObject objObject2 = new ObjObject(color, rotation, position, scale, pathObj);
            object3DList.add(objObject2);






            // Add all objects to scene
            for (Object3D object3D : object3DList) {
                scene.addObject(object3D);
                System.out.println("Added object: " + object3D);
            }


            /************************************************************/
            /************************************************/

            Vector3D lightPosition = new Vector3D(0,0,0);
            Vector3D lightColor = new Vector3D(255,255,255);
            Vector3D lightdirection = new Vector3D(0,0,1);
            DirectionLight directionLight = new DirectionLight(
                    lightPosition,lightColor, 9.5, 4, lightdirection
            );

            scene.addLight(directionLight);

            //CAMERA INTERSECTION:
            //List<Object3D> object3DList, List<Light> lightList,
            //       Object3D objectHit, Vector3D origin, Vector3D direction

            // Render the scene
            System.out.println("Rendering scene...");
            BufferedImage image = scene.render();

            // Generate a timestamp-based filename
            String filename = "scene_output.png";

            // Save the rendered image
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("Image saved as " + filename);

        } catch (Exception e) {
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Rendering completed at: " + "scene_output.png");
    }
}