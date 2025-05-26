package Objects;

import Lights.Light;
import Lights.LightIntersection;
import Materials.BlingPhongMaterial;
import Materials.Material;
import vectors.Intersection;
import vectors.Vector3D;
import vectors.Ray;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Camera class is responsible for simulating a virtual camera in a 3D scene.
 * It casts rays through a view frustum to detect object intersections and calculates
 * pixel colors using lighting information. Rendering is performed in parallel using tiles.
 * The camera supports features like reflection, refraction, and multi-bounce light tracing.
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
    private int bounces;

    //Depth of field
    // Parámetros de profundidad de campo
    private double focalDistance;      // Distancia al plano de enfoque perfecto
    private double aperture;          // Tamaño de la apertura (simulación f-stop)
    private boolean enableDepthOfField; // Activar/desactivar el efecto
    private int dofSamples;           // Número de muestras para el blur

    private final int TileSize = 64;
    private final int SAMPLES_PER_PIXEL= 9;

    // Progress tracking
    private AtomicInteger completedTiles = new AtomicInteger(0);
    private int totalTiles = 0;
    private boolean showProgress = true;
    private long renderStartTime;

    /**
     * Gets the maximum number of reflection/refraction bounces.
     * @return The current bounce limit.
     */
    public int getBounces() {
        return bounces;
    }

    /**
     * Sets the maximum number of reflection/refraction bounces.
     * @param bounces The new bounce limit (must be >= 0).
     */
    public void setBounces(int bounces) {
        this.bounces = bounces;
    }

    /**
     * Gets the camera's position in world space.
     * @return The origin point of the camera.
     */
    public Vector3D getOrigin() {
        return origin;
    }

    /**
     * Sets the camera's position in world space.
     * @param origin The new camera position.
     */
    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    /**
     * Gets the camera's rotation angles (in degrees) around each axis.
     * @return The rotation vector (x, y, z angles).
     */
    public Vector3D getRotation() {
        return rotation;
    }

    /**
     * Sets the camera's rotation angles (in degrees) around each axis.
     * @param rotation The new rotation vector (x, y, z angles).
     */
    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
    }

    /**
     * Gets the distance to the near clipping plane.
     * @return The near plane distance.
     */
    public double getNearplane() {
        return nearplane;
    }

    /**
     * Sets the distance to the near clipping plane.
     * @param nearplane The new near plane distance (must be > 0).
     */
    public void setNearplane(double nearplane) {
        this.nearplane = nearplane;
    }

    /**
     * Gets the distance to the far clipping plane.
     * @return The far plane distance.
     */
    public double getFarplane() {
        return farplane;
    }

    /**
     * Sets the distance to the far clipping plane.
     * @param farplane The new far plane distance (must be > near plane).
     */
    public void setFarplane(double farplane) {
        this.farplane = farplane;
    }

    /**
     * Sets the target image buffer for rendering.
     * @param image The BufferedImage to render into.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Gets the width of the rendered image in pixels.
     * @return The image width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the rendered image in pixels.
     * @param width The new image width (must be > 0).
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the rendered image in pixels.
     * @return The image height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the rendered image in pixels.
     * @param height The new image height (must be > 0).
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the camera's field of view in degrees.
     * @return The vertical FOV angle.
     */
    public double getFov() {
        return fov;
    }

    /**
     * Sets the camera's field of view in degrees.
     * @param fov The new FOV angle (must be > 0 and < 180).
     */
    public void setFov(double fov) {
        this.fov = fov;
    }

    /**
     * Returns the tile size used for parallel rendering.
     * @return The tile size in pixels.
     */
    public int getTileSize() {
        return TileSize;
    }

    /**
     * Enables or disables progress display during rendering.
     * @param showProgress True to show progress, false to hide.
     */
    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    /**
     * Constructs a Camera object with specified parameters.
     *
     * @param origin    The position of the camera in world space.
     * @param rotation  The rotation angles (in degrees) around each axis.
     * @param nearplane Distance to the near clipping plane.
     * @param farplane  Distance to the far clipping plane.
     * @param width     Width of the output image in pixels.
     * @param height    Height of the output image in pixels.
     * @param fov       Vertical field of view in degrees.
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
        setBounces(0);
    }

    /**
     * Renders the scene by shooting rays from the camera through each pixel.
     * This method uses tile-based multithreading for parallelism, dividing the
     * image into tiles that are processed concurrently by a thread pool.
     *
     * @param objects The list of 3D objects in the scene.
     * @param lights  The list of light sources in the scene.
     */
    public void shot(List<Object3D> objects, List<Light> lights) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        int TILE_SIZE = getTileSize();

        // Calculate total number of tiles for progress tracking
        int tilesX = (int)Math.ceil((double)width / TILE_SIZE);
        int tilesY = (int)Math.ceil((double)height / TILE_SIZE);
        totalTiles = tilesX * tilesY;
        completedTiles.set(0);
        renderStartTime = System.currentTimeMillis();

        if (showProgress) {
            System.out.println("Starting render: " + width + "x" + height + " (" + totalTiles + " tiles)");
            System.out.println("Progress: 0%");
        }

        for (int tileY = 0; tileY < height; tileY += TILE_SIZE) {
            for (int tileX = 0; tileX < width; tileX += TILE_SIZE) {
                final int startX = tileX;
                final int startY = tileY;

                executor.execute(() -> {
                    renderTile(startX, startY, TILE_SIZE, objects, lights);
                    updateProgress();
                });
            }
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
            if (showProgress) {
                // Final rendering stats
                long totalTime = System.currentTimeMillis() - renderStartTime;
                System.out.println("\nRendering complete!");
                System.out.println("Total time: " + formatTime(totalTime));
                System.out.println("Resolution: " + width + "x" + height + " pixels");
                System.out.println("Bounce depth: " + bounces);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted on parallelization of ray shooting");
        }
    }

    /**
     * Updates and displays the rendering progress.
     */
    private synchronized void updateProgress() {
        if (!showProgress) return;

        int completed = completedTiles.incrementAndGet();
        int percentage = (completed * 100) / totalTiles;

        // Calculate ETA
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - renderStartTime;
        long estimatedTotalTime = (long)(elapsedTime / ((double)completed / totalTiles));
        long remainingTime = estimatedTotalTime - elapsedTime;

        // Only update display at certain intervals to avoid console spam
        if (completed == 1 || completed == totalTiles || completed % (Math.max(1, totalTiles / 100)) == 0) {
            System.out.print("\rProgress: " + percentage + "% | " +
                    "Tiles: " + completed + "/" + totalTiles + " | " +
                    "Elapsed: " + formatTime(elapsedTime) + " | " +
                    "ETA: " + formatTime(remainingTime));
        }
    }

    /**
     * Formats time in milliseconds to a human-readable string.
     *
     * @param timeMs Time in milliseconds
     * @return Formatted time string (HH:MM:SS)
     */
    private String formatTime(long timeMs) {
        long seconds = timeMs / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return String.format("%02d:%02d:%02d",
                hours,
                minutes % 60,
                seconds % 60);
    }

    /**
     * Traces a single ray through the scene, calculating its color contribution.
     * This method handles intersection testing, lighting calculations, and recursive
     * reflection/refraction based on material properties.
     *
     * @param ray     The ray to trace through the scene.
     * @param objects The list of 3D objects to test for intersections.
     * @param lights  The list of light sources for illumination.
     * @param bounces The remaining number of reflection/refraction bounces allowed.
     * @return The final RGB color as an integer (0xRRGGBB).
     */
    private int traceRay(Ray ray, List<Object3D> objects, List<Light> lights, int bounces) {
        double closestDist = Double.MAX_VALUE;
        Object3D closestObject = null;
        Object3D triangleHit = null;
        Triangle triaAux = null;

        // Find closest intersection with objects in the scene
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

        // View direction - from intersection point to camera/ray origin
        Vector3D viewDirection = ray.getOrigin().subtract(intersectionPoint).normalize();

        LightIntersection lightIntersection = new LightIntersection(
                objects, lights, closestObject, ray.getOrigin(), ray.getDirection(), getOrigin());

        // Calculate direct lighting
        int directColor;
        if (triangleHit == null) {
            directColor = closestObject.getColorInt();
        } else {
            directColor = lightIntersection.lightsIntersection(triangleHit, intersectionPoint);
        }

        // If no more bounces or not a material with reflection/refraction, return direct color
        if (bounces <= 0) {
            return directColor;
        }

        // Get material and normal
        Material material = null;
        Vector3D normal = null;

        if (closestObject instanceof Triangle) {
            material = closestObject.getMaterial();
            normal = ((Triangle) closestObject).getNormal(intersectionPoint);
        } else if (triangleHit != null) {
            material = triangleHit.getMaterial();
            normal = ((Triangle) triangleHit).getNormal(intersectionPoint);
        } else if (closestObject instanceof Sphere) {
            material = closestObject.getMaterial();
            // For spheres, calculate normal as normalized vector from center to intersection point
            normal = intersectionPoint.subtract(((Sphere) closestObject).getPosition()).normalize();
        }

        // If no material or normal, return direct color
        if (material == null || normal == null) {
            return directColor;
        }

        // Ensure normal is pointing in the correct direction (facing the ray)
        if (normal.dot(ray.getDirection()) > 0) {
            normal = normal.scale(-1);
        }

        int finalColor = directColor;

        // Handle BlingPhong material properties
        if (material instanceof BlingPhongMaterial bpMaterial) {
            double reflectivity = bpMaterial.getReflectivity();
            double refractionIndex = bpMaterial.getRefraction();

            // Calculate reflection if material is reflective
            if (reflectivity > 0.0) {
                int reflectionColor = calculateReflection(ray, intersectionPoint, normal,
                        objects, lights, bounces);
                finalColor = blendColors(directColor, reflectionColor, reflectivity);
            }

            // Calculate refraction if material has refraction
            if (refractionIndex > 0.0) {
                int refractionColor = calculateRefraction(ray, intersectionPoint, normal, refractionIndex,
                        objects, lights, bounces);

                // Blend with existing color (which may already include reflection)
                finalColor = blendColors(finalColor, refractionColor, refractionIndex);
            }
        }

        return finalColor;
    }

    /**
     * Calculates the reflection color by generating and tracing a reflected ray.
     *
     * @param ray The incoming ray being reflected.
     * @param intersectionPoint The point where reflection occurs.
     * @param normal The surface normal at the intersection point.
     * @param objects The list of scene objects.
     * @param lights The list of light sources.
     * @param bounces Remaining bounce count.
     * @return The reflected color as RGB integer.
     */
    private int calculateReflection(Ray ray, Vector3D intersectionPoint, Vector3D normal,
                                    List<Object3D> objects, List<Light> lights, int bounces) {
        // Calculate reflection direction using R = I - 2(N·I)N
        Vector3D reflectionDir = ray.getDirection().subtract(
                normal.scale(2 * ray.getDirection().dot(normal))).normalize();

        // Create reflection ray with a small offset to avoid self-intersection
        Vector3D offsetPoint = intersectionPoint.add(reflectionDir.scale(1e-4));
        Ray reflectRay = new Ray(offsetPoint, reflectionDir);

        // Trace the reflection ray (recursive call with one less bounce)
        return traceRay(reflectRay, objects, lights, bounces - 1);
    }

    /**
     * Calculates the refraction color by generating and tracing a refracted ray.
     * Handles both refraction and Fresnel effects for realistic transparency.
     *
     * @param ray The incoming ray being refracted.
     * @param intersectionPoint The point where refraction occurs.
     * @param normal The surface normal at the intersection point.
     * @param refractionIndex The material's index of refraction.
     * @param objects The list of scene objects.
     * @param lights The list of light sources.
     * @param bounces Remaining bounce count.
     * @return The refracted color as RGB integer.
     */
    private int calculateRefraction(Ray ray, Vector3D intersectionPoint, Vector3D normal,
                                    double refractionIndex, List<Object3D> objects, List<Light> lights, int bounces) {

        // Normalize the incident direction vector
        Vector3D incident = ray.getDirection().normalize();

        // Determine if the ray is entering or exiting the object
        boolean entering = incident.dot(normal) < 0;

        // Adjust normal based on whether we're entering or exiting
        Vector3D surfaceNormal = entering ? normal : normal.scale(-1);

        // Calculate the ratio of refractive indices
        double n1 = entering ? 1.0 : refractionIndex;  // Index of medium ray is coming from
        double n2 = entering ? refractionIndex : 1.0;  // Index of medium ray is going to
        double eta = n1 / n2;

        // Cosine of incident angle (using dot product with normal)
        double cosTheta1 = -incident.dot(surfaceNormal);  // Negative because vectors point in opposite directions

        // Calculate the sine squared of refracted angle using Snell's Law
        double sinTheta2Squared = eta * eta * (1.0 - cosTheta1 * cosTheta1);

        // Total internal reflection check
        if (sinTheta2Squared > 1.0) {
            // Total internal reflection, return only reflection color
            return calculateReflection(ray, intersectionPoint, surfaceNormal, objects, lights, bounces);
        }

        // Calculate cosine of refracted angle
        double cosTheta2 = Math.sqrt(1.0 - sinTheta2Squared);

        // Calculate refracted ray direction
        // Using formula: T = η * I + (η * cosθ₁ - cosθ₂) * N
        Vector3D refractionDir = incident.scale(eta)
                .add(surfaceNormal.scale(eta * cosTheta1 - cosTheta2));
        refractionDir = refractionDir.normalize();

        // Slightly offset intersection point in refraction direction
        // to avoid self-intersection
        Vector3D offsetPoint = intersectionPoint.add(refractionDir.scale(1e-4));

        // Create refracted ray
        Ray refractRay = new Ray(offsetPoint, refractionDir);

        // Trace the refracted ray
        int refractionColor = traceRay(refractRay, objects, lights, bounces - 1);

        // Also calculate reflection (Fresnel effect)
        int reflectionColor = calculateReflection(ray, intersectionPoint, surfaceNormal, objects, lights, bounces);

        // Calculate Fresnel coefficient to blend refraction and reflection
        double fresnel = calculateFresnelTerm(cosTheta1, n1, n2);

        // Blend refraction and reflection colors according to Fresnel coefficient
        return blendColors(refractionColor, reflectionColor, fresnel);
    }

    /**
     * Calculates the Fresnel term using Schlick's approximation.
     * Determines how much light is reflected vs refracted at a surface.
     *
     * @param cosTheta Cosine of the angle between incident ray and normal
     * @param n1 Refractive index of first medium
     * @param n2 Refractive index of second medium
     * @return Fresnel reflection coefficient (0-1)
     */
    private double calculateFresnelTerm(double cosTheta, double n1, double n2) {
        // Reflection at normal incidence
        double r0 = Math.pow((n1 - n2) / (n1 + n2), 2);

        // Schlick's approximation
        return r0 + (1 - r0) * Math.pow(1 - Math.abs(cosTheta), 5);
    }

    /**
     * Blends two colors based on a blend factor using linear interpolation.
     *
     * @param baseColor The base color to blend from (RGB integer).
     * @param blendColor The color to blend toward (RGB integer).
     * @param factor The blending factor between 0.0 (all base) and 1.0 (all blend).
     * @return The blended color as RGB integer.
     */
    private int blendColors(int baseColor, int blendColor, double factor) {
        // Clamp factor to valid range
        factor = Math.min(1.0, Math.max(0.0, factor));

        int r1 = (baseColor >> 16) & 0xFF;
        int g1 = (baseColor >> 8) & 0xFF;
        int b1 = baseColor & 0xFF;

        int r2 = (blendColor >> 16) & 0xFF;
        int g2 = (blendColor >> 8) & 0xFF;
        int b2 = blendColor & 0xFF;

        int r = (int)(r1 * (1 - factor) + r2 * factor);
        int g = (int)(g1 * (1 - factor) + g2 * factor);
        int b = (int)(b1 * (1 - factor) + b2 * factor);

        // Clamp values to valid range
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return (r << 16) | (g << 8) | b;
    }

    /**
     * Generates a primary ray from the camera through a specific pixel.
     * The ray direction is calculated using the camera's field of view and aspect ratio.
     *
     * @param pixelX The x-coordinate of the pixel (0 to width-1).
     * @param pixelY The y-coordinate of the pixel (0 to height-1).
     * @return A Ray object originating from the camera through the specified pixel.
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
     * Gets the rendered image after calling {@link #shot(List, List)}.
     * The image will be empty/null if shot() hasn't been called yet.
     *
     * @return The BufferedImage containing the rendered scene.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Renders a specific tile of the image by generating and tracing rays for each pixel.
     * This method is designed to be called by worker threads during parallel rendering.
     *
     * @param startX  The starting x-coordinate of the tile (left edge).
     * @param startY  The starting y-coordinate of the tile (top edge).
     * @param tileSize The size of the tile (in pixels).
     * @param objectslist  The list of 3D objects in the scene.
     * @param lights   The list of light sources in the scene.
     */
    private void renderTile(int startX, int startY, int tileSize, List<Object3D> objectslist, List<Light> lights) {
        int endX = Math.min(startX + tileSize, width);
        int endY = Math.min(startY + tileSize, height);
        int bounces = getBounces();
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                Ray ray = generateRay(x, y);

                int pixelColor = traceRay(ray, objectslist, lights,bounces);

                image.setRGB(x, y, pixelColor);
            }
        }
    }
}