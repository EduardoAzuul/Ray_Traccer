package PreBuildScenes;

import Lights.Light;
import Lights.PointLight;
import Materials.Texture;
import Objects.ObjObject;
import Objects.Object3D;
import ResuableElements.TipicalMaterials;
import vectors.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Temple {

    // Method to generate and return a list of 3D objects that make up the temple scene
    public static List<Object3D> getObjects() {
        List<Object3D> object3DList = new ArrayList<>();
        String pathObj;
        Vector3D color, rotation, position, scale;
        Texture texture = null;

        // Water plane
        pathObj = "ObjFiles/Objs/square.obj";
        color = new Vector3D(255, 255, 255);
        rotation = new Vector3D(90, 0, 0);
        position = new Vector3D(0, -10, -20);
        scale = new Vector3D(120, 120, 120);
        ObjObject agua = new ObjObject(TipicalMaterials.DEEP_WATER, color, rotation, position, scale, pathObj);
        object3DList.add(agua);

        // Small tree object 1
        pathObj = "ObjFiles/templeObjs/tree.obj";
        texture = new Texture("Textures/gold.jpg");
        color = new Vector3D(255, 255, 255);
        rotation = new Vector3D(30, -15, 0);
        position = new Vector3D(-10, -20, -60);
        scale = new Vector3D(120, 120, 120);
        ObjObject roca = new ObjObject(TipicalMaterials.SAPPHIRE, color, rotation, position, scale, pathObj);
        object3DList.add(roca);

        // Small tree object 2
        rotation = new Vector3D(40, 10, 0);
        position = new Vector3D(-5, -10, -300);
        scale = new Vector3D(450, 450, 450);
        ObjObject roca3 = new ObjObject(TipicalMaterials.SAPPHIRE, color, rotation, position, scale, pathObj);
        object3DList.add(roca3);

        // Small tree object 3
        rotation = new Vector3D(30, 15, 0);
        position = new Vector3D(20, -20, -60);
        scale = new Vector3D(80, 80, 80);
        ObjObject roca2 = new ObjObject(TipicalMaterials.SAPPHIRE, color, rotation, position, scale, pathObj);
        object3DList.add(roca2);

        // Wall made of rock
        pathObj = "ObjFiles/templeObjs/muroRoca.obj";
        texture = new Texture("Textures/muroRoca.jpg");
        rotation = new Vector3D(-15, -30, 0);
        position = new Vector3D(20, -40, -90);
        scale = new Vector3D(50, 30, 30);
        ObjObject muro1 = new ObjObject(TipicalMaterials.CONCRETE_OFFWHITE, color, rotation, position, scale, pathObj);
        muro1.setTexture(texture);
        object3DList.add(muro1);

        // Archway object
        pathObj = "ObjFiles/templeObjs/arcoRoca.obj";
        texture = new Texture("Textures/arcoRoca.png");
        rotation = new Vector3D(0, 0, 0);
        position = new Vector3D(0, 10, -30);
        scale = new Vector3D(8, 8, 8);
        ObjObject arco = new ObjObject(TipicalMaterials.CONCRETE_OFFWHITE, color, rotation, position, scale, pathObj);
        arco.setTexture(texture);
        object3DList.add(arco);

        // Left wall section
        pathObj = "ObjFiles/templeObjs/muroRoca.obj";
        texture = new Texture("Textures/muroRoca.jpg");
        rotation = new Vector3D(0, 15, 0);
        position = new Vector3D(-80, -20, -250);
        scale = new Vector3D(100, 160, 100);
        ObjObject coastScan = new ObjObject(TipicalMaterials.CONCRETE_REDDISH, color, rotation, position, scale, pathObj);
        coastScan.setTexture(texture);
        object3DList.add(coastScan);

        // Right wall section
        rotation = new Vector3D(0, -15, 0);
        position = new Vector3D(80, 0, -250);
        scale = new Vector3D(100, 150, 100);
        ObjObject coastScan2 = new ObjObject(TipicalMaterials.CONCRETE_WHITE, color, rotation, position, scale, pathObj);
        coastScan2.setTexture(texture);
        object3DList.add(coastScan2);

        // Stairs
        pathObj = "ObjFiles/templeObjs/escaleras.obj";
        texture = new Texture("Textures/gold.jpg");
        rotation = new Vector3D(0, 60, 0);
        position = new Vector3D(35, 5, -185);
        scale = new Vector3D(30, 40, 30);
        ObjObject escaleras = new ObjObject(TipicalMaterials.GOLD, color, rotation, position, scale, pathObj);
        object3DList.add(escaleras);

        // Main temple object
        pathObj = "ObjFiles/templeObjs/temploAzteca.obj";
        position = new Vector3D(40, 15, -190);
        scale = new Vector3D(0.6, 0.9, 0.6);
        ObjObject templo = new ObjObject(TipicalMaterials.GOLD, color, rotation, position, scale, pathObj);
        templo.setTexture(texture);
        object3DList.add(templo);

        // Plant objects (senecio_1.obj)
        pathObj = "ObjFiles/templeObjs/senecio_1.obj";
        rotation = new Vector3D(0, 0, 0);
        position = new Vector3D(0, -20, -50);
        scale = new Vector3D(5, 5, 5);
        object3DList.add(new ObjObject(TipicalMaterials.YOUNG_SPROUT, color, rotation, position, scale, pathObj));

        rotation = new Vector3D(30, 15, 0);
        position = new Vector3D(-30, -16, -40);
        object3DList.add(new ObjObject(TipicalMaterials.YOUNG_SPROUT, color, rotation, position, scale, pathObj));

        rotation = new Vector3D(-30, -15, 0);
        position = new Vector3D(50, -20, -60);
        scale = new Vector3D(7, 7, 7);
        object3DList.add(new ObjObject(TipicalMaterials.YOUNG_SPROUT, color, rotation, position, scale, pathObj));

        // Bamboo objects
        pathObj = "ObjFiles/templeObjs/Bamboo.obj";
        rotation = new Vector3D(10, 0, 0);
        position = new Vector3D(-30, -12, -60);
        scale = new Vector3D(0.2, 0.2, 0.2);
        object3DList.add(new ObjObject(TipicalMaterials.BAMBOO, color, rotation, position, scale, pathObj));

        rotation = new Vector3D(8, 0, 5);
        position = new Vector3D(-40, -16, -55);
        object3DList.add(new ObjObject(TipicalMaterials.BAMBOO, color, rotation, position, scale, pathObj));

        position = new Vector3D(-35, -8, -70);
        object3DList.add(new ObjObject(TipicalMaterials.BAMBOO, color, rotation, position, scale, pathObj));

        position = new Vector3D(-50, -15, -70);
        object3DList.add(new ObjObject(TipicalMaterials.BAMBOO, color, rotation, position, scale, pathObj));

        // Leafy plant decorations
        pathObj = "ObjFiles/templeObjs/planta.obj";
        texture = new Texture("Textures/planta.jpg");
        rotation = new Vector3D(10, 0, 0);
        position = new Vector3D(-30, -12, -60);
        scale = new Vector3D(0.3, 0.3, 0.3);
        ObjObject planta1 = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        planta1.setTexture(texture);
        object3DList.add(planta1);

        position = new Vector3D(40, -11, -40);
        ObjObject planta2 = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        planta2.setTexture(texture);
        object3DList.add(planta2);

        rotation = new Vector3D(-10, 10, 0);
        position = new Vector3D(50, -11, -35);
        ObjObject planta3 = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        planta3.setTexture(texture);
        object3DList.add(planta3);

        position = new Vector3D(15, -15, -15);
        ObjObject planta4 = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        planta4.setTexture(texture);
        object3DList.add(planta4);

        // Rock objects
        pathObj = "ObjFiles/templeObjs/rocas.obj";
        texture = new Texture("Textures/Rocky_grass.jpg");
        rotation = new Vector3D(0, 90, 0);
        position = new Vector3D(-60, -20, -110);
        scale = new Vector3D(20, 20, 20);
        ObjObject rocas = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
        rocas.setTexture(texture);
        object3DList.add(rocas);

        rotation = new Vector3D(0, -90, 0);
        position = new Vector3D(60, -20, -120);
        ObjObject rocas2 = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
        rocas2.setTexture(texture);
        object3DList.add(rocas2);

        // Rocky terrain
        pathObj = "ObjFiles/templeObjs/terreno_rocos.obj";
        rotation = new Vector3D(-90, 0, 0);
        position = new Vector3D(0, 20, -100);
        scale = new Vector3D(200, 200, 200);
        ObjObject rocas3 = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
        rocas3.setTexture(texture);
        object3DList.add(rocas3);

        return object3DList;
    }

    // Method to generate and return a list of lights for the scene
    public static List<Light> getLights() {
        List<Light> lights = new ArrayList<>();

        Vector3D lightPosition;
        Vector3D lightColor;

        // Light sources with varying color, position and radius
        lightPosition = new Vector3D(10, 10, -15);
        lightColor = new Vector3D(247, 190, 69);
        lights.add(new PointLight(lightPosition, lightColor, 40));

        lightPosition = new Vector3D(10, 50, -60);
        lightColor = new Vector3D(255, 255, 255);
        lights.add(new PointLight(lightPosition, lightColor, 60));
        lights.add(new PointLight(lightPosition, lightColor, 60)); // duplicate

        lightPosition = new Vector3D(20, 50, -175);
        lightColor = new Vector3D(72, 182, 244);
        lights.add(new PointLight(lightPosition, lightColor, 40));

        lightPosition = new Vector3D(60, 50, -180);
        lightColor = new Vector3D(255, 253, 200);
        lights.add(new PointLight(lightPosition, lightColor, 60));

        lightPosition = new Vector3D(-60, 50, -150);
        lights.add(new PointLight(lightPosition, lightColor, 60));

        lightPosition = new Vector3D(100, 40, -180);
        lights.add(new PointLight(lightPosition, lightColor, 60));

        lightPosition = new Vector3D(-50, 40, -180);
        lightColor = new Vector3D(237, 111, 79);
        lights.add(new PointLight(lightPosition, lightColor, 40));

        return lights;
    }
}
