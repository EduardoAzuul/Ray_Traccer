package Objects;

import Materials.Material;
import Materials.Texture;
import Tools.ObjReader;
import vectors.Vector3D;
import Objects.WrapperBoxes.*;

import java.util.List;

/**
 * Represents a 3D object loaded from an OBJ file.
 * This object consists of a list of triangles and includes
 * an AABB (Axis-Aligned Bounding Box) and BVH (Bounding Volume Hierarchy)
 * for optimized spatial queries.
 *
 * @author José Eduardo Moreno Paredes
 */
public class ObjObject extends Object3D {
    private List<Triangle> triangleList;
    private Cube cube;
    private BVHNode bvhRoot;

    /**
     * Constructs an ObjObject using basic geometric transformations.
     *
     * @param color    The base color of the object.
     * @param rotation The rotation vector of the object.
     * @param position The position vector of the object.
     * @param scale    The scale vector of the object.
     * @param objPath  The path to the OBJ file to load.
     */
    public ObjObject(Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String objPath) {
        super(color, rotation, position, scale);
        loadFromObjFile(objPath);
        rotateNormals();
        setCube();
        buildBVH();
    }

    /**
     * Constructs an ObjObject using a specific material and transformations.
     *
     * @param material Material to apply to the object.
     * @param color    The base color of the object.
     * @param rotation The rotation vector of the object.
     * @param position The position vector of the object.
     * @param scale    The scale vector of the object.
     * @param objPath  The path to the OBJ file to load.
     */
    public ObjObject(Material material, Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String objPath) {
        super(color, rotation, position, scale);
        setMaterial(material);
        loadFromObjFile(objPath);
        rotateNormals();
        setCube();
        buildBVH();
    }

    /**
     * Applies a texture to all triangles of the object.
     *
     * @param texture The texture to set.
     */
    public void setTexture(Texture texture) {
        for (Triangle triangle : triangleList) {
            triangle.setTexture(texture);
        }
    }

    /**
     * Rotates the normals of each triangle according to the object's rotation.
     */
    private void rotateNormals() {
        for (Triangle triangle : triangleList) {
            Vector3D normal1 = triangle.getNormalEdge1();
            Vector3D normal2 = triangle.getNormalEdge2();
            Vector3D normal3 = triangle.getNormalEdge3();

            triangle.setNormalEdge1(normal1.rotateVector(getRotation()));
            triangle.setNormalEdge2(normal2.rotateVector(getRotation()));
            triangle.setNormalEdge3(normal3.rotateVector(getRotation()));
            triangle.setMaterial(getMaterial());
        }
    }

    /**
     * Loads triangle data from an OBJ file.
     *
     * @param objPath Path to the OBJ file.
     */
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

    /**
     * Sets the list of triangles for this object.
     *
     * @param triangleList A list of triangles.
     */
    public void setTriangleList(List<Triangle> triangleList) {
        this.triangleList = triangleList;
    }

    /**
     * Returns the list of triangles that compose this object.
     *
     * @return List of triangles.
     */
    public List<Triangle> getTriangleList() {
        return triangleList;
    }

    /**
     * Checks if the object has successfully loaded triangles.
     *
     * @return true if loaded, false otherwise.
     */
    public boolean isLoaded() {
        return triangleList != null && !triangleList.isEmpty();
    }

    /**
     * Returns the object's bounding box.
     *
     * @return A Cube representing the bounding box.
     */
    public Cube getCube() {
        return cube;
    }

    /**
     * Sets the bounding box manually.
     *
     * @param min Minimum corner of the cube.
     * @param max Maximum corner of the cube.
     */
    public void setCube(Vector3D min, Vector3D max) {
        this.cube = new Cube(min, max);
    }

    /**
     * Computes and sets the bounding box based on the triangle vertices.
     */
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

    /**
     * Builds the Bounding Volume Hierarchy (BVH) from the object's triangles.
     */
    public void buildBVH() {
        if (!isLoaded()) return;

        BVHGenerator bvhGenerator = new BVHGenerator(triangleList);
        this.bvhRoot = bvhGenerator.buildBVH();
    }

    /**
     * Returns the root node of the BVH.
     *
     * @return BVH root node.
     */
    public BVHNode getBvhRoot() {
        return bvhRoot;
    }

    /**
     * Checks if a point is contained within the object's bounding box.
     *
     * @param point The point to check.
     * @return true if the point is inside the bounding box, false otherwise.
     */
    public boolean containsPoint(Vector3D point) {
        return cube != null && cube.contains(point);
    }

    /**
     * Checks whether this object intersects with another object.
     *
     * @param other The other ObjObject to test.
     * @return true if the bounding boxes intersect, false otherwise.
     */
    public boolean intersects(ObjObject other) {
        return this.cube != null && other.cube != null && this.cube.intersects(other.cube);
    }

    /**
     * Returns the center point of the object's bounding box.
     *
     * @return Center of the cube.
     */
    public Vector3D getCenter() {
        return cube != null ? cube.getCenter() : new Vector3D(0, 0, 0);
    }

    /**
     * Returns the dimensions (width, height, depth) of the bounding box.
     *
     * @return Vector representing the cube's dimensions.
     */
    public Vector3D getDimensions() {
        return cube != null ? cube.getDimensions() : new Vector3D(0, 0, 0);
    }

    /**
     * Updates the Axis-Aligned Bounding Box and rebuilds the BVH.
     */
    public void updateAABB() {
        setCube();
        buildBVH();
    }
}
