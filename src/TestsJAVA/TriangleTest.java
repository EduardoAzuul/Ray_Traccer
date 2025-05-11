package TestsJAVA;

import Objects.Triangle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import vectors.Vector3D;

public class TriangleTest {

    @Test
    public void testTriangleCenter() {
        Triangle triangle = new Triangle(
                new Vector3D(1, 1, 1),             // color
                new Vector3D(0, 0, 0),             // rotation
                new Vector3D(0, 0, 0),             // origin
                new Vector3D(1, 1, 1),             // scale
                new Vector3D(0, 0, 0),             // vertex1
                new Vector3D(3, 0, 0),             // vertex2
                new Vector3D(0, 3, 0)              // vertex3
        );

        Vector3D expectedCenter = new Vector3D(1.0, 1.0, 0.0);
        assertEquals(expectedCenter, triangle.getCenter());
    }

    @Test
    public void testTransformationWithRotationAndScale() {
        Triangle triangle = new Triangle(
                new Vector3D(0, 0, 0),             // color
                new Vector3D(0, 90, 0),            // rotation Y-axis
                new Vector3D(0, 0, 0),             // origin
                new Vector3D(2, 1, 1),             // scale
                new Vector3D(1, 0, 0),             // vertex1
                new Vector3D(1, 1, 0),             // vertex2
                new Vector3D(1, 0, 1)              // vertex3
        );

        Vector3D transformedV1 = triangle.getVertex1();
        // Expected: rotated around Y 90° and scaled X2 -> (1,0,0) -> (0,0,-2)
        assertEquals(new Vector3D(0, 0, -2), transformedV1);
    }
}
