package Objects;

import vectors.Vector3D;

public class Triangle extends Object3D {
    private Vector3D vertex1;
    private Vector3D vertex2;
    private Vector3D vertex3;
    private Vector3D normal;
    private Vector3D normalEdge1;
    private Vector3D normalEdge2;
    private Vector3D normalEdge3;

    // Constructor with proper transformation order
    public Triangle(Vector3D color, Vector3D rotation, Vector3D origin,
                    Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
        super(color, rotation, origin, scale);

        // Apply transformations in correct order: scale -> rotate -> translate
        this.vertex1 = transformVertex(vertex1);
        this.vertex2 = transformVertex(vertex2);
        this.vertex3 = transformVertex(vertex3);
        setNormal();
        //print();
    }

    public Triangle(Vector3D color, Vector3D rotation, Vector3D origin,
                    Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3, Vector3D normal1, Vector3D normal2, Vector3D normal3) {
        super(color, rotation, origin, scale);

        // Apply transformations in correct order: scale -> rotate -> translate
        this.vertex1 = transformVertex(vertex1);
        this.vertex2 = transformVertex(vertex2);
        this.vertex3 = transformVertex(vertex3);
        setNormalEdge1(normal1);
        setNormalEdge2(normal2);
        setNormalEdge3(normal3);

        //print();
    }

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
        Vector3D translated = rotated.add(position);


        return translated;
    }

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


    public Vector3D getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vector3D vertex1) {
        this.vertex1 = vertex1;
    }

    public Vector3D getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vector3D vertex2) {
        this.vertex2 = vertex2;
    }

    public Vector3D getVertex3() {
        return vertex3;
    }

    public void setVertex3(Vector3D vertex3) {
        this.vertex3 = vertex3;
    }

    private void setNormal() {
        Vector3D V = vertex2.subtract(vertex1);
        Vector3D W = vertex1.subtract(vertex3);

        this.normal = (V.cross(W).normalize());
    }

    public Vector3D getNormalEdge1() {
        return normalEdge1;
    }

    public void setNormalEdge1(Vector3D normalEdge1) {
        this.normalEdge1 = normalEdge1;
    }

    public Vector3D getNormalEdge2() {
        return normalEdge2;
    }

    public void setNormalEdge2(Vector3D normalEdge2) {
        this.normalEdge2 = normalEdge2;
    }

    public Vector3D getNormalEdge3() {
        return normalEdge3;
    }

    public void setNormalEdge3(Vector3D normalEdge3) {
        this.normalEdge3 = normalEdge3;
    }


    public Vector3D getNormal(Vector3D point) {
        // Vectores desde los vértices
        Vector3D v0 = vertex2.subtract(vertex1);
        Vector3D v1 = vertex3.subtract(vertex1);
        Vector3D v2 = point.subtract(vertex1);

        // Productos escalares
        double d00 = v0.dot(v0);
        double d01 = v0.dot(v1);
        double d11 = v1.dot(v1);
        double d20 = v2.dot(v0);
        double d21 = v2.dot(v1);

        // Determinante
        double denom = d00 * d11 - d01 * d01;

        // Coordenadas baricéntricas
        double v = (d11 * d20 - d01 * d21) / denom;
        double w = (d00 * d21 - d01 * d20) / denom;
        double u = 1.0 - v - w;

        // Interpolación de la normal
        Vector3D interpolatedNormal = normalEdge1.multiplyByScalar(u)
                .add(normalEdge2.multiplyByScalar(v))
                .add(normalEdge3.multiplyByScalar(w));


        return interpolatedNormal.normalize().multiplyByScalar(-1);
    }

    public void print() {
        System.out.println("TRIANGLE***********************************");
        System.out.println("PointA: " + vertex1);
        System.out.println("PointB: " + vertex2);
        System.out.println("PointC: " + vertex3);
        System.out.println("Color: " + color);
    }

    public Vector3D getCenter() {
        return new Vector3D(
                (vertex1.getX()+vertex2.getX()+vertex3.getX())/3,
                (vertex1.getY()+vertex2.getY()+vertex3.getY())/3,
                (vertex1.getZ()+vertex2.getZ()+vertex3.getZ())/3
        );
    }
}
