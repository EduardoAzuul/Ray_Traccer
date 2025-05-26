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

public class Office {
    public static List<Object3D> getObjects() {
        List<Object3D> object3DList = new ArrayList<>();
        String pathObj;
        Vector3D color, rotation, position, scale;
        Texture texture = new Texture("Textures/dark_wood.jpg");

        pathObj = "ObjFiles/OfficeObjs/desk.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(0, -25, -55);
        scale = new Vector3D(3.0, 3.0, 3.0);
        ObjObject desk = new ObjObject(TipicalMaterials.DARK_WOOD, color, rotation, position, scale, pathObj);
        desk.setTexture(texture);
        object3DList.add(desk);

        pathObj = "ObjFiles/OfficeObjs/objPot.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, 30, 0);
        position = new Vector3D(-35, -25, -40);
        scale = new Vector3D(4.0, 4.0, 4.0);
        object3DList.add(new ObjObject(TipicalMaterials.LIGHT_LEAF, color, rotation, position, scale, pathObj));


        pathObj = "ObjFiles/Objs/cube.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, -30, 180);
        scale = new Vector3D(10,12,10);
        ObjObject maceta = new ObjObject(TipicalMaterials.MARBLE, color, rotation, position, scale, pathObj);

        object3DList.add(maceta);

        pathObj = "ObjFiles/OfficeObjs/Jabali.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, 160, 0);
        position = new Vector3D(-15, -3, -50);
        scale = new Vector3D(0.6, 0.6, 0.6);
        object3DList.add(new ObjObject(TipicalMaterials.GLASS_CLEAR, color, rotation, position, scale, pathObj));


        texture = new Texture("Textures/splash_Art.jpg");
        pathObj = "ObjFiles/Objs/cube.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, 30, 0);
        position = new Vector3D(-10, 20, -88);
        scale = new Vector3D(30,15,5);
        ObjObject pintura = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
        pintura.setTexture(texture);
        object3DList.add(pintura);

        texture = new Texture("Textures/Mapa.jpg");
        pathObj = "ObjFiles/Objs/cube.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, -30, 180);
        position = new Vector3D(45, 20, -60);
        scale = new Vector3D(2,15,20);
        ObjObject pintura2 = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
        pintura2.setTexture(texture);
        object3DList.add(pintura2);

        pathObj = "ObjFiles/OfficeObjs/desk_lamp.obj";
        texture = new Texture("Textures/Gold.jpg");
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, -120, 0);
        position = new Vector3D(20, -4, -55);
        scale = new Vector3D(80,80,80);
        ObjObject Lamp = new ObjObject(TipicalMaterials.GOLD, color, rotation, position, scale, pathObj);
        Lamp.setTexture(texture);
        object3DList.add(Lamp);

        pathObj = "ObjFiles/OfficeObjs/Tasa.obj";
        texture = new Texture("Textures/Tiburon.jpg");
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(16, -3, -52);
        scale = new Vector3D(1.2 ,1.2,1.2);
        ObjObject tasa = new ObjObject(TipicalMaterials.MARBLE_YELLOW, color, rotation, position, scale, pathObj);
        tasa.setTexture(texture);
        object3DList.add(tasa);

        pathObj = "ObjFiles/Objs/sphere.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(11.2, -0.5, -45);
        scale = new Vector3D(2,2,2);
        ObjObject esfera = new ObjObject(TipicalMaterials.GLASS_CLEAR, color, rotation, position, scale, pathObj);
        object3DList.add(esfera);


        pathObj = "ObjFiles/OfficeObjs/Chair_low.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(0, 100, 0);
        position = new Vector3D(15, -25, -25);
        scale = new Vector3D(30.0, 30.0, 30.0);
        object3DList.add(new ObjObject(TipicalMaterials.LEATHER_BLACK, color, rotation, position, scale, pathObj));


        // Piso
        pathObj = "ObjFiles/Objs/square.obj";
        texture = new Texture("Textures/piso_cartago.jpg");
        color = new Vector3D(233, 31, 91);
        rotation = new Vector3D(-90, 0, 0);
        position = new Vector3D(0, -25, -45);
        scale = new Vector3D(80.0, 100.0, 80.0);
        ObjObject piso = new ObjObject(TipicalMaterials.MARBLE_WORN_BEIGE, color, rotation, position, scale, pathObj);
        piso.setTexture(texture);
        object3DList.add(piso);

        // Techo
        rotation = new Vector3D(90, 0, 0);
        position = new Vector3D(0, 55, -45);
        object3DList.add(new ObjObject(TipicalMaterials.PAINT_WHITE_MATTE, color, rotation, position, scale, pathObj));

        // Paredes
        rotation = new Vector3D(0, 60, 0);
        position = new Vector3D(-75, 0, -45);
        object3DList.add(new ObjObject(TipicalMaterials.PAINT_LIGHT_BLUE, color, rotation, position, scale, pathObj));

        rotation = new Vector3D(0, -60, 0);
        position = new Vector3D(75, 0, -45);
        object3DList.add(new ObjObject(TipicalMaterials.PAINT_LIGHT_BLUE, color, rotation, position, scale, pathObj));

        texture = new Texture("Textures/ClayPlasteres.jpg");
        rotation = new Vector3D(0, 30, 0);
        position = new Vector3D(0, 0, -90);
        ObjObject pared1 = new ObjObject(TipicalMaterials.PAINT_BLUE_MATTE, color, rotation, position, scale, pathObj);
        pared1.setTexture(texture);
        object3DList.add(pared1);
        return object3DList;
    }

    public static List<Light> getLights() {
        List<Light> lights = new ArrayList<>();

        Vector3D lightPosition;
        Vector3D lightColor;

        lightPosition = new Vector3D(10, 30, -20);
        lightColor = new Vector3D(255, 255, 255);
        lights.add(new PointLight(lightPosition, lightColor, 30));

        lightPosition = new Vector3D(-10, 30, -30);
        lightColor = new Vector3D(140, 255, 140);
        lights.add(new PointLight(lightPosition, lightColor, 10));

        lightPosition = new Vector3D(-10, 30, -40);
        lightColor = new Vector3D(235,81,141);
        lights.add(new PointLight(lightPosition, lightColor, 20));

        lightPosition = new Vector3D(0,40,-60);
        lightColor = new Vector3D(236,178,80);
        lights.add(new PointLight(lightPosition, lightColor, 20));

        lightPosition = new Vector3D(-20,-10,-15);
        lightColor = new Vector3D(236,178,80);
        lights.add(new PointLight(lightPosition, lightColor, 2));

        lightPosition = new Vector3D(20, 10, -55);
        lightColor = new Vector3D(236,178,80);
        Vector3D lightDirection = new Vector3D(-100,-100, 0);
        //Vector3D position, Vector3D color, double intensity, double radius, double angle, Vector3D direction
        lights.add(new SpotLight(lightPosition, lightColor, 40,0.5,20,lightDirection));





        return lights;
    }
}
