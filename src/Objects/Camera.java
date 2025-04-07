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
        Ray[][] rays = new Ray[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Ray ray = generateRay(rays[i][j] ,i, j);

                double closestDist = Double.MAX_VALUE;
                int pixelColor = 0xFFFFFF; // Default background color

                for (Object3D obj : objects) {
                    if (obj instanceof Sphere) {
                        Sphere sphere = (Sphere) obj;
                        double dist = Intersection.sphere(ray, sphere,this.nearplane, this.farplane);

                        if (dist > 0.0 && dist < closestDist) {
                            closestDist = dist;
                            pixelColor = sphere.getColorInt();

                            /*System.out.println("Ray origin: " + ray.getOrigin() +
                                    " direction: " + ray.getDirection());
                            System.out.println("Intersection distance: " + dist);*/

                        }

                    }

                    if(obj instanceof Triangle){
                        Triangle triangle = (Triangle) obj;
                        double dist = Intersection.triangle(ray,triangle,this.nearplane,this.farplane,0.1);
                        if (dist > 0.0 && dist < closestDist) {
                            closestDist = dist;
                            pixelColor = triangle.getColorInt();

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
    private Ray generateRay(Ray ray, int pixelX, int pixelY) {
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
        direction.normalize();

        // Apply camera rotation if needed (not implemented in current code)
        return  ray = new Ray(origin, direction);
    }



    // Getter for the rendered image
    public BufferedImage getImage() {
        return image;
    }
}
