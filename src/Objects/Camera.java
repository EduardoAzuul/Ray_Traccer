package Objects;

import Lights.DirectionLight;
import Lights.Light;
import Lights.LightIntersection;
import vectors.Intersection;
import vectors.Vector3D;
import vectors.Ray;

import java.awt.image.BufferedImage;
import java.util.List;

public class Camera {
    private Vector3D origin;
    private Vector3D rotation;
    private double nearplane;
    private double farplane;
    private BufferedImage image;
    private int width;
    private int height;
    private double fov;  // Field of view in degrees

    // Constructor
    public Camera(Vector3D origin, Vector3D rotation, double nearplane, double farplane,
                  int width, int height, double fov) {
        this.origin = origin;
        this.rotation = rotation;
        this.nearplane = nearplane;
        this.farplane = farplane;
        this.width = width;
        this.height = height;
        this.fov = fov;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    // Generate the view frustum rays (shot)
    public void shot(List<Object3D> objects, List<Light> lights) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {


                Ray ray = generateRay(x, y);
                int pixelColor = traceRay(ray, objects, lights);

                image.setRGB(x, y, pixelColor);

            }
        }
    }

    private int traceRay(Ray ray, List<Object3D> objects, List<Light> lights) {
        double closestDist = Double.MAX_VALUE;
        Object3D closestObject = null;
        Object3D triangleHit = null;
        Triangle triaAux = null;

        for (Object3D obj : objects) {
            double dist = -1.0;

            if (obj instanceof Sphere sphere) {
                dist = Intersection.sphere(ray, sphere, nearplane, farplane);
            }
            else if (obj instanceof Triangle triangle) {
                dist = Intersection.triangle(ray, triangle, nearplane, farplane, 1e-4);
                triaAux = triangle;


            }
            else if (obj instanceof ObjObject objObject) {
                triaAux = Intersection.objTriangleIntersected(ray, objObject, nearplane, farplane, 1e-4);

                if(triaAux != null) {
                    dist = Intersection.triangle(ray, triaAux, nearplane, farplane, 1e-4);
                }

            }

            // Simplified condition - no need to check dist > 0 if checking dist > nearplane
            if (dist > nearplane && dist < farplane && dist < closestDist) {
                closestDist = dist;
                closestObject = obj;
                triangleHit = triaAux;

            }
        }

        if(closestObject == null) {
            return 0x000000; // Return black if no intersection
        }


        // Calculate the intersection point
        Vector3D intersectionPoint = ray.getOrigin().add(ray.getDirection().scale(closestDist));

        // Create a LightIntersection object to handle lighting calculations
        LightIntersection lightIntersection = new LightIntersection(
                objects,         // All objects in the scene
                lights,          // All lights in the scene
                closestObject,   // The object that was hit
                ray.getOrigin(), // Origin of the ray
                ray.getDirection() // Direction of the ray
        );

        // Calculate the final color with lighting
        if (triangleHit != null) {
            //System.out.println("Triangle intersection: " + triangleHit);
            return lightIntersection.lightsIntersection(triangleHit, intersectionPoint);
        }

        return closestObject.getColorInt();

    }

    // Improved ray generation with correct FOV handling
    private Ray generateRay(int pixelX, int pixelY) {
        // 1. Aspect ratio (proporción de la imagen)
        double aspectRatio = (double) width / height;

        // 2. Escala de FOV (campo de visión vertical)
        double fovScale = Math.tan(Math.toRadians(fov * 0.5));

        // 3. Coordenadas normalizadas del píxel (NDC)
        double ndcX = (2.0 * (pixelX + 0.5) / width - 1.0); // va de -1 a 1
        double ndcY = 1.0 - (2.0 * (pixelY + 0.5) / height); // va de 1 a -1 (invertido)

        // 4. Escalar según FOV y proporción de aspecto
        double cameraX = ndcX * aspectRatio * fovScale;
        double cameraY = ndcY * fovScale;

        // 5. Crear el vector de dirección hacia -Z (mirando al fondo de la escena)
        Vector3D direction = new Vector3D(cameraX, cameraY, -1).normalize();

        // 6. Aplicar rotación de la cámara si existe
        if (rotation != null && (rotation.getX() != 0 || rotation.getY() != 0 || rotation.getZ() != 0)) {
            direction = direction.rotateVector(rotation);
        }

        // 7. Devolver el rayo desde el origen de la cámara hacia la dirección calculada
        return new Ray(origin, direction);
    }



    public BufferedImage getImage() {
        return image;
    }
}