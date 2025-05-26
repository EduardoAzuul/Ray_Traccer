package PreBuildScenes;

import Lights.Light;
import Lights.PointLight;
import Materials.Texture;
import Objects.Object3D;
import Objects.ObjObject;
import ResuableElements.TipicalMaterials;
import ResuableElements.TipicalObj;
import vectors.Vector3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pre-built 3D scene resembling a museum setup,
 * including architectural elements (walls, columns, floor, ceiling),
 * carpets, and a grid of angel statues.
 *
 * <p>It also defines the lighting configuration using point lights
 * placed at strategic positions.</p>
 *
 * @author José Eduardo Moreno Paredes
 */
public class MuseumAngels {

    /**
     * Constructs and returns a list of 3D objects that form the "Museum Angels" scene.
     *
     * @return a list of {@code Object3D} objects composing the scene.
     */
    public static List<Object3D> getObjects() {
        List<Object3D> object3DList = new ArrayList<>();
        String pathObj;
        Vector3D color, rotation, position, scale;
        Texture texture;

        // Red carpet in the center
        pathObj = "ObjFiles/Objs/cube.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(-90, 0, 0);
        position = new Vector3D(0, -20.5, -50);
        scale = new Vector3D(10.0, 140.0, 1.0);
        ObjObject redCarpet = new ObjObject(TipicalMaterials.CLOTH_WOOL_RED, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/tela_roja.png");
        redCarpet.setTexture(texture);
        object3DList.add(redCarpet);

        // Yellow carpets on both sides
        position = new Vector3D(11, -20.6, -50);
        scale = new Vector3D(2.0, 140.0, 1.0);
        ObjObject yellowCarpet1 = new ObjObject(TipicalMaterials.CLOTH_YELLOW, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/tela_blanca.jpg");
        yellowCarpet1.setTexture(texture);
        object3DList.add(yellowCarpet1);

        position = new Vector3D(-11, -20.6, -50);
        ObjObject yellowCarpet2 = new ObjObject(TipicalMaterials.CLOTH_YELLOW, color, rotation, position, scale, pathObj);
        yellowCarpet2.setTexture(texture);
        object3DList.add(yellowCarpet2);

        // Floor
        pathObj = "ObjFiles/Objs/square.obj";
        color = new Vector3D(233, 31, 91);
        rotation = new Vector3D(-90, 0, 0);
        position = new Vector3D(0, -20, -75);
        scale = new Vector3D(90.0, 100.0, 80.0);
        ObjObject floor = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/piso_mosaicos.jpg");
        floor.setTexture(texture);
        object3DList.add(floor);

        // Ceiling
        rotation = new Vector3D(90, 0, 0);
        position = new Vector3D(0, 55, -60);
        texture = new Texture("Textures/ClayPlasteres.jpg");
        ObjObject ceiling = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        ceiling.setTexture(texture);
        object3DList.add(ceiling);

        // Left wall
        rotation = new Vector3D(0, 90, 0);
        position = new Vector3D(-75, 0, -60);
        ObjObject wallLeft = new ObjObject(TipicalMaterials.MARBLE_OLD_BEIGE, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/bricks_white.jpg");
        wallLeft.setTexture(texture);
        object3DList.add(wallLeft);

        // Back wall
        rotation = new Vector3D(0, 0, 0);
        position = new Vector3D(0, 0, -140);
        ObjObject wallBack = new ObjObject(TipicalMaterials.MARBLE_OLD_BEIGE, color, rotation, position, scale, pathObj);
        wallBack.setTexture(texture);
        object3DList.add(wallBack);

        // Right wall
        rotation = new Vector3D(0, -90, 0);
        position = new Vector3D(75, 0, -60);
        ObjObject wallRight = new ObjObject(TipicalMaterials.MARBLE_OLD_BEIGE, color, rotation, position, scale, pathObj);
        wallRight.setTexture(texture);
        object3DList.add(wallRight);

        // Decorative columns
        pathObj = "ObjFiles/Objs/columna.obj";
        color = new Vector3D(255, 255, 255);
        rotation = new Vector3D(0, 90, 0);
        scale = new Vector3D(5, 7, 5);
        texture = new Texture("Textures/concreto.jpg");

        position = new Vector3D(-55, -30, -80);
        ObjObject column1 = new ObjObject(TipicalMaterials.CONCRETE_REDDISH, color, rotation, position, scale, pathObj);
        column1.setTexture(texture);
        object3DList.add(column1);

        position = new Vector3D(55, -30, -80);
        ObjObject column2 = new ObjObject(TipicalMaterials.CONCRETE_REDDISH, color, rotation, position, scale, pathObj);
        column2.setTexture(texture);
        object3DList.add(column2);

        // Angel statue grid
        object3DList.addAll(TipicalObj.createAngelGrid());

        return object3DList;
    }

    /**
     * Returns a list of light sources to illuminate the "Museum Angels" scene.
     * Point lights are used at different positions and colors.
     *
     * @return a list of {@code Light} instances.
     */
    public static List<Light> getLights() {
        List<Light> lights = new ArrayList<>();

        Vector3D lightPosition;
        Vector3D lightColor;

        // Main white light from the right
        lightPosition = new Vector3D(10, 30, -20);
        lightColor = new Vector3D(255, 255, 255);
        lights.add(new PointLight(lightPosition, lightColor, 60));

        // Blue-tinted light from the left
        lightPosition = new Vector3D(-10, 30, -20);
        lightColor = new Vector3D(60, 110, 255);
        lights.add(new PointLight(lightPosition, lightColor, 30));

        // Warm yellow light from deeper into the room
        lightPosition = new Vector3D(-10, 20, -100);
        lightColor = new Vector3D(255, 220, 60);
        lights.add(new PointLight(lightPosition, lightColor, 25));

        // Duplicate for artistic symmetry (optional duplication)
        lights.add(new PointLight(lightPosition, lightColor, 25));

        return lights;
    }
}
