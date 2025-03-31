package TestsJAVA;

import Objects.Sphere;
import org.junit.jupiter.api.Test;
import vectors.Vector3D;


//ADING UNITESTING WORK IN PROGRESS

class SphereTest {
    @Test
    void testSphere() {
        Vector3D position = new Vector3D(1, 2, 3);


    Vector3D color = new Vector3D(100, 200, 100);
    Vector3D rotation = new Vector3D(100, 100, 100);
    Vector3D scale = new Vector3D(1, 1, 1);
    int radius = 2;


    Sphere sphere = new Sphere(
            color,          //Color
            rotation,                //Rotation
            position,
            scale, //Position
            2
    );
    }
}