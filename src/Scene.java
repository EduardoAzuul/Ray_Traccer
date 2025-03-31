import Objects.Camera;
import Objects.Object3D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private List<Object3D> objects;

    // Constructor
    public Scene(Camera camera) {
        this.camera = camera;
        this.objects = new ArrayList<>();
    }

    // Add an object to the scene
    public void addObject(Object3D object) {
        objects.add(object);
        System.out.println("Objetct "+object+" added");
    }

    // Generate the image for the scene using the camera
    public BufferedImage render() {
        // Render the scene by casting rays from the camera
        camera.shot(objects);

        // Return the generated image from the camera
        return camera.getImage();
    }

}
