import Denoisser.Denoisser;
import Lights.DirectionalLight;
import Lights.Light;
import Lights.PointLight;
import Materials.Texture;
import Objects.Camera;
import Objects.ObjObject;
import Objects.Object3D;
import PreBuildScenes.MuseumAngels;
import ResuableElements.TipicalMaterials;
import vectors.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RaytracerForTests {
    // This class is similar to Raytracer, but its purpose
    // is to generate images for presentation without
    // changing the scene code.

    public static void main(String[] args) {
        // DateTimeFormatter to format timestamps in console logs
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Log start time of rendering
        System.out.println("Starting render at: " + LocalDateTime.now().format(formatter));
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Start timer for loading objects
        LocalDateTime loadObjStart = LocalDateTime.now();

        try {
            // --- CAMERA SETUP ---
            // Create a camera located at (0,0,0) with no rotation,
            // near plane distance 1.5, far plane 600,
            // resolution 4096 x 2160 pixels,
            // and a field of view (FOV) of 60 degrees.
            Camera camera = new Camera(
                    new Vector3D(0, 0, 0),   // origin
                    new Vector3D(0, 0, 0),   // rotation (Euler angles)
                    1.5,                    // near clipping plane
                    600,                    // far clipping plane
                    4096 ,                  // image width in pixels
                    2160,                   // image height in pixels
                    60                      // vertical field of view in degrees
            );
            // Set the number of light bounces for reflections/refractions
            camera.setBounces(3);

            // --- SCENE CREATION ---
            // Initialize a new scene object with the camera
            Scene scene = new Scene(camera);

            // --- OBJECTS SETUP ---
            // Variables for object attributes
            String pathObj;
            Vector3D color, rotation, position, scale;

            // Add a large flat cube scaled mostly in X and Z for the floor-like surface
            pathObj = "ObjFiles/Objs/cube.obj";
            color = new Vector3D(226,90,141);  // RGB color for this object
            rotation = new Vector3D(0, 0, 0);  // no rotation
            position = new Vector3D(0,-20,-50); // placed below camera and far away in Z
            scale = new Vector3D(300,0.5,300);  // very wide and thin cube
            ObjObject rocas2 = new ObjObject(TipicalMaterials.PLASTIC_GREEN, color, rotation, position, scale, pathObj);
            scene.addObject(rocas2);  // add object to the scene

            // Another large cube with different scale acting as a wall or floor
            color = new Vector3D(255, 255, 255);  // white color
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(0,-20,-100);
            scale = new Vector3D(300,300,0.5);  // very thick in Y and X but thin in Z
            ObjObject rocas3 = new ObjObject(TipicalMaterials.PLASTIC_BLUE, color, rotation, position, scale, pathObj);
            scene.addObject(rocas3);

            // Load spheres at different positions with different materials and colors
            pathObj = "ObjFiles/Objs/sphere.obj";

            // Sphere 1, pink color, default material
            color = new Vector3D(226,90,141);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(-40,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera1 = new ObjObject(color, rotation, position, scale, pathObj);
            scene.addObject(Esfera1);

            // Sphere 2, yellow marble material
            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(-20,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera2 = new ObjObject(TipicalMaterials.MARBLE_YELLOW, color, rotation, position, scale, pathObj);
            scene.addObject(Esfera2);

            // Sphere 3, shiny metal material
            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(0,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera3 = new ObjObject(TipicalMaterials.SHINY_METAL, color, rotation, position, scale, pathObj);
            scene.addObject(Esfera3);

            // Sphere 4, glass clear material (transparent)
            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(20,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera4 = new ObjObject(TipicalMaterials.GLASS_CLEAR, color, rotation, position, scale, pathObj);
            scene.addObject(Esfera4);

            // Sphere 5, ruby material (red translucent)
            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(40,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera5 = new ObjObject(TipicalMaterials.RUBY, color, rotation, position, scale, pathObj);
            scene.addObject(Esfera5);

            // Log how long loading the objects took
            LocalDateTime loadObjEnd = LocalDateTime.now();
            Duration loadObjDuration = Duration.between(loadObjStart, loadObjEnd);
            System.out.println("Time to load OBJ objects: " + loadObjDuration.toMillis() + " milliseconds");

            // --- LIGHTS SETUP ---
            LocalDateTime loadLightsStart = LocalDateTime.now();

            Vector3D lightPosition;
            Vector3D lightColor;

            // Add three white point lights at different positions to illuminate the scene

            lightPosition = new Vector3D(50, 50, -30);
            lightColor = new Vector3D(255,255,255);  // white light
            scene.addLight(new PointLight(lightPosition, lightColor, 60));  // intensity/radius 60

            lightPosition = new Vector3D(-50, 50, -30);
            lightColor = new Vector3D(255,255,255);
            scene.addLight(new PointLight(lightPosition, lightColor, 60));

            lightPosition = new Vector3D(0, 50, -30);
            lightColor = new Vector3D(255,255,255);
            scene.addLight(new PointLight(lightPosition, lightColor, 60));

            // Log how long adding lights took
            LocalDateTime loadLightsEnd = LocalDateTime.now();
            Duration loadLightsDuration = Duration.between(loadLightsStart, loadLightsEnd);
            System.out.println("Time to load lights: " + loadLightsDuration.toMillis() + " milliseconds");

            // --- RENDERING ---
            LocalDateTime renderStart = LocalDateTime.now();

            System.out.println("Rendering scene...");
            // Render the scene to a BufferedImage using the raytracer
            BufferedImage image = scene.render();

            // Apply Gaussian blur denoiser to smooth out noise/artifacts
            image = Denoisser.gaussianBlur(image);

            // Create filename for the output image - fixed name here, could be timestamped
            String filename = "scene_output.png";

            // Write the rendered image to a PNG file
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("Image saved as " + filename);

            // Log how long the rendering took
            LocalDateTime renderEnd = LocalDateTime.now();
            Duration renderDuration = Duration.between(renderStart, renderEnd);
            System.out.println("Time to render scene: " + renderDuration.toMillis() + " milliseconds");

        } catch (Exception e) {
            // Catch any exception during the rendering process and print the error
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }

        // Print final completion message with output filename
        System.out.println("Rendering completed at: " + "scene_output.png");

        // Log total duration of the program execution
        Duration totalDuration = Duration.between(currentDateTime, LocalDateTime.now());
        System.out.println("Total duration: " + totalDuration.toMillis() + " milliseconds");
    }
}
