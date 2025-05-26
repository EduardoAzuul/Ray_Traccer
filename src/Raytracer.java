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
        // Formatter to print timestamps in logs
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Starting render at: " + LocalDateTime.now().format(formatter));

        // Record the start time of the whole process for total duration
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Start timer for loading 3D objects
        LocalDateTime loadObjStart = LocalDateTime.now();

        try {
            // --- CAMERA SETUP ---
            // Create a camera located at (0,0,0) with no rotation,
            // near clipping plane at 1.5 units,
            // far clipping plane at 600 units,
            // image resolution of 4096x2160 pixels,
            // and field of view (FOV) of 60 degrees.
            Camera camera = new Camera(
                    new Vector3D(0, 0, 0),   // camera position in world space
                    new Vector3D(0, 0, 0),   // camera rotation (Euler angles)
                    1.5,                     // near clipping plane distance
                    600,                     // far clipping plane distance
                    4096,                    // image width in pixels
                    2160,                    // image height in pixels
                    60                       // vertical field of view in degrees
            );

            // Set number of light bounces for reflections/refractions
            camera.setBounces(3);

            // --- SCENE SETUP ---
            // Create the scene using the camera
            Scene scene = new Scene(camera);

            /**********************************************************/
            /******************** OBJECTS ****************************/

            // Retrieve a list of 3D objects from the pre-built MuseumAngels scene
            List<Object3D> object3DList = MuseumAngels.getObjects();

            // Add each object into the scene
            for (Object3D object3D : object3DList) {
                scene.addObject(object3D);
            }

            // Log how long loading the objects took
            LocalDateTime loadObjEnd = LocalDateTime.now();
            Duration loadObjDuration = Duration.between(loadObjStart, loadObjEnd);
            System.out.println("Time to load OBJ objects: " + loadObjDuration.toMillis() + " milliseconds");

            /************************************************************/
            /******************** LIGHTS ****************************/

            // Start timer for adding lights
            LocalDateTime loadLightsStart = LocalDateTime.now();

            // Retrieve a list of lights from the MuseumAngels scene
            List<Light> lights = MuseumAngels.getLights();

            // Add each light to the scene
            for (Light light : lights) {
                scene.addLight(light);
            }

            // Log how long adding lights took
            LocalDateTime loadLightsEnd = LocalDateTime.now();
            Duration loadLightsDuration = Duration.between(loadLightsStart, loadLightsEnd);
            System.out.println("Time to load lights: " + loadLightsDuration.toMillis() + " milliseconds");

            /************************************************************/

            // Start timer for rendering the scene
            LocalDateTime renderStart = LocalDateTime.now();

            System.out.println("Rendering scene...");
            // Render the scene to an image buffer
            BufferedImage image = scene.render();

            // Apply a Gaussian blur denoiser to reduce noise/artifacts
            image = Denoisser.gaussianBlur(image);

            // Set the output filename for the rendered image
            String filename = "scene_output.png";

            // Save the image as a PNG file on disk
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("Image saved as " + filename);

            // Log how long rendering took
            LocalDateTime renderEnd = LocalDateTime.now();
            Duration renderDuration = Duration.between(renderStart, renderEnd);
            System.out.println("Time to render scene: " + renderDuration.toMillis() + " milliseconds");

        } catch (Exception e) {
            // Handle any exceptions during the rendering pipeline and print the error stacktrace
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Rendering completed at: " + "scene_output.png");

        // Log total elapsed time for the entire rendering process
        Duration totalDuration = Duration.between(currentDateTime, LocalDateTime.now());
        System.out.println("Total duration: " + totalDuration.toMillis() + " milliseconds");
    }
}
