package ResuableElements;

import Materials.BlingPhongMaterial;
import Materials.Material;
import Objects.ObjObject;
import vectors.Vector3D;

public class TipicalObj {

    public static ObjObject createTeapot(Material material) {
        String path = "Objs/SmallTeapot.obj";
        Vector3D color = new Vector3D(255, 255, 255);
        Vector3D rotation = new Vector3D(0, 0, 0);
        Vector3D position = new Vector3D(0, 0, -30);
        Vector3D scale = new Vector3D(8.0, 8.0, 8.0);
        //Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String objPath
        ObjObject obj = new ObjObject(material, color, rotation, position, scale, path);
        return obj;
    }

    public static ObjObject createAngel(Material material) {
        String path = "Objs/Angle.obj";
        Vector3D color = new Vector3D(255, 255, 255);
        Vector3D rotation = new Vector3D(0, 0, 0);
        Vector3D position = new Vector3D(0, -20, -50);
        Vector3D scale = new Vector3D(8.0, 8.0, 8.0);

        ObjObject obj = new ObjObject(material, color, rotation, position, scale, path);
        return obj;
    }

    public static ObjObject createFloor(Material material) {
        String path = "Objs/square.obj";
        Vector3D color = new Vector3D(255, 255, 255);
        Vector3D rotation = new Vector3D(-90, 0, 0);
        Vector3D position = new Vector3D(0, -20, -50);
        Vector3D scale = new Vector3D(80.0, 80.0, 80.0);

        ObjObject obj = new ObjObject(material, color, rotation, position, scale, path);
        return obj;
    }
}
