package Objects;

import Materials.Material;
import Tools.ObjReader;
import vectors.Vector3D;
import Objects.WrapperBoxes.*;

import java.util.ArrayList;
import java.util.List;

public class ObjObject extends Object3D {
    private List<Triangle> triangleList;
    private Cube cube;
    private BVHNode bvhRoot;


    public ObjObject(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String objPath) {
        super(color, rotation, position, scale);
        loadFromObjFile(objPath);
        rotateNormals();
        setCube();
        buildBVH();
    }

    public ObjObject(Material material, Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String objPath) {
        super(color, rotation, position, scale);
        setMaterial(material);
        loadFromObjFile(objPath);
        rotateNormals();
        setCube();
        buildBVH();
    }

    private void rotateNormals(){
        for (Triangle triangle : triangleList) {
            Vector3D normal1= triangle.getNormalEdge1();
            Vector3D normal2= triangle.getNormalEdge2();
            Vector3D normal3= triangle.getNormalEdge3();

            triangle.setNormalEdge1(normal1.rotateVector(getRotation()));
            triangle.setNormalEdge2(normal2.rotateVector(getRotation()));
            triangle.setNormalEdge3(normal3.rotateVector(getRotation()));
            triangle.setMaterial(getMaterial());
        }
    }

    private void loadFromObjFile(String objPath) {
        try {
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

    public void setTriangleList(List<Triangle> triangleList) {
        this.triangleList = triangleList;
    }

    public List<Triangle> getTriangleList() {
        return triangleList;
    }

    public boolean isLoaded() {
        return triangleList != null && !triangleList.isEmpty();
    }

    public Cube getCube() {
        return cube;
    }

    public void setCube(Vector3D min, Vector3D max) {
        this.cube = new Cube(min, max);
    }

    private void setCube() {
        if (!isLoaded()) {
            this.cube = new Cube(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0));
            return;
        }

        Vector3D min = new Vector3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Vector3D max = new Vector3D(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);

        for (Triangle triangle : getTriangleList()) {
            Vector3D[] vertices = { triangle.getVertex1(), triangle.getVertex2(), triangle.getVertex3() };

            for (Vector3D v : vertices) {
                min = new Vector3D(
                        Math.min(min.getX(), v.getX()),
                        Math.min(min.getY(), v.getY()),
                        Math.min(min.getZ(), v.getZ())
                );
                max = new Vector3D(
                        Math.max(max.getX(), v.getX()),
                        Math.max(max.getY(), v.getY()),
                        Math.max(max.getZ(), v.getZ())
                );
            }
        }

        setCube(min, max);
    }

    public void buildBVH() {
        if (!isLoaded()) return;

        // Usar la nueva clase BVHGenerator
        BVHGenerator bvhGenerator = new BVHGenerator(triangleList);
        this.bvhRoot = bvhGenerator.buildBVH();
    }

    public BVHNode getBvhRoot() {
        return bvhRoot;
    }

    public boolean containsPoint(Vector3D point) {
        return cube != null && cube.contains(point);
    }

    public boolean intersects(ObjObject other) {
        return this.cube != null && other.cube != null && this.cube.intersects(other.cube);
    }

    public Vector3D getCenter() {
        return cube != null ? cube.getCenter() : new Vector3D(0, 0, 0);
    }

    public Vector3D getDimensions() {
        return cube != null ? cube.getDimensions() : new Vector3D(0, 0, 0);
    }

    public void updateAABB() {
        setCube();
        buildBVH();
    }
}