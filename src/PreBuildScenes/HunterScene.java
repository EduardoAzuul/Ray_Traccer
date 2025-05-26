package PreBuildScenes;

import Materials.Texture;
import Objects.ObjObject;
import Objects.Object3D;
import ResuableElements.TipicalMaterials;
import vectors.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class HunterScene {
    public static List<Object3D> getObjects() {
        List<Object3D> object3DList = new ArrayList<>();
        String pathObj;
        Vector3D color, rotation, position, scale;
        Texture texture;

        // Alfombra roja
        pathObj = "ObjFiles/Objs/hunter.obj";
        color = new Vector3D(20.0, 100.0, 185.0);
        rotation = new Vector3D(-90, 45, 0);
        position = new Vector3D(0, -20, -50);
        scale = new Vector3D(1,1,1);
        ObjObject alfombra1 = new ObjObject(TipicalMaterials.CONCRETE_WHITE, color, rotation, position, scale, pathObj);
        texture = new Texture("Textures/concreto.jpg");
        alfombra1.setTexture(texture);
        object3DList.add(alfombra1);

        return object3DList;
    }

}
