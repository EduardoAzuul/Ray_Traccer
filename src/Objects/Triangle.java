package Objects;

import vectors.Vector3D;

public class Triangle extends Object3D {
    private Vector3D vertex1 = new Vector3D();
    private Vector3D vertex2 = new Vector3D();
    private Vector3D vertex3 = new Vector3D();

    public Vector3D getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vector3D vertex1) {
        this.vertex1 = vertex1.add(position); //Add the movemente of origin
    }

    public Vector3D getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vector3D vertex2) {
        this.vertex2 = vertex2.add(position);
    }

    public Vector3D getVertex3() {
        return vertex3;
    }

    public void setVertex3(Vector3D vertex3) {
        this.vertex3 = vertex3.add(position);
    }

    public Vector3D getOrigin() {
        return position;
    }

    public void setOrigin(Vector3D origin) {
        this.position = origin;
    }

    public Triangle(Vector3D color, Vector3D rotation, Vector3D origin,
                    Vector3D scale, Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
        super(color, rotation, origin, scale);
        setOrigin(origin);
        setVertex1(vertex1);
        setVertex2(vertex2);
        setVertex3(vertex3);

    }




}