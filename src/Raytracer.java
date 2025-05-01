import Lights.SpotLight;
import Lights.PointLight;
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
                    3840,                    // width
                    2160,                    // height
                    60                      // field of view
            );

            // Create the scene
            Scene scene = new Scene(camera);

            // Add objects to the scene
            String pathObj = "Objs/cube.obj";
            File objFile = new File(pathObj);
            if (!objFile.exists()) {
                System.err.println("Warning: OBJ file not found at " + objFile.getAbsolutePath());
            }

            Vector3D color = new Vector3D(20.0, 100.0, 185.0);
            Vector3D rotation = new Vector3D(0,0,0);
            Vector3D position = new Vector3D(0, 0, -100);
            Vector3D scale = new Vector3D(1.0, 1.0, 1.0);


            // Add OBJ object
            //ObjObject objObject = new ObjObject(color, rotation, position, scale, pathObj);
            //object3DList.add(objObject);

            pathObj="Objs/Angle.obj";
            color = new Vector3D(255,252,240);

            position = new Vector3D(0,-25, -50);
            rotation = new Vector3D(0,20, 0);
            scale = new Vector3D(8.0, 8.0, 4.0);
            ObjObject objObject2 = new ObjObject(color, rotation, position, scale, pathObj);
            object3DList.add(objObject2);



            // Add all objects to scene
            for (Object3D object3D : object3DList) {
                scene.addObject(object3D);
                System.out.println("Added object: " + object3D);
            }


            /************************************************************/
            /********************   LIGHTS   ****************************/

            Vector3D lightPosition = new Vector3D(0,0,0);
            Vector3D lightColor = new Vector3D(255,255,255);
            Vector3D lightdirection = new Vector3D(0,0,-1);
            SpotLight spotLight = new SpotLight(
                    lightPosition,lightColor, 0.5, 2,45, lightdirection
            );

            //scene.addLight(spotLight);

            lightPosition = new Vector3D(10,20,-15);

            lightColor = new Vector3D(255,255,255);
            PointLight pointLight= new PointLight (lightPosition,lightColor, 65);

            scene.addLight(pointLight);

            lightPosition = new Vector3D(-10,20,-15);
            lightColor = new Vector3D(0,0,255);
            PointLight pointLight2 = new PointLight (lightPosition,lightColor, 30);
            scene.addLight(pointLight2);


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