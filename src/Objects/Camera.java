package Objects;

import Lights.Light;
import Lights.LightIntersection;
import vectors.Intersection;
import vectors.Vector3D;
import vectors.Ray;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Camera class is responsible for simulating a virtual camera in a 3D scene.
 * It casts rays through a view frustum to detect object intersections and calculates
 * pixel colors using lighting information. Rendering is performed in parallel using tiles.
 *
 * @author José Eduardo Moreno Paredes
 */
public class Camera {
    private Vector3D origin;
    private Vector3D rotation;
    private double nearplane;
    private double farplane;
    private BufferedImage image;
    private int width;
    private int height;
    private double fov;

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

    private final int TileSize = 32;

    /**
     * Returns the tile size used for parallel rendering.
     * @return tile size in pixels.
     */
    public int getTileSize() {
        return TileSize;
    }



    /**
     * Constructs a Camera object.
     *
     * @param origin    The position of the camera.
     * @param rotation  The rotation (orientation) of the camera.
     * @param nearplane Near clipping plane distance.
     * @param farplane  Far clipping plane distance.
     * @param width     Width of the image in pixels.
     * @param height    Height of the image in pixels.
     * @param fov       Field of view in degrees.
     */
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

    /**
     * Renders the scene by shooting rays from the camera through each pixel.
     * This method uses tile-based multithreading for parallelism.
     *
     * @param objects The list of 3D objects in the scene.
     * @param lights  The list of light sources in the scene.
     */
    public void shot(List<Object3D> objects, List<Light> lights) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        int TILE_SIZE = getTileSize();
        for (int tileY = 0; tileY < height; tileY += TILE_SIZE) {
            for (int tileX = 0; tileX < width; tileX += TILE_SIZE) {
                final int startX = tileX;
                final int startY = tileY;

                executor.execute(() -> renderTile(startX, startY, TILE_SIZE, objects, lights));
            }
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("Interrupted on parallelization of ray shooting");
        }
    }

    /**
     * Traces a single ray and determines its resulting color based on intersections and lighting.
     *
     * @param ray     The ray to trace.
     * @param objects The list of 3D objects in the scene.
     * @param lights  The list of light sources in the scene.
     * @return The RGB color value resulting from the ray's interaction.
     */
    private int traceRay(Ray ray, List<Object3D> objects, List<Light> lights) {
        double closestDist = Double.MAX_VALUE;
        Object3D closestObject = null;
        Object3D triangleHit = null;
        Triangle triaAux = null;

        for (Object3D obj : objects) {
            double dist = -1.0;

            if (obj instanceof Sphere sphere) {
                dist = Intersection.sphere(ray, sphere, nearplane, farplane);
            } else if (obj instanceof Triangle triangle) {
                dist = Intersection.triangle(ray, triangle, nearplane, farplane, 1e-4);
                triaAux = triangle;
            } else if (obj instanceof ObjObject objObject) {
                triaAux = Intersection.objTriangleIntersected(ray, objObject, nearplane, farplane, 1e-4);

                if (triaAux != null) {
                    dist = Intersection.triangle(ray, triaAux, nearplane, farplane, 1e-4);
                }
            }

            if (dist > nearplane && dist < farplane && dist < closestDist) {
                closestDist = dist;
                closestObject = obj;
                triangleHit = triaAux;
            }
        }

        if (closestObject == null) {
            return 0x000000; // No intersection, return black
        }

        Vector3D intersectionPoint = ray.getOrigin().add(ray.getDirection().scale(closestDist));

        LightIntersection lightIntersection = new LightIntersection(
                objects, lights, closestObject, ray.getOrigin(), ray.getDirection(), getOrigin());

        /***************** FOR MATTE OBJECTS************************/
        if (triangleHit != null) {
            return lightIntersection.lightsIntersectionMatte(triangleHit, intersectionPoint);
        }

        return closestObject.getColorInt();
    }

    /**
     * Generates a ray from the camera through a specific pixel.
     *
     * @param pixelX The x-coordinate of the pixel.
     * @param pixelY The y-coordinate of the pixel.
     * @return The generated ray.
     */
    private Ray generateRay(int pixelX, int pixelY) {
        double aspectRatio = (double) width / height;
        double fovScale = Math.tan(Math.toRadians(fov * 0.5));
        double ndcX = (2.0 * (pixelX + 0.5) / width - 1.0);
        double ndcY = 1.0 - (2.0 * (pixelY + 0.5) / height);
        double cameraX = ndcX * aspectRatio * fovScale;
        double cameraY = ndcY * fovScale;

        Vector3D direction = new Vector3D(cameraX, cameraY, -1).normalize();

        if (rotation != null && (rotation.getX() != 0 || rotation.getY() != 0 || rotation.getZ() != 0)) {
            direction = direction.rotateVector(rotation);
        }

        return new Ray(origin, direction);
    }

    /**
     * Returns the rendered image after calling {@link #shot(List, List)}.
     *
     * @return The BufferedImage containing the rendered scene.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Renders a specific tile of the image by generating and tracing rays for each pixel.
     *
     * @param startX  The starting x-coordinate of the tile.
     * @param startY  The starting y-coordinate of the tile.
     * @param tileSize The size of the tile (in pixels).
     * @param objects  The list of 3D objects in the scene.
     * @param lights   The list of light sources in the scene.
     */
    private void renderTile(int startX, int startY, int tileSize, List<Object3D> objects, List<Light> lights) {
        int endX = Math.min(startX + tileSize, width);
        int endY = Math.min(startY + tileSize, height);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                Ray ray = generateRay(x, y);
                int pixelColor = traceRay(ray, objects, lights);
                image.setRGB(x, y, pixelColor);
            }
        }
    }
}
