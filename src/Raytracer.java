import Lights.DirectionalLight;
import Lights.SpotLight;
import Lights.PointLight;
import Objects.*;
import Objects.ObjObject;
import vectors.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Raytracer {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Starting render at: " + LocalDateTime.now().format(formatter));
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime loadObjStart = LocalDateTime.now();
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
            Vector3D position = new Vector3D(15, 0, -20);
            Vector3D scale = new Vector3D(1.0, 1.0, 1.0);

            // Add OBJ object
            //ObjObject objObject = new ObjObject(color, rotation, position, scale, pathObj);
            //object3DList.add(objObject);

            pathObj = "Objs/SmallTeapot.obj";
            color = new Vector3D(255,252,240);
            position = new Vector3D(0,0, -30);
            rotation = new Vector3D(0,0, 0);
            scale = new Vector3D(8.0, 8.0, 8.0);
            ObjObject objObject2 = new ObjObject(color, rotation, position, scale, pathObj);
            //object3DList.add(objObject2);

            pathObj = "Objs/Angle.obj";
            rotation = new Vector3D(0,0,0);
            position = new Vector3D(0,-20, -50);
            objObject2 = new ObjObject(color, rotation, position, scale, pathObj);
            object3DList.add(objObject2);

            pathObj = "Objs/square.obj";
            color = new Vector3D(233,31,91);
            rotation = new Vector3D(-90,0,0);
            position = new Vector3D(0,-20, -50);
            scale = new Vector3D(80.0, 80.0, 80.0);
            ObjObject objObject3 = new ObjObject(color,rotation,position,scale, pathObj);
            object3DList.add(objObject3);

            // Add all objects to scene
            for (Object3D object3D : object3DList) {
                scene.addObject(object3D);
                System.out.println("Added object: " + object3D);
            }

            // Log the duration for loading objects
            LocalDateTime loadObjEnd = LocalDateTime.now();
            Duration loadObjDuration = Duration.between(loadObjStart, loadObjEnd);
            System.out.println("Time to load OBJ objects: " + loadObjDuration.toMillis() + " milliseconds");

            /************************************************************/
            /********************   LIGHTS   ****************************/

            LocalDateTime loadLightsStart = LocalDateTime.now();

            Vector3D lightPosition = new Vector3D(0,0,0);
            Vector3D lightColor = new Vector3D(255,255,255);
            Vector3D lightdirection = new Vector3D(0,0,-1);
            SpotLight spotLight = new SpotLight(
                    lightPosition, lightColor, 60, 2, 45, lightdirection
            );

            //scene.addLight(spotLight);

            lightPosition = new Vector3D(10,20,-15);
            lightColor = new Vector3D(255,255,255);
            PointLight pointLight = new PointLight(lightPosition, lightColor, 60);
            scene.addLight(pointLight);

            lightPosition = new Vector3D(-10,20,-15);
            lightColor = new Vector3D(0,0,255);
            PointLight pointLight2 = new PointLight(lightPosition, lightColor, 30);
            scene.addLight(pointLight2);

            // Log the duration for adding lights
            LocalDateTime loadLightsEnd = LocalDateTime.now();
            Duration loadLightsDuration = Duration.between(loadLightsStart, loadLightsEnd);
            System.out.println("Time to load lights: " + loadLightsDuration.toMillis() + " milliseconds");

            /********************************************************/

            LocalDateTime renderStart = LocalDateTime.now();

            Vector3D lightDirection = new Vector3D(1,0,1);
            DirectionalLight directionalLight = new DirectionalLight(lightColor, 5, lightDirection);
            System.out.println(directionalLight.getDirection());
            //scene.addLight(directionalLight);

            // Render the scene
            System.out.println("Rendering scene...");
            BufferedImage image = scene.render();

            // Generate a timestamp-based filename
            String filename = "scene_output.png";

            // Save the rendered image
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("Image saved as " + filename);

            // Log the duration for rendering the scene
            LocalDateTime renderEnd = LocalDateTime.now();
            Duration renderDuration = Duration.between(renderStart, renderEnd);
            System.out.println("Time to render scene: " + renderDuration.toMillis() + " milliseconds");

        } catch (Exception e) {
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Rendering completed at: " + "scene_output.png");

        // Log the total duration
        Duration totalDuration = Duration.between(currentDateTime, LocalDateTime.now());
        System.out.println("Total duration: " + totalDuration.toMillis() + " milliseconds");
    }
}
