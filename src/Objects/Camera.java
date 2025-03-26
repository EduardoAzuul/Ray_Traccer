package Objects;

import vectors.Intersection;
import vectors.Point;
import vectors.Ray;
import vectors.Vector3D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


public class Camera {
    private Point origin;
    private Point rotation;
    private double nearplane;
    private double farplane;
    private BufferedImage image;
    private int width;
    private int height;
    private double fov;  // Field of view for perspective projection

    // Constructor
    public Camera(Point origin, Point rotation, double nearplane, double farplane, int width, int height, double fov) {
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
                    if (obj instanceof Sphere) {
                        Sphere sphere = (Sphere) obj;
                        Double dist = Intersection.intersect(ray, sphere);

                        if (dist != null && dist > 0.0 && dist < closestDist) {
                            closestDist = dist;
                            pixelColor = sphere.getColorInt();

                            System.out.println("Ray origin: " + ray.getOrigin() +
                                    " direction: " + ray.getDirection());
                            System.out.println("Intersection distance: " + dist);

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
        Point direction = new Point(screenX, screenY, -1);
        direction.normalize();

        // Apply camera rotation if needed (not implemented in current code)
        return new Ray(origin, direction);
    }



    // Getter for the rendered image
    public BufferedImage getImage() {
        return image;
    }
}
