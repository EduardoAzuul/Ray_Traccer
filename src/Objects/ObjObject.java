package Objects;

import Tools.ObjReader;
import vectors.Vector3D;

import java.util.List;

public class ObjObject extends Object3D {
    private List<Triangle> triangleList;
    private Cube cube;

    public ObjObject(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String objPath) {
        super(color, rotation, position, scale);
        loadFromObjFile(objPath);
        setCube();
    }

    private void loadFromObjFile(String objPath) {
        try {
            // Read the triangle list from the OBJ file and apply transformations
            List<Triangle> loadedTriangles = ObjReader.triangleList(
                    getColor(), getRotation(), getPosition(), getScale(), objPath);

            setTriangleList(loadedTriangles);

            if (triangleList == null || triangleList.isEmpty()) {
                System.out.println("Warning: No triangles loaded from OBJ file: " + objPath);
            } else {
                System.out.println("Successfully loaded " + triangleList.size() +
                        " triangles from OBJ file: " + objPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading OBJ file: " + objPath);
            e.printStackTrace();
            triangleList = null;
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

    // Returns true if the model was loaded successfully
    public boolean isLoaded() {
        return triangleList != null && !triangleList.isEmpty();
    }

    public Cube getCube() {
        return cube;
    }

    public void setCube(Vector3D min, Vector3D max) {
        this.cube = new Cube(min,max);
    }

    private void setCube() {
        double minx = Double.MAX_VALUE;
        double maxx = Double.MIN_VALUE;
        double miny = Double.MAX_VALUE;
        double maxy = Double.MIN_VALUE;
        double minz = Double.MAX_VALUE;
        double maxz = Double.MIN_VALUE;

        for (Triangle triangle : getTriangleList()) {
            Vector3D[] vertices = { triangle.getVertex1(), triangle.getVertex2(), triangle.getVertex3() };

            for (Vector3D v : vertices) {
                if (v.getX() < minx) minx = v.getX();
                if (v.getX() > maxx) maxx = v.getX();
                if (v.getY() < miny) miny = v.getY();
                if (v.getY() > maxy) maxy = v.getY();
                if (v.getZ() < minz) minz = v.getZ();
                if (v.getZ() > maxz) maxz = v.getZ();
            }
        }

        Vector3D min = new Vector3D(minx, miny, minz);
        Vector3D max = new Vector3D(maxx, maxy, maxz);

        setCube(min, max);
    }
}