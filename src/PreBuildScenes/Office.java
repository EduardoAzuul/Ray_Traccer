package PreBuildScenes;

import Lights.Light;
import Lights.PointLight;
import Lights.SpotLight;
import Materials.Texture;
import Objects.ObjObject;
import Objects.Object3D;
import ResuableElements.TipicalMaterials;
import vectors.Vector3D;

import java.util.ArrayList;
import java.util.List;

/**
 * The Office class provides a preconfigured 3D scene
 * that simulates a decorated office environment with
 * various objects and lighting elements.
 */
public class Office {

    /**
     * Creates and returns a list of 3D objects representing the office scene.
     * @return List of Object3D to be rendered in the scene
     */
    public static List<Object3D> getObjects() {
        List<Object3D> object3DList = new ArrayList<>();

        // Common variables for object initialization
        String pathObj;
        Vector3D color, rotation, position, scale;
        Texture texture = new Texture("Textures/dark_wood.jpg");

        // Desk setup
        pathObj = "ObjFiles/OfficeObjs/desk.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(0, -25, -55);
        scale = new Vector3D(3.0, 3.0, 3.0);
        ObjObject desk = new ObjObject(TipicalMaterials.DARK_WOOD, color, rotation, position, scale, pathObj);
        desk.setTexture(texture);
        object3DList.add(desk);

        // Plant object
        pathObj = "ObjFiles/OfficeObjs/objPot.obj";
        rotation = new Vector3D(0, 30, 0);
        position = new Vector3D(-35, -25, -40);
        scale = new Vector3D(4.0, 4.0, 4.0);
        object3DList.add(new ObjObject(TipicalMaterials.LIGHT_LEAF, color, rotation, position, scale, pathObj));

        // Pot (cube-shaped)
        pathObj = "ObjFiles/Objs/cube.obj";
        rotation = new Vector3D(0, -30, 180);
        scale = new Vector3D(10,12,10);
        ObjObject pot = new ObjObject(TipicalMaterials.MARBLE, color, rotation, position, scale, pathObj);
        object3DList.add(pot);

        // Decorative glass boar statue
        pathObj = "ObjFiles/OfficeObjs/Jabali.obj";
        rotation = new Vector3D(0, 160, 0);
        position = new Vector3D(-15, -3, -50);
        scale = new Vector3D(0.6, 0.6, 0.6);
        object3DList.add(new ObjObject(TipicalMaterials.GLASS_CLEAR, color, rotation, position, scale, pathObj));

        // Framed art - splash art
        texture = new Texture("Textures/splash_Art.jpg");
        pathObj = "ObjFiles/Objs/cube.obj";
        rotation = new Vector3D(0, 30, 0);
        position = new Vector3D(-10, 20, -88);
        scale = new Vector3D(30,15,5);
        ObjObject painting1 = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
        painting1.setTexture(texture);
        object3DList.add(painting1);

        // Framed map
        texture = new Texture("Textures/Mapa.jpg");
        rotation = new Vector3D(0, -30, 180);
        position = new Vector3D(45, 20, -60);
        scale = new Vector3D(2,15,20);
        ObjObject painting2 = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
        painting2.setTexture(texture);
        object3DList.add(painting2);

        // Desk lamp with gold material
        pathObj = "ObjFiles/OfficeObjs/desk_lamp.obj";
        texture = new Texture("Textures/Gold.jpg");
        rotation = new Vector3D(0, -120, 0);
        position = new Vector3D(20, -4, -55);
        scale = new Vector3D(80,80,80);
        ObjObject lamp = new ObjObject(TipicalMaterials.GOLD, color, rotation, position, scale, pathObj);
        lamp.setTexture(texture);
        object3DList.add(lamp);

        // Coffee mug with shark texture
        pathObj = "ObjFiles/OfficeObjs/Tasa.obj";
        texture = new Texture("Textures/Tiburon.jpg");
        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(16, -3, -52);
        scale = new Vector3D(1.2 ,1.2,1.2);
        ObjObject mug = new ObjObject(TipicalMaterials.MARBLE_YELLOW, color, rotation, position, scale, pathObj);
        mug.setTexture(texture);
        object3DList.add(mug);

        // Decorative transparent sphere
        pathObj = "ObjFiles/Objs/sphere.obj";
        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(11.2, -0.5, -45);
        scale = new Vector3D(2,2,2);
        ObjObject sphere = new ObjObject(TipicalMaterials.GLASS_CLEAR, color, rotation, position, scale, pathObj);
        object3DList.add(sphere);

        // Chair setup
        pathObj = "ObjFiles/OfficeObjs/Chair_low.obj";
        rotation = new Vector3D(0, 100, 0);
        position = new Vector3D(15, -25, -25);
        scale = new Vector3D(30.0, 30.0, 30.0);
        object3DList.add(new ObjObject(TipicalMaterials.LEATHER_BLACK, color, rotation, position, scale, pathObj));

        // Floor setup
        pathObj = "ObjFiles/Objs/square.obj";
        texture = new Texture("Textures/piso_cartago.jpg");
        color = new Vector3D(233, 31, 91);
        rotation = new Vector3D(-90, 0, 0);
        position = new Vector3D(0, -25, -45);
        scale = new Vector3D(80.0, 100.0, 80.0);
        ObjObject floor = new ObjObject(TipicalMaterials.MARBLE_WORN_BEIGE, color, rotation, position, scale, pathObj);
        floor.setTexture(texture);
        object3DList.add(floor);

        // Ceiling
        rotation = new Vector3D(90, 0, 0);
        position = new Vector3D(0, 55, -45);
        object3DList.add(new ObjObject(TipicalMaterials.PAINT_WHITE_MATTE, color, rotation, position, scale, pathObj));

        // Left wall
        rotation = new Vector3D(0, 60, 0);
        position = new Vector3D(-75, 0, -45);
        object3DList.add(new ObjObject(TipicalMaterials.PAINT_LIGHT_BLUE, color, rotation, position, scale, pathObj));

        // Right wall
        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(75, 0, -45);
        object3DList.add(new ObjObject(TipicalMaterials.PAINT_LIGHT_BLUE, color, rotation, position, scale, pathObj));

        // Back wall with custom clay texture
        texture = new Texture("Textures/ClayPlasteres.jpg");
        rotation = new Vector3D(0, 30, 0);
        position = new Vector3D(0, 0, -90);
        ObjObject backWall = new ObjObject(TipicalMaterials.PAINT_BLUE_MATTE, color, rotation, position, scale, pathObj);
        backWall.setTexture(texture);
        object3DList.add(backWall);

        return object3DList;
    }

    /**
     * Creates and returns a list of lights used in the office scene.
     * @return List of Light objects, including PointLights and SpotLights
     */
    public static List<Light> getLights() {
        List<Light> lights = new ArrayList<>();

        Vector3D lightPosition;
        Vector3D lightColor;

        // General white ceiling light
        lightPosition = new Vector3D(10, 30, -20);
        lightColor = new Vector3D(255, 255, 255);
        lights.add(new PointLight(lightPosition, lightColor, 30));

        // Green-tinted light
        lightPosition = new Vector3D(-10, 30, -30);
        lightColor = new Vector3D(140, 255, 140);
        lights.add(new PointLight(lightPosition, lightColor, 10));

        // Pink-tinted light
        lightPosition = new Vector3D(-10, 30, -40);
        lightColor = new Vector3D(235, 81, 141);
        lights.add(new PointLight(lightPosition, lightColor, 20));

        // Orange-yellow warm ceiling light
        lightPosition = new Vector3D(0, 40, -60);
        lightColor = new Vector3D(236, 178, 80);
        lights.add(new PointLight(lightPosition, lightColor, 20));

        // Small point light near the desk
        lightPosition = new Vector3D(-20, -10, -15);
        lightColor = new Vector3D(236, 178, 80);
        lights.add(new PointLight(lightPosition, lightColor, 2));

        // Desk lamp spotlight
        lightPosition = new Vector3D(20, 10, -55);
        lightColor = new Vector3D(236, 178, 80);
        Vector3D lightDirection = new Vector3D(-100, -100, 0);
        lights.add(new SpotLight(lightPosition, lightColor, 40, 0.5, 20, lightDirection));

        return lights;
    }
}
