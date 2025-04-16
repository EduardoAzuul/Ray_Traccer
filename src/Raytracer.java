import Obj_Reader.ObjReader;
import Objects.*;
import Objects.ObjObject;
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


        String pathObj = "Objs/cube.obj";
        Vector3D color = new Vector3D(250.0, 1.0, 1.0);
        Vector3D rotation = new Vector3D();
        Vector3D position = new Vector3D(0,0,-25);




        Vector3D scale = new Vector3D(1.0, 1.0, 1.0);

        //Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, double radius
        /*Sphere sphere = new Sphere(color,rotation,position,scale,5 );

        object3DList.add(sphere);*/
        //Vector3D color, Vector3D rotation, Vector3D origin,
        //                    Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3

        Triangle triangle = new Triangle(
                color,
                rotation,
                position,
                scale,
                new Vector3D(1.000000, -1.000000, -24.000000),
                new Vector3D(-1.000000, -1.000000, -24.000000),
                new Vector3D(-1.000000, -1.000000, -26.000000)

        );
        object3DList.add(triangle);


        ObjObject objObject= new ObjObject(color,rotation,position,scale,pathObj);

        //object3DList.add(objObject);

        for (Object3D object3D : object3DList) {
            scene.addObject(object3D);

        }
        System.out.println(scene.toString());


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