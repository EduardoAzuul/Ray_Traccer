package Objects;

import vectors.Vector3D;

public class Triangle extends Object3D {
    private Vector3D vertex1;
    private Vector3D vertex2;
    private Vector3D vertex3;
    private Vector3D normal;

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

    public Vector3D getNormal() {
        return normal;
    }

    public void print() {
        System.out.println("TRIANGLE***********************************");
        System.out.println("PointA: " + vertex1);
        System.out.println("PointB: " + vertex2);
        System.out.println("PointC: " + vertex3);
        System.out.println("Color: " + color);
    }
}
