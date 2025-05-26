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
import java.util.List;

public class RaytracerForTests {
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

            String pathObj;
            Vector3D color, rotation, position, scale;
            Texture texture = null;

            pathObj = "ObjFiles/Objs/cube.obj";
            texture = new Texture("Textures/roble.jpg");
            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(0,-20,-50);
            scale = new Vector3D(300,0.5,300);
            ObjObject rocas2 = new ObjObject(TipicalMaterials.PLASTIC_GREEN, color, rotation, position, scale, pathObj);
            scene.addObject(rocas2);

            pathObj = "ObjFiles/Objs/cube.obj";
            texture = new Texture("Textures/roble.jpg");
            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(0,-20,-100);
            scale = new Vector3D(300,300,0.5);
            ObjObject rocas3 = new ObjObject(TipicalMaterials.PLASTIC_BLUE, color, rotation, position, scale, pathObj);
            scene.addObject(rocas3);

            pathObj = "ObjFiles/Objs/sphere.obj";

            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(-30,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera1 = new ObjObject(color, rotation, position, scale, pathObj);
            //rocas2.setTexture(texture);
            scene.addObject(Esfera1);

            pathObj = "ObjFiles/Objs/sphere.obj";

            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(-15,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera2 = new ObjObject(TipicalMaterials.CONCRETE_REDDISH,color, rotation, position, scale, pathObj);
            //rocas2.setTexture(texture);
            scene.addObject(Esfera2);

            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(0,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera3 = new ObjObject(TipicalMaterials.SHINY_METAL,color, rotation, position, scale, pathObj);
            //rocas2.setTexture(texture);
            scene.addObject(Esfera3);

            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(15,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera4 = new ObjObject(TipicalMaterials.GLASS_CLEAR,color, rotation, position, scale, pathObj);
            //rocas2.setTexture(texture);
            scene.addObject(Esfera4);

            texture = new Texture("Textures/splash_Art.jpg");
            color = new Vector3D(255, 255, 255);
            rotation = new Vector3D(0, 0, 0);
            position = new Vector3D(30,0,-50);
            scale = new Vector3D(5,5,5);
            ObjObject Esfera5 = new ObjObject(TipicalMaterials.BAMBOO,color, rotation, position, scale, pathObj);
            //Esfera5.setTexture(texture);
            scene.addObject(Esfera5);



            // Log the duration for loading objects
            LocalDateTime loadObjEnd = LocalDateTime.now();
            Duration loadObjDuration = Duration.between(loadObjStart, loadObjEnd);
            System.out.println("Time to load OBJ objects: " + loadObjDuration.toMillis() + " milliseconds");

            /************************************************************/
            /********************   LIGHTS   ****************************/


            LocalDateTime loadLightsStart = LocalDateTime.now();

            Vector3D lightPosition;
            Vector3D lightColor;

            /*lightPosition = new Vector3D(-40, 20, -30);
            lightColor = new Vector3D(247,190,69);
            scene.addLight(new PointLight(lightPosition, lightColor, 60));

            lightPosition = new Vector3D(10, 20, 0);
            lightColor = new Vector3D(247,190,69);
            scene.addLight(new PointLight(lightPosition, lightColor, 60));*/

            lightPosition = new Vector3D(50, 50, -30);
            lightColor = new Vector3D(255,255,255);
            scene.addLight(new PointLight(lightPosition, lightColor, 60));

            lightPosition = new Vector3D(-50, 50, -30);
            lightColor = new Vector3D(255,255,255);
            scene.addLight(new PointLight(lightPosition, lightColor, 60));
            //scene.addLight(new DirectionalLight(lightColor,  1,new Vector3D(-1,-1,-1)));



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
