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

public class MuseumAngels {

    public static List<Object3D> getObjects() {
        List<Object3D> object3DList = new ArrayList<>();
        String pathObj;
        Vector3D color, rotation, position, scale;
        Texture texture;

        // Alfombra roja
        pathObj = "ObjFiles/Objs/cube.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(-90, 0, 0);
        position = new Vector3D(0, -20.5, -50);
        scale = new Vector3D(10.0, 140.0, 1.0);
        ObjObject alfombra1 = new ObjObject(TipicalMaterials.CLOTH_WOOL_RED, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/tela_roja.png");
        alfombra1.setTexture(texture);
        object3DList.add(alfombra1);

        // Alfombras amarillas
        position = new Vector3D(11, -20.6, -50);
        scale = new Vector3D(2.0, 140.0, 1.0);
        ObjObject alfombra2=new ObjObject(TipicalMaterials.CLOTH_YELLOW, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/tela_blanca.jpg");
        alfombra2.setTexture(texture);
        object3DList.add(alfombra2);

        position = new Vector3D(-11, -20.6, -50);
        ObjObject alfombra3 = new ObjObject(TipicalMaterials.CLOTH_YELLOW, color, rotation, position, scale, pathObj);
        alfombra3.setTexture(texture);
        object3DList.add(alfombra3);

        // Piso
        pathObj = "ObjFiles/Objs/square.obj";
        color = new Vector3D(233, 31, 91);
        rotation = new Vector3D(-90, 0, 0);
        position = new Vector3D(0, -20, -75);
        scale = new Vector3D(90.0, 100.0, 80.0);
        ObjObject piso = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/piso_mosaicos.jpg");
        piso.setTexture(texture);
        object3DList.add(piso);

        // Techo
        rotation = new Vector3D(90, 0, 0);
        position = new Vector3D(0, 55, -60);
        texture = new Texture("Textures/ClayPlasteres.jpg");
        ObjObject techo = new ObjObject(TipicalMaterials.LEAF, color, rotation, position, scale, pathObj);
        techo.setTexture(texture);
        object3DList.add(techo);

        // Paredes
        rotation = new Vector3D(0, 90, 0);
        position = new Vector3D(-75, 0, -60);
        ObjObject pared1 = new ObjObject(TipicalMaterials.MARBLE_OLD_BEIGE, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/bricks_white.jpg");
        pared1.setTexture(texture);
        object3DList.add(pared1);

        // Paredes
        rotation = new Vector3D(0, 0, 0);
        position = new Vector3D(0, 0, -140);
        ObjObject pared3 = new ObjObject(TipicalMaterials.MARBLE_OLD_BEIGE, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/bricks_white.jpg");
        pared3.setTexture(texture);
        object3DList.add(pared3);

        rotation = new Vector3D(0, -90, 0);
        position = new Vector3D(75, 0, -60);
        ObjObject pared2 = new ObjObject(TipicalMaterials.MARBLE_OLD_BEIGE, color, rotation, position, scale, pathObj);
        pared2.setTexture(texture);
        object3DList.add(pared2);

        // Columnas
        pathObj = "ObjFiles/Objs/columna.obj";
        color = new Vector3D(255, 255, 255);
        rotation = new Vector3D(0, 90, 0);
        scale = new Vector3D(5, 7, 5);

        texture = new Texture("Textures/concreto.jpg");

        position = new Vector3D(-55, -30, -80);
        ObjObject columna1 = new ObjObject(TipicalMaterials.CONCRETE_REDDISH, color, rotation, position, scale, pathObj);
        columna1.setTexture(texture);
        object3DList.add(columna1);


        position = new Vector3D(55, -30, -80);
        ObjObject columna2 = new ObjObject(TipicalMaterials.CONCRETE_REDDISH, color, rotation, position, scale, pathObj);
        columna2.setTexture(texture);
        object3DList.add(columna2);


        // Ángel grid
        object3DList.addAll(TipicalObj.createAngelGrid());

        return object3DList;
    }

    public static List<Light> getLights() {
        List<Light> lights = new ArrayList<>();

        Vector3D lightPosition;
        Vector3D lightColor;

        lightPosition = new Vector3D(10, 30, -20);
        lightColor = new Vector3D(255, 255, 255);
        lights.add(new PointLight(lightPosition, lightColor, 60));

        lightPosition = new Vector3D(-10, 30, -20);
        lightColor = new Vector3D(60, 110, 255);
        lights.add(new PointLight(lightPosition, lightColor, 30));

        lightPosition = new Vector3D(-10, 20, -100);
        lightColor = new Vector3D(255, 220, 60);
        lights.add(new PointLight(lightPosition, lightColor, 25));

        // Repetido en original, lo mantengo
        lights.add(new PointLight(lightPosition, lightColor, 25));

        return lights;
    }
}
