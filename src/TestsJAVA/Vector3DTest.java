package TestsJAVA;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vectors.Vector3D;

public class Vector3DTest {

    @Test
    public void testConstructorAndGetters() {
        Vector3D v = new Vector3D(1, 2, 3);
        assertEquals(1, v.getX());
        assertEquals(2, v.getY());
        assertEquals(3, v.getZ());
    }

    @Test
    public void testDefaultConstructor() {
        Vector3D v = new Vector3D();
        assertEquals(0, v.getX());
        assertEquals(0, v.getY());
        assertEquals(0, v.getZ());
    }

    @Test
    public void testSetters() {
        Vector3D v = new Vector3D();
        v.setX(5);
        v.setY(-3);
        v.setZ(7);
        assertEquals(5, v.getX());
        assertEquals(-3, v.getY());
        assertEquals(7, v.getZ());
    }

    @Test
    public void testDotProduct() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, -5, 6);
        assertEquals(12, v1.dot(v2));
    }

    @Test
    public void testCrossProduct() {
        Vector3D v1 = new Vector3D(1, 0, 0);
        Vector3D v2 = new Vector3D(0, 1, 0);
        Vector3D cross = v1.cross(v2);
        assertEquals(0, cross.getX(), 1e-9);
        assertEquals(0, cross.getY(), 1e-9);
        assertEquals(1, cross.getZ(), 1e-9);
    }

    @Test
    public void testMagnitude() {
        Vector3D v = new Vector3D(3, 4, 0);
        assertEquals(5, v.magnitude(), 1e-9);
    }

    @Test
    public void testNormalize() {
        Vector3D v = new Vector3D(0, 3, 4);
        Vector3D normalized = v.normalize();
        assertEquals(0, normalized.getX(), 1e-9);
        assertEquals(0.6, normalized.getY(), 1e-9);
        assertEquals(0.8, normalized.getZ(), 1e-9);
    }

    @Test
    public void testNormalizeZeroVector() {
        Vector3D v = new Vector3D(0, 0, 0);
        Vector3D normalized = v.normalize();
        assertEquals(0, normalized.getX());
        assertEquals(0, normalized.getY());
        assertEquals(0, normalized.getZ());
    }

    @Test
    public void testAdd() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, -5, 6);
        Vector3D result = v1.add(v2);
        assertEquals(5, result.getX());
        assertEquals(-3, result.getY());
        assertEquals(9, result.getZ());
    }

    @Test
    public void testSubtract() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, -5, 6);
        Vector3D result = v1.subtract(v2);
        assertEquals(-3, result.getX());
        assertEquals(7, result.getY());
        assertEquals(-3, result.getZ());
    }

    @Test
    public void testMultiplyByScalar() {
        Vector3D v = new Vector3D(1, -2, 3);
        Vector3D result = v.multiplyByScalar(2);
        assertEquals(2, result.getX());
        assertEquals(-4, result.getY());
        assertEquals(6, result.getZ());
    }

    @Test
    public void testAmplitude() {
        Vector3D v = new Vector3D(1, 0, 0);
        assertEquals(0, v.amplitude(), 1e-9);

        Vector3D v2 = new Vector3D(0, 1, 0);
        assertEquals(Math.PI / 2, v2.amplitude(), 1e-9);
    }

    @Test
    public void testRotateVector() {
        Vector3D v = new Vector3D(1, 0, 0);
        Vector3D rotation = new Vector3D(0, 0, 90); // Rotate 90 degrees around Z-axis
        Vector3D rotated = v.rotateVector(rotation);

        assertEquals(0, rotated.getX(), 1e-6);
        assertEquals(1, rotated.getY(), 1e-6);
        assertEquals(0, rotated.getZ(), 1e-6);
    }

    @Test
    public void testRandomColor() {
        Vector3D color = Vector3D.randomColor();
        assertTrue(color.getX() >= 0 && color.getX() <= 255);
        assertTrue(color.getY() >= 0 && color.getY() <= 255);
        assertTrue(color.getZ() >= 0 && color.getZ() <= 255);
    }

    @Test
    public void testToString() {
        Vector3D v = new Vector3D(1, 2, 3);
        String expected = "Point: x=1.0, y=2.0, z=3.0";
        assertEquals(expected, v.toString());
    }
}
