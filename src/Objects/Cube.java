package Objects;

import vectors.Vector3D;

//This class is used as a wraper in ObjObject to only check the cube insted of the whole faces
//Axis-Aligned Bounding Box - AABB :)
public class Cube {
    private Vector3D min;
    private Vector3D max;

    public Vector3D getMin() {
        return min;
    }

    public void setMin(Vector3D min) {
        this.min = min;
    }

    public Vector3D getMax() {
        return max;
    }

    public void setMax(Vector3D max) {
        this.max = max;
    }

    public Cube(Vector3D min, Vector3D max) {
        setMin(min);
        setMax(max);
    }


}
