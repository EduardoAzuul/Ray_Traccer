package Lights;

import Materials.BlingPhongMaterial;
import Objects.ObjObject;
import Objects.Object3D;
import Objects.Sphere;
import Objects.Triangle;
import vectors.Intersection;
import vectors.Ray;
import vectors.Vector3D;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Handles lighting calculations for 3D objects in a scene.
 * Manages interactions between light sources and objects,
 * including diffuse, specular, and ambient lighting components.
 * Supports multiple light types (directional, point, spot) and
 * Blinn-Phong shading model.
 *
 * @author José Eduardo Moreno Paredes
 */
public class LightIntersection {
    /** List of all objects in the scene */
    private List<Object3D> object3DList = new ArrayList<>();
    /** List of all light sources in the scene */
    private List<Light> lightList = new ArrayList<>();
    /** The object that was hit by the ray */
    private Object3D objectHit;
    /** Origin point of the intersection ray */
    private Vector3D origin;
    /** Direction of the intersection ray */
    private Vector3D direction;
    /** Position of the camera/viewer */
    private Vector3D cameraOrigin;

    // Lighting calculation constants
    /** Base ambient light intensity (5%) */
    private static final double AMBIENT_INTENSITY = 0.05;
    /** Light attenuation coefficient */
    private static final double ATTENUATION_FACTOR = 0.03;
    /** Small offset to prevent self-shadowing artifacts */
    private static final double SHADOW_BIAS = 0.0001;
    /** Precision value for triangle intersections */
    private static final double TRIANGLE_EPSILON = 1e-4;
    /** Minimum light contribution threshold (1%) */
    private static final double MIN_LIGHT_INTENSITY = 0.01;

    // Getters and Setters
    /**
     * Gets the list of 3D objects in the scene
     * @return List of Object3D instances
     */
    public List<Object3D> getObject3DList() {
        return object3DList;
    }

    /**
     * Sets the list of 3D objects in the scene
     * @param object3DList New list of Object3D instances
     */
    public void setObject3DList(List<Object3D> object3DList) {
        this.object3DList = object3DList;
    }

    /**
     * Gets the list of light sources in the scene
     * @return List of Light instances
     */
    public List<Light> getLightList() {
        return lightList;
    }

    /**
     * Sets the list of light sources in the scene
     * @param lightList New list of Light instances
     */
    public void setLightList(List<Light> lightList) {
        this.lightList = lightList;
    }

    /**
     * Gets the object that was intersected
     * @return The intersected Object3D
     */
    public Object3D getObjectHit() {
        return objectHit;
    }

    /**
     * Sets the intersected object
     * @param objectHit The Object3D that was hit
     */
    public void setObjectHit(Object3D objectHit) {
        this.objectHit = objectHit;
    }

    /**
     * Gets the ray origin point
     * @return Origin vector
     */
    public Vector3D getOrigin() {
        return origin;
    }

    /**
     * Sets the ray origin point
     * @param origin New origin vector
     */
    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    /**
     * Gets the ray direction
     * @return Direction vector (normalized)
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     * Sets the ray direction (automatically normalized)
     * @param direction New direction vector
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction.normalize();
    }

    /**
     * Gets the camera position
     * @return Camera origin vector
     */
    public Vector3D getCameraOrigin() {
        return cameraOrigin;
    }

    /**
     * Sets the camera position
     * @param cameraOrigin New camera position vector
     */
    public void setCameraOrigin(Vector3D cameraOrigin) {
        this.cameraOrigin = cameraOrigin;
    }

    /**
     * Creates a new LightIntersection instance
     * @param object3DList List of objects in the scene
     * @param lightList List of lights in the scene
     * @param objectHit The intersected object
     * @param origin Ray origin point
     * @param direction Ray direction (will be normalized)
     * @param cameraOrigin Camera position
     */
    public LightIntersection(List<Object3D> object3DList, List<Light> lightList,
                             Object3D objectHit, Vector3D origin, Vector3D direction,
                             Vector3D cameraOrigin) {
        setObject3DList(object3DList);
        setLightList(lightList);
        setObjectHit(objectHit);
        setOrigin(origin);
        setDirection(direction);
        setCameraOrigin(cameraOrigin);
    }

    /**
     * Calculates the final color at an intersection point considering all light sources
     * @param object The intersected object
     * @param point The intersection point in world coordinates
     * @return Final color as packed RGB integer (0xRRGGBB)
     */
    public int lightsIntersection(Object3D object, Vector3D point) {
        // Determine base color and shading model
        boolean useBlinnPhong = object.getMaterial() instanceof BlingPhongMaterial;
        int objectColor = useBlinnPhong ? object.getMaterial().getColor() : object.getColorInt();

        // Apply texture if available
        if (object instanceof Triangle) {
            if (((Triangle) object).getTexture() != null) {
                objectColor = ((Triangle) object).getTextureColor(point);
            }
        }

        // Start with ambient lighting
        int ambientColor = calculateAmbientLight(objectColor, AMBIENT_INTENSITY);
        int finalColor = ambientColor;
        boolean anyLightHits = false;

        // Process each light source
        for (Light light : lightList) {
            // Skip lights with insignificant contribution
            if (!(light instanceof DirectionalLight)) {
                double distance = point.subtract(light.getPosition()).length();
                double attenuatedIntensity = calculateAttenuatedIntensity(light, distance);
                if (attenuatedIntensity < MIN_LIGHT_INTENSITY) {
                    continue;
                }
            }

            // Skip if light is blocked (in shadow)
            if (isLightBlocked(light, point, object)) {
                continue;
            }

            // Calculate light contribution based on type and shading model
            int lightContribution = 0;
            if (useBlinnPhong && object instanceof Triangle triangle) {
                lightContribution = calculateBlinnPhongLighting(triangle, point, light, objectColor);
            } else if (object instanceof Triangle triangle) {
                if (light instanceof DirectionalLight directionalLight) {
                    lightContribution = calculateDirectionalLighting(triangle, point, directionalLight, objectColor);
                } else if (light instanceof SpotLight spotLight && isInSpotlightCone(spotLight, point)) {
                    lightContribution = calculateSpotLighting(triangle, point, spotLight, objectColor);
                } else if (light instanceof PointLight pointLight) {
                    lightContribution = calculatePointLighting(triangle, point, pointLight, objectColor);
                }
            }

            // Accumulate light contribution
            if (lightContribution != 0) {
                finalColor = blendColors(finalColor, lightContribution);
                anyLightHits = true;
            }
        }

        return anyLightHits ? finalColor : ambientColor;
    }

    /**
     * Calculate attenuated light intensity based on distance
     * @return the attenuated intensity value
     */
    private double calculateAttenuatedIntensity(Light light, double distance) {
        double baseIntensity = light.getIntensity();

        // Apply inverse square law attenuation
        return baseIntensity / (1.0 + ATTENUATION_FACTOR * distance * distance);
    }

    /**
     * Check if light is blocked by any object (shadow calculation)
     */
    private boolean isLightBlocked(Light light, Vector3D point, Object3D currentObject) {
        // Skip shadow check for directional lights
        if (light instanceof DirectionalLight) {
            return false;
        }

        // Calculate vector from point to light
        Vector3D lightVector = light.getPosition().subtract(point);
        double lightDistance = lightVector.length();
        Vector3D rayDirection = lightVector.normalize();
        Ray ray = new Ray(point, rayDirection);

        // Check for intersections with all objects
        for (Object3D obj : getObject3DList()) {
            // Skip self to prevent self-shadowing
            if (obj == currentObject) continue;

            double dist = -1.0;

            if (obj instanceof Sphere sphere) {
                dist = Intersection.sphere(ray, sphere, SHADOW_BIAS, lightDistance);
            } else if (obj instanceof Triangle triangle) {
                dist = Intersection.triangle(ray, triangle, SHADOW_BIAS, lightDistance, TRIANGLE_EPSILON);
            } else if (obj instanceof ObjObject objObject) {
                dist = Intersection.obj(ray, objObject, SHADOW_BIAS, lightDistance, TRIANGLE_EPSILON);
            }

            // If intersection found between point and light
            if (dist > 0.0 && dist < lightDistance) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculate Blinn-Phong lighting model contribution
     */
    private int calculateBlinnPhongLighting(Triangle triangle, Vector3D point, Light light, int objectColor) {
        // Calculate diffuse and specular factors
        double diffuseFactor = calculateDiffuseFactor(triangle, point, light);
        double specularFactor = calculateSpecularFactor(triangle, point, light);

        // Apply diffuse lighting to object color
        int lightColor = applyDiffuseToColor(objectColor, light.getColorint(), diffuseFactor);

        // Calculate specular highlight (white highlight based on specular factor)
        int specularColor = applySpecularHighlight(specularFactor);

        // Combine diffuse and specular components
        return blendColors(lightColor, specularColor);
    }

    /**
     * Calculate diffuse factor (Lambert's cosine law)
     */
    private double calculateDiffuseFactor(Triangle triangle, Vector3D point, Light light) {
        Vector3D normal = triangle.getNormal(point).normalize();
        Vector3D lightDir = calculateLightDirection(triangle, point, light).multiplyByScalar(-1);
        double intensity = calculateLightIntensity(point, light);

        // Cosine of angle between normal and light direction
        double dotNL = normal.dot(lightDir);

        // Return diffuse factor clamped to [0,1]
        return max(0, dotNL) * intensity;
    }

    /**
     * Calculate specular factor using Blinn-Phong model
     */
    private double calculateSpecularFactor(Triangle triangle, Vector3D point, Light light) {
        // Only calculate specular if the object has a Blinn-Phong material
        if (!(triangle.getMaterial() instanceof BlingPhongMaterial material)) {
            return 0.0;
        }

        Vector3D normal = triangle.getNormal(point).normalize();
        Vector3D lightDir = calculateLightDirection(triangle, point, light).multiplyByScalar(-1);
        Vector3D viewDir = cameraOrigin.subtract(point).normalize().multiplyByScalar(-1);

        // Calculate half-vector between light and view direction
        Vector3D halfway = lightDir.add(viewDir).normalize();

        // Skip specular calculation if light is behind the surface
        if (normal.dot(lightDir) <= 0) {
            return 0.0;
        }

        // Calculate specular angle and apply shininess exponent
        double specAngle = max(0, normal.dot(halfway));
        double intensity = calculateLightIntensity(point, light);

        return material.getSpecular() * intensity * Math.pow(specAngle, material.getShininess());
    }

    /**
     * Apply specular highlight to color
     */
    private int applySpecularHighlight(double specularFactor) {
        // Clamp specular factor to [0,1] and convert to intensity
        int intensity = (int)(min(1.0, specularFactor) * 255);

        // Create white highlight with variable intensity
        return (intensity << 16) | (intensity << 8) | intensity;
    }

    /**
     * Calculate directional light contribution
     */
    private int calculateDirectionalLighting(Triangle triangle, Vector3D point, DirectionalLight light, int objectColor) {
        Vector3D normal = triangle.getNormal(point).normalize().multiplyByScalar(-1);
        Vector3D lightDir = light.getDirection().normalize().multiplyByScalar(-1);
        double intensity = light.getIntensity();

        // Calculate cosine of angle between normal and light
        double dotNL = normal.dot(lightDir);

        // Return black if light is behind the surface
        if (dotNL <= 0) {
            return 0x000000;
        }

        // Calculate final color
        return multiplyColors(objectColor, light.getColorint(), dotNL * intensity);
    }

    /**
     * Calculate spotlight contribution
     */
    private int calculateSpotLighting(Triangle triangle, Vector3D point, SpotLight light, int objectColor) {
        Vector3D normal = triangle.getNormal(point).normalize().multiplyByScalar(-1);
        Vector3D lightDir = calculateLightDirection(triangle, point, light);
        double intensity = calculateLightIntensity(point, light);

        // Calculate cosine of angle between normal and light
        double dotNL = normal.dot(lightDir);

        // Return black if light is behind the surface
        if (dotNL <= 0) {
            return 0x000000;
        }

        // Calculate final color
        return multiplyColors(objectColor, light.getColorint(), dotNL * intensity);
    }

    /**
     * Calculate point light contribution
     */
    private int calculatePointLighting(Triangle triangle, Vector3D point, PointLight light, int objectColor) {
        Vector3D normal = triangle.getNormal(point).normalize().multiplyByScalar(-1);
        Vector3D lightDir = calculateLightDirection(triangle, point, light);
        double intensity = calculateLightIntensity(point, light);

        // Calculate cosine of angle between normal and light
        double dotNL = normal.dot(lightDir);

        // Return black if light is behind the surface
        if (dotNL <= 0) {
            return 0x000000;
        }

        // Calculate final color
        return multiplyColors(objectColor, light.getColorint(), dotNL * intensity);
    }

    /**
     * Check if a point is within a spotlight's cone
     */
    private boolean isInSpotlightCone(SpotLight spotLight, Vector3D point) {
        // Direction FROM light TO point (consistent with lighting calculations)
        Vector3D lightToPoint = point.subtract(spotLight.getPosition()).normalize();

        // Direction that the spotlight is pointing (FROM the light)
        Vector3D spotDirection = spotLight.getDirection().normalize();

        // Calculate the cosine of the angle between the two vectors
        double cosAngle = lightToPoint.dot(spotDirection);

        // Convert the spotlight's cone angle to cosine for comparison
        double cosMaxAngle = Math.cos(Math.toRadians(spotLight.getAngle() / 2.0));

        // Check if the point is within the cone
        return cosAngle >= cosMaxAngle;
    }

    /**
     * Calculate light intensity at a point, considering distance attenuation
     */
    private double calculateLightIntensity(Vector3D point, Light light) {
        double baseIntensity = light.getIntensity();

        // No attenuation for directional lights
        if (light instanceof DirectionalLight) {
            return baseIntensity;
        }

        // Calculate distance for attenuation
        Vector3D lightVector = light.getPosition().subtract(point);
        double distance = lightVector.length();

        // Apply inverse square law attenuation
        return baseIntensity / (1.0 + ATTENUATION_FACTOR * distance * distance);
    }

    /**
     * Calculate direction from a point to a light source
     */
    private Vector3D calculateLightDirection(Triangle object, Vector3D point, Light light) {
        if (light instanceof DirectionalLight directionalLight) {
            // For directional lights, use the light's direction
            return directionalLight.getDirection().normalize();
        } else {
            // For point and spotlights, calculate direction from point to light
            return light.getPosition().subtract(point).normalize();
        }
    }

    /**
     * Apply diffuse lighting to a color
     */
    private int applyDiffuseToColor(int objectColor, int lightColor, double diffuseFactor) {
        int or = (objectColor >> 16) & 0xFF;
        int og = (objectColor >> 8) & 0xFF;
        int ob = objectColor & 0xFF;

        int lr = (lightColor >> 16) & 0xFF;
        int lg = (lightColor >> 8) & 0xFF;
        int lb = lightColor & 0xFF;

        // Calculate diffuse lighting
        int r = (int)((or * lr / 255.0) * diffuseFactor);
        int g = (int)((og * lg / 255.0) * diffuseFactor);
        int b = (int)((ob * lb / 255.0) * diffuseFactor);

        // Clamp values
        r = min(255, max(0, r));
        g = min(255, max(0, g));
        b = min(255, max(0, b));

        return (r << 16) | (g << 8) | b;
    }

    /**
     * Calculate ambient light component
     */
    private int calculateAmbientLight(int objectColor, double ambientIntensity) {
        int or = (objectColor >> 16) & 0xFF;
        int og = (objectColor >> 8) & 0xFF;
        int ob = objectColor & 0xFF;

        int r = (int)(or * ambientIntensity);
        int g = (int)(og * ambientIntensity);
        int b = (int)(ob * ambientIntensity);

        return (r << 16) | (g << 8) | b;
    }

    /**
     * Blend two colors additively
     */
    private int blendColors(int color1, int color2) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = min(255, r1 + r2);
        int g = min(255, g1 + g2);
        int b = min(255, b1 + b2);

        return (r << 16) | (g << 8) | b;
    }

    /**
     * Multiply colors with a lighting factor
     */
    private int multiplyColors(int objectColor, int lightColor, double factor) {
        int or = (objectColor >> 16) & 0xFF;
        int og = (objectColor >> 8) & 0xFF;
        int ob = objectColor & 0xFF;

        int lr = (lightColor >> 16) & 0xFF;
        int lg = (lightColor >> 8) & 0xFF;
        int lb = lightColor & 0xFF;

        // Calculate lit color components with a brightness factor
        double brightnessFactor = 0.7;
        int r = (int)((or * lr / 255.0) * factor * brightnessFactor);
        int g = (int)((og * lg / 255.0) * factor * brightnessFactor);
        int b = (int)((ob * lb / 255.0) * factor * brightnessFactor);

        // Clamp values
        r = min(255, max(0, r));
        g = min(255, max(0, g));
        b = min(255, max(0, b));

        return (r << 16) | (g << 8) | b;
    }
}