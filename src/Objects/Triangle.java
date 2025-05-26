package Objects;

import Materials.Material;
import Materials.Texture;
import vectors.Vector3D;

/**
 * Represents a 3D triangle object that extends the base Object3D class.
 * Supports vertex transformations, normal calculations, texture mapping,
 * and barycentric coordinate interpolation.
 *
 * @author José Eduardo Moreno
 */
public class Triangle extends Object3D {
    /** First vertex of the triangle */
    private Vector3D vertex1;
    /** Second vertex of the triangle */
    private Vector3D vertex2;
    /** Third vertex of the triangle */
    private Vector3D vertex3;
    /** Face normal of the triangle */
    private Vector3D normal;
    /** Normal at first vertex (for smooth shading) */
    private Vector3D normalEdge1;
    /** Normal at second vertex (for smooth shading) */
    private Vector3D normalEdge2;
    /** Normal at third vertex (for smooth shading) */
    private Vector3D normalEdge3;
    /** Material properties of the triangle */
    private Material material;
    /** Texture applied to the triangle */
    private Texture texture;
    /** Texture coordinates for first vertex */
    private Vector3D texture1;
    /** Texture coordinates for second vertex */
    private Vector3D texture2;
    /** Texture coordinates for third vertex */
    private Vector3D texture3;

    /**
     * Gets the texture applied to this triangle.
     * @return The texture object
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Sets the texture for this triangle.
     * @param texture The texture to apply
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Gets the texture coordinates for vertex 1.
     * @return UV coordinates as Vector3D (z component unused)
     */
    public Vector3D getTexture1() {
        return texture1;
    }

    /**
     * Sets the texture coordinates for vertex 1.
     * @param texture1 UV coordinates as Vector3D (z component unused)
     */
    public void setTexture1(Vector3D texture1) {
        this.texture1 = texture1;
    }

    /**
     * Gets the texture coordinates for vertex 2.
     * @return UV coordinates as Vector3D (z component unused)
     */
    public Vector3D getTexture2() {
        return texture2;
    }

    /**
     * Sets the texture coordinates for vertex 2.
     * @param texture2 UV coordinates as Vector3D (z component unused)
     */
    public void setTexture2(Vector3D texture2) {
        this.texture2 = texture2;
    }

    /**
     * Gets the texture coordinates for vertex 3.
     * @return UV coordinates as Vector3D (z component unused)
     */
    public Vector3D getTexture3() {
        return texture3;
    }

    /**
     * Sets the texture coordinates for vertex 3.
     * @param texture3 UV coordinates as Vector3D (z component unused)
     */
    public void setTexture3(Vector3D texture3) {
        this.texture3 = texture3;
    }

    /**
     * Gets the material of the triangle.
     * @return The material properties
     */
    @Override
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the triangle.
     * @param material The material properties to apply
     */
    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Sets all texture coordinates at once.
     * @param texture1 UV coordinates for vertex 1
     * @param texture2 UV coordinates for vertex 2
     * @param texture3 UV coordinates for vertex 3
     */
    public void setTextures(Vector3D texture1, Vector3D texture2, Vector3D texture3) {
        this.texture1 = texture1;
        this.texture2 = texture2;
        this.texture3 = texture3;
    }

    /**
     * Constructs a triangle with basic vertex information.
     * Applies transformations in order: scale → rotate → translate.
     *
     * @param color The RGB color of the triangle
     * @param rotation The rotation angles in degrees
     * @param origin The origin position
     * @param scale The scaling factors
     * @param vertex1 First vertex in model space
     * @param vertex2 Second vertex in model space
     * @param vertex3 Third vertex in model space
     */
    public Triangle(Vector3D color, Vector3D rotation, Vector3D origin,
                    Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
        super(color, rotation, origin, scale);
        this.vertex1 = transformVertex(vertex1);
        this.vertex2 = transformVertex(vertex2);
        this.vertex3 = transformVertex(vertex3);
        setNormal();
    }

    /**
     * Constructs a triangle with vertex and normal information.
     * Applies transformations in order: scale → rotate → translate.
     *
     * @param color The RGB color of the triangle
     * @param rotation The rotation angles in degrees
     * @param origin The origin position
     * @param scale The scaling factors
     * @param vertex1 First vertex in model space
     * @param vertex2 Second vertex in model space
     * @param vertex3 Third vertex in model space
     * @param normal1 Normal at first vertex
     * @param normal2 Normal at second vertex
     * @param normal3 Normal at third vertex
     */
    public Triangle(Vector3D color, Vector3D rotation, Vector3D origin,
                    Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3,
                    Vector3D normal1, Vector3D normal2, Vector3D normal3) {
        super(color, rotation, origin, scale);
        this.vertex1 = transformVertex(vertex1);
        this.vertex2 = transformVertex(vertex2);
        this.vertex3 = transformVertex(vertex3);
        setNormalEdge1(normal1);
        setNormalEdge2(normal2);
        setNormalEdge3(normal3);
    }

    /**
     * Transforms a vertex from model space to world space.
     * Applies transformations in order: scale → rotate → translate.
     *
     * @param vertex The vertex in model space
     * @return The transformed vertex in world space
     */
    private Vector3D transformVertex(Vector3D vertex) {
        // 1. Scale first
        Vector3D scaled = new Vector3D(
                vertex.getX() * scale.getX(),
                vertex.getY() * scale.getY(),
                vertex.getZ() * scale.getZ()
        );

        // 2. Then rotate
        Vector3D rotated = rotateVector(scaled, rotation);

        // 3. Finally translate
        return rotated.add(position);
    }

    /**
     * Rotates a vector by specified Euler angles.
     *
     * @param v The vector to rotate
     * @param rotation Rotation angles in degrees (X, Y, Z)
     * @return The rotated vector
     */
    private Vector3D rotateVector(Vector3D v, Vector3D rotation) {
        double radX = Math.toRadians(rotation.getX());
        double radY = Math.toRadians(rotation.getY());
        double radZ = Math.toRadians(rotation.getZ());

        // Rotation around X axis
        double cosX = Math.cos(radX);
        double sinX = Math.sin(radX);
        double y1 = v.getY() * cosX - v.getZ() * sinX;
        double z1 = v.getY() * sinX + v.getZ() * cosX;

        // Rotation around Y axis
        double cosY = Math.cos(radY);
        double sinY = Math.sin(radY);
        double x2 = v.getX() * cosY + z1 * sinY;
        double z2 = -v.getX() * sinY + z1 * cosY;

        // Rotation around Z axis
        double cosZ = Math.cos(radZ);
        double sinZ = Math.sin(radZ);
        double x3 = x2 * cosZ - y1 * sinZ;
        double y3 = x2 * sinZ + y1 * cosZ;

        return new Vector3D(x3, y3, z2);
    }

    /**
     * Gets the first vertex of the triangle.
     * @return Vertex 1 in world space
     */
    public Vector3D getVertex1() {
        return vertex1;
    }

    /**
     * Sets the first vertex of the triangle.
     * @param vertex1 New vertex position
     */
    public void setVertex1(Vector3D vertex1) {
        this.vertex1 = vertex1;
    }

    /**
     * Gets the second vertex of the triangle.
     * @return Vertex 2 in world space
     */
    public Vector3D getVertex2() {
        return vertex2;
    }

    /**
     * Sets the second vertex of the triangle.
     * @param vertex2 New vertex position
     */
    public void setVertex2(Vector3D vertex2) {
        this.vertex2 = vertex2;
    }

    /**
     * Gets the third vertex of the triangle.
     * @return Vertex 3 in world space
     */
    public Vector3D getVertex3() {
        return vertex3;
    }

    /**
     * Sets the third vertex of the triangle.
     * @param vertex3 New vertex position
     */
    public void setVertex3(Vector3D vertex3) {
        this.vertex3 = vertex3;
    }

    /**
     * Calculates and sets the face normal of the triangle.
     */
    private void setNormal() {
        Vector3D V = vertex2.subtract(vertex1);
        Vector3D W = vertex1.subtract(vertex3);
        this.normal = (V.cross(W).normalize());
    }

    /**
     * Gets the normal at vertex 1 (for smooth shading).
     * @return The vertex normal
     */
    public Vector3D getNormalEdge1() {
        return normalEdge1;
    }

    /**
     * Sets the normal at vertex 1 (for smooth shading).
     * @param normalEdge1 The vertex normal
     */
    public void setNormalEdge1(Vector3D normalEdge1) {
        this.normalEdge1 = normalEdge1;
    }

    /**
     * Gets the normal at vertex 2 (for smooth shading).
     * @return The vertex normal
     */
    public Vector3D getNormalEdge2() {
        return normalEdge2;
    }

    /**
     * Sets the normal at vertex 2 (for smooth shading).
     * @param normalEdge2 The vertex normal
     */
    public void setNormalEdge2(Vector3D normalEdge2) {
        this.normalEdge2 = normalEdge2;
    }

    /**
     * Gets the normal at vertex 3 (for smooth shading).
     * @return The vertex normal
     */
    public Vector3D getNormalEdge3() {
        return normalEdge3;
    }

    /**
     * Sets the normal at vertex 3 (for smooth shading).
     * @param normalEdge3 The vertex normal
     */
    public void setNormalEdge3(Vector3D normalEdge3) {
        this.normalEdge3 = normalEdge3;
    }

    /**
     * Calculates the interpolated normal at a point on the triangle.
     * Uses barycentric coordinates for smooth shading.
     *
     * @param point The point on the triangle surface
     * @return The interpolated normal vector
     */
    public Vector3D getNormal(Vector3D point) {
        Vector3D v0 = vertex2.subtract(vertex1);
        Vector3D v1 = vertex3.subtract(vertex1);
        Vector3D v2 = point.subtract(vertex1);

        double d00 = v0.dot(v0);
        double d01 = v0.dot(v1);
        double d11 = v1.dot(v1);
        double d20 = v2.dot(v0);
        double d21 = v2.dot(v1);

        double denom = d00 * d11 - d01 * d01;
        double v = (d11 * d20 - d01 * d21) / denom;
        double w = (d00 * d21 - d01 * d20) / denom;
        double u = 1.0 - v - w;

        Vector3D interpolatedNormal = normalEdge1.multiplyByScalar(u)
                .add(normalEdge2.multiplyByScalar(v))
                .add(normalEdge3.multiplyByScalar(w));

        return interpolatedNormal.normalize().multiplyByScalar(-1);
    }

    /**
     * Prints triangle information to standard output.
     * Useful for debugging purposes.
     */
    public void print() {
        System.out.println("TRIANGLE***********************************");
        System.out.println("PointA: " + vertex1);
        System.out.println("PointB: " + vertex2);
        System.out.println("PointC: " + vertex3);
        System.out.println("Color: " + color);
    }

    /**
     * Calculates the geometric center of the triangle.
     * @return The centroid as a Vector3D
     */
    public Vector3D getCenter() {
        return new Vector3D(
                (vertex1.getX()+vertex2.getX()+vertex3.getX())/3,
                (vertex1.getY()+vertex2.getY()+vertex3.getY())/3,
                (vertex1.getZ()+vertex2.getZ()+vertex3.getZ())/3
        );
    }

    /**
     * Gets the texture color at a specific point on the triangle.
     * Uses barycentric coordinates for texture interpolation.
     *
     * @param point The point on the triangle surface
     * @return The interpolated texture color as packed RGB integer
     */
    public int getTextureColor(Vector3D point) {
        if (texture == null || texture1 == null || texture2 == null || texture3 == null) {
            return 0; // Default black color
        }

        Vector3D v0 = vertex2.subtract(vertex1);
        Vector3D v1 = vertex3.subtract(vertex1);
        Vector3D v2 = point.subtract(vertex1);

        double d00 = v0.dot(v0);
        double d01 = v0.dot(v1);
        double d11 = v1.dot(v1);
        double d20 = v2.dot(v0);
        double d21 = v2.dot(v1);

        double denom = d00 * d11 - d01 * d01;
        if (Math.abs(denom) < 1e-6) {
            return 0;
        }

        double v = (d11 * d20 - d01 * d21) / denom;
        double w = (d00 * d21 - d01 * d20) / denom;
        double u = 1.0 - v - w;

        double interpU = texture1.getX() * u + texture2.getX() * v + texture3.getX() * w;
        double interpV = texture1.getY() * u + texture2.getY() * v + texture3.getY() * w;

        return texture.getColorAt(interpU, interpV);
    }
}