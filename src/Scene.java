import Lights.Light;
import Objects.Camera;
import Objects.Object3D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private List<Object3D> objects;
    private List<Light> lights;

    public List<Object3D> getObjects() {
        return objects;
    }

    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    // Constructor
    public Scene(Camera camera) {
        this.camera = camera;
        this.objects = new ArrayList<>();
        this.lights = new ArrayList<>();
    }

    // Add an object to the scene
    public void addObject(Object3D object) {
        objects.add(object);
        System.out.println("Objetct "+object+" added");
    }

    public void addLight(Light light) {
        this.lights.add(light);
        System.out.println("Light "+light+" added");
    }


    // Generate the image for the scene using the camera
    public BufferedImage render() {
        // Render the scene by casting rays from the camera
        camera.shot(objects, getLights());

        // Return the generated image from the camera
        return camera.getImage();
    }

}
