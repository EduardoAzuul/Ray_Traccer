import Lights.Light;
import Objects.Camera;
import Objects.Object3D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 3D scene containing objects, lights, and a camera.
 * This class manages all elements of a 3D scene and handles rendering.
 *
 * @author José Eduardo Moreno
 */
public class Scene {
    /** The camera viewing the scene */
    private Camera camera;

    /** List of 3D objects in the scene */
    private List<Object3D> objects;

    /** List of light sources in the scene */
    private List<Light> lights;

    /**
     * Gets the list of objects in the scene.
     * @return List of Object3D instances
     */
    public List<Object3D> getObjects() {
        return objects;
    }

    /**
     * Sets the list of objects in the scene.
     * @param objects List of Object3D instances to set
     */
    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    /**
     * Gets the list of lights in the scene.
     * @return List of Light instances
     */
    public List<Light> getLights() {
        return lights;
    }

    /**
     * Sets the list of lights in the scene.
     * @param lights List of Light instances to set
     */
    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    /**
     * Constructs a new Scene with the specified camera.
     * Initializes empty lists for objects and lights.
     *
     * @param camera The camera to view the scene
     */
    public Scene(Camera camera) {
        this.camera = camera;
        this.objects = new ArrayList<>();
        this.lights = new ArrayList<>();
    }

    /**
     * Adds a 3D object to the scene.
     * @param object The Object3D to add
     */
    public void addObject(Object3D object) {
        objects.add(object);
        System.out.println("Object "+object+" added");
    }

    /**
     * Adds a light source to the scene.
     * @param light The Light to add
     */
    public void addLight(Light light) {
        this.lights.add(light);
        System.out.println("Light "+light+" added");
    }

    /**
     * Renders the scene by having the camera capture an image.
     * The camera casts rays through all objects and lights in the scene.
     *
     * @return The rendered image as a BufferedImage
     */
    public BufferedImage render() {
        // Render the scene by casting rays from the camera
        camera.shot(objects, getLights());

        // Return the generated image from the camera
        return camera.getImage();
    }
}