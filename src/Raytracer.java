import Denoisser.Denoisser;
import Lights.Light;
import Lights.PointLight;
import Materials.Texture;
import Objects.*;
import PreBuildScenes.HunterScene;
import PreBuildScenes.MuseumAngels;
import PreBuildScenes.Office;
import PreBuildScenes.MuseumAngels;
import ResuableElements.TipicalMaterials;
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

            // Create the camera with FOV
            Camera camera = new Camera(
                    new Vector3D(0, 0, 0),   // origin
                    new Vector3D(0, 0, 0),   // rotation
                    1.5,                    // near plane
                    600,                    // far plane
                    4096 ,                    // width
                    2160,                    // height
                    60                      // field of view
            );

            camera.setBounces(2);
            // Create the scene
            Scene scene = new Scene(camera);

            /**********************************************************/
            /******************** Objects ****************************/

            List<Object3D> object3DList = MuseumAngels.getObjects();
            for (Object3D object3D : object3DList) {
                scene.addObject(object3D);
            }




            // Log the duration for loading objects
            LocalDateTime loadObjEnd = LocalDateTime.now();
            Duration loadObjDuration = Duration.between(loadObjStart, loadObjEnd);
            System.out.println("Time to load OBJ objects: " + loadObjDuration.toMillis() + " milliseconds");

            /************************************************************/
            /********************   LIGHTS   ****************************/


            LocalDateTime loadLightsStart = LocalDateTime.now();

            List<Light> lights = MuseumAngels.getLights();
            for (Light light : lights) {
                scene.addLight(light);
            }

            //scene.setLights(lights);


            // Log the duration for adding lights
            LocalDateTime loadLightsEnd = LocalDateTime.now();
            Duration loadLightsDuration = Duration.between(loadLightsStart, loadLightsEnd);
            System.out.println("Time to load lights: " + loadLightsDuration.toMillis() + " milliseconds");

            /********************************************************/

            LocalDateTime renderStart = LocalDateTime.now();

            // Render the scene
            System.out.println("Rendering scene...");
            BufferedImage image = scene.render();
            image = Denoisser.gaussianBlur(image);
            /*********************** DENOISSER ********************************/



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
