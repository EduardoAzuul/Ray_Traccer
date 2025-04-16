package Objects;

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
    private double fov;  // Field of view for perspective projection

    public Vector3D getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
    }

    public double getNearplane() {
        return nearplane;
    }

    public void setNearplane(double nearplane) {
        this.nearplane = nearplane;
    }

    public double getFarplane() {
        return farplane;
    }

    public void setFarplane(double farplane) {
        this.farplane = farplane;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    // Constructor
    public Camera(Vector3D origin, Vector3D rotation, double nearplane, double farplane, int width, int height, double fov) {
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
    public void shot(List<Object3D> objects) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Ray ray = generateRay(i, j);

                double closestDist = Double.MAX_VALUE;
                int pixelColor = 0xFFFFFF; // Default background color

                for (Object3D obj : objects) {
                    if (obj instanceof Sphere sphere) {
                        double dist = Intersection.sphere(ray, sphere, getNearplane(), getFarplane());

                        if (dist > 0.0 && dist < closestDist) {
                            closestDist = dist;
                            pixelColor = sphere.getColorInt();

                            /*System.out.println("Ray origin: " + ray.getOrigin() +
                                    " direction: " + ray.getDirection());
                            System.out.println("Intersection distance: " + dist);*/
                        }
                    }

                    if (obj instanceof Triangle triangle) {
                        double dist = Intersection.triangle(ray, triangle, getNearplane(), getFarplane(), 0.1);
                        if (dist > 0.0 && dist < closestDist) {
                            closestDist = dist;
                            pixelColor = triangle.getColorInt();

                            /*System.out.println("Ray origin: " + ray.getOrigin() +
                                    " direction: " + ray.getDirection());
                            System.out.println("Intersection distance: " + dist);*/
                        }
                    }

                    if (obj instanceof ObjObject objObject) {
                        double dist = Intersection.obj(ray, objObject, getNearplane(), getFarplane(), 0.1);
                        if (dist > 0.0 && dist < closestDist) {
                            closestDist = dist;
                            pixelColor = objObject.getColorInt();

                            /*System.out.println("Ray origin: " + ray.getOrigin() +
                                    " direction: " + ray.getDirection());
                            System.out.println("Intersection distance: " + dist);*/
                        }
                    }
                }

                image.setRGB(i, j, pixelColor);
            }
        }
    }

    // Generate a ray based on the camera's parameters
    private Ray generateRay(int pixelX, int pixelY) {
        // Aspect ratio calculation
        double aspectRatio = (double) width / height;

        // Convert pixel coordinates to normalized device coordinates
        double ndcX = (2.0 * ((pixelX + 0.5) / width) - 1.0);
        double ndcY = 1.0 - 2.0 * ((pixelY + 0.5) / height);

        // Adjust for FOV and aspect ratio
        double tanFov = Math.tan(Math.toRadians(fov / 2.0));
        double screenX = ndcX * aspectRatio * tanFov;
        double screenY = ndcY * tanFov;

        // Create direction vector (z is -1 because we look along negative Z)
        Vector3D direction = new Vector3D(screenX, screenY, -1);
        Vector3D normalizedDir = direction.normalized();

        // Apply camera rotation if needed (not implemented in current code)
        return new Ray(origin, normalizedDir);
    }

    // Getter for the rendered image
    public BufferedImage getImage() {
        return image;
    }
}
