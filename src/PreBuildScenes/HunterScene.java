package PreBuildScenes;

import Materials.Texture;
import Objects.ObjObject;
import Objects.Object3D;
import ResuableElements.TipicalMaterials;
import vectors.Vector3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pre-built scene containing 3D objects,
 * specifically one that includes a model named "hunter.obj".
 * This class is used to generate and provide a list of objects for rendering.
 *
 * <p>The scene includes textured and transformed 3D objects built from OBJ files.</p>
 *
 * @author José Eduardo Moreno Paredes
 */
public class HunterScene {

    /**
     * Returns a list of objects to be included in the "Hunter Scene".
     *
     * @return A list of {@code Object3D} instances ready to be rendered.
     */
    public static List<Object3D> getObjects() {
        List<Object3D> object3DList = new ArrayList<>();

        String pathObj;
        Vector3D color, rotation, position, scale;
        Texture texture;

        // Load the hunter model from an OBJ file
        pathObj = "ObjFiles/Objs/hunter.obj";
        color = new Vector3D(20.0, 100.0, 185.0);       // Base color for the object
        rotation = new Vector3D(-90, 45, 0);            // Orientation in degrees (x, y, z)
        position = new Vector3D(0, -20, -50);           // Position in world space
        scale = new Vector3D(1, 1, 1);                  // Scale factor (uniform)

        // Create the object using a predefined white concrete material
        ObjObject hunterModel = new ObjObject(
                TipicalMaterials.CONCRETE_WHITE,
                color,
                rotation,
                position,
                scale,
                pathObj
        );

        // Apply a texture to the object
        texture = new Texture("Textures/concreto.jpg");
        hunterModel.setTexture(texture);

        // Add the object to the scene
        object3DList.add(hunterModel);

        return object3DList;
    }
}
