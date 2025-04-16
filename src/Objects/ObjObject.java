package Objects;

import Obj_Reader.ObjReader;
import vectors.Vector3D;

import java.util.List;

public class ObjObject extends Object3D {
    private List<Triangle> triangleList;

    public ObjObject(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String ObjPath) {
        super(color, rotation, position, scale);
        // Read the triangle list from the OBJ file and apply transformations
        setTriangleList(ObjReader.triangleList(color, rotation, position, scale, ObjPath));

        if (getTriangleList() != null) {
            System.out.println("The list of triangles contains " + getTriangleList().size() + " triangles.");
        } else {
            System.out.println("Error: The triangle list is null. Failed to load OBJ file.");
        }
    }

    // Set the list of triangles
    public void setTriangleList(List<Triangle> triangleList) {
        this.triangleList = triangleList;
    }

    // Get the list of triangles
    public List<Triangle> getTriangleList() {
        return triangleList;
    }

}
