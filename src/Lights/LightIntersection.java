package Lights;

import Objects.ObjObject;
import Objects.Object3D;
import Objects.Sphere;
import Objects.Triangle;
import vectors.Intersection;
import vectors.Ray;
import vectors.Vector3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LightIntersection {
    List<Object3D> object3DList= new ArrayList<>();
    List<Light> lightList= new ArrayList<>();
    Object3D objectHit;
    Vector3D origin;
    Vector3D direction;

    public List<Object3D> getObject3DList() {
        return object3DList;
    }

    public void setObject3DList(List<Object3D> object3DList) {
        this.object3DList = object3DList;
    }

    public List<Light> getLightList() {
        return lightList;
    }

    public void setLightList(List<Light> lightList) {
        this.lightList = lightList;
    }

    public Object3D getObjectHit() {
        return objectHit;
    }

    public void setObjectHit(Object3D objectHit) {
        this.objectHit = objectHit;
    }

    public Vector3D getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    /***************************************************************/

    public LightIntersection(List<Object3D> object3DList, List<Light> lightList,
                             Object3D objectHit, Vector3D origin, Vector3D direction)
    {
        setObject3DList(object3DList);
        setLightList(lightList);
        setObjectHit(objectHit);
        setOrigin(origin);
        setDirection(direction.normalize());

    }

    /*************************************************************/

    public int lightsIntersection(Object3D object, Vector3D point) {
        // Calculate the ambient color (could be made a parameter of the method)
        int ambientColor = calculateAmbientLight(object.getColorInt(), 0.15); // 15% ambient light
        int finalColor = ambientColor;
        boolean anyLightHits = false;

        for(Light light: lightList) {    // Check for every light
            // Calculate vector from intersection point to light
            Vector3D lightVector = light.getPosition().subtract(point);
            double lightDistance = lightVector.length();
            Vector3D rayDirection = lightVector.normalize();
            Ray ray = new Ray(point, rayDirection);

            boolean collision = false;
            double shadowBias = 0.0001; // Reduced bias to prevent self-shadowing

            // Check if there's any object blocking the light
            for (Object3D obj : getObject3DList()) {
                // Skip the object itself to prevent self-shadowing
                if (obj == object) continue;

                double dist = -1.0;

                if (obj instanceof Sphere sphere) {
                    dist = Intersection.sphere(ray, sphere, shadowBias, lightDistance);
                }
                else if (obj instanceof Triangle triangle) {
                    dist = Intersection.triangle(ray, triangle, shadowBias, lightDistance, 1e-4);
                }
                else if (obj instanceof ObjObject objObject) {
                    dist = Intersection.obj(ray, objObject, shadowBias, lightDistance, 1e-4);
                }

                // If something collides between point and light
                if (dist > 0.0 && dist < lightDistance) {
                    collision = true;
                    break;
                }
            }

            if (collision) { // Light is blocked, try next light
                continue;
            }

            // Handle directional light
            if (light instanceof SpotLight) {
                if (SpotLightHit((SpotLight) light, point)) {
                    if (object instanceof Triangle) {
                        int lightColor = LambertianLight((Triangle) object, point, (SpotLight) light);
                        finalColor = blendColors(finalColor, lightColor);
                        anyLightHits = true;
                    }
                }
            }

            // Handle point light
            else if (light instanceof PointLight) {
                if (object instanceof Triangle) {
                    int lightColor = LambertianLight((Triangle) object, point, (PointLight) light);
                    finalColor = blendColors(finalColor, lightColor);
                    anyLightHits = true;
                }
            }
        }

        // If no lights hit the point, use ambient light only
        return anyLightHits ? finalColor : ambientColor;
    }

    // Helper method to calculate ambient light
    private int calculateAmbientLight(int objectColor, double ambientIntensity) {
        int or = (objectColor >> 16) & 0xFF;
        int og = (objectColor >> 8) & 0xFF;
        int ob = objectColor & 0xFF;

        int r = (int)(or * ambientIntensity);
        int g = (int)(og * ambientIntensity);
        int b = (int)(ob * ambientIntensity);

        return (r << 16) | (g << 8) | b;
    }

    // Helper method to blend colors (additive blending)
    private int blendColors(int color1, int color2) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = Math.min(255, r1 + r2);
        int g = Math.min(255, g1 + g2);
        int b = Math.min(255, b1 + b2);

        return (r << 16) | (g << 8) | b;
    }

    public boolean SpotLightHit(SpotLight spotLight, Vector3D point) {
        Vector3D lightDir = spotLight.getDirection().normalize();  // Dirección del spotlight (hacia donde apunta)
        Vector3D lightPos = spotLight.getPosition();               // Posición del spotlight
        double angle = spotLight.getAngle();                       // Ángulo de apertura (en grados)
        double initialRadius = spotLight.getRadius();              // Radio base del haz

        Vector3D lightToPoint = point.subtract(lightPos);
        double distance = lightToPoint.magnitude();
        Vector3D toPointDir = lightToPoint.normalize();
        double cosLimit = Math.cos(Math.toRadians(angle / 2.0));
        double cosTheta = lightDir.dot(toPointDir);

        if (cosTheta > cosLimit) {  // Cambiado de "cosTheta < cosLimit" a "cosTheta > cosLimit"
            // Calculamos el radio a la distancia donde está el punto
            double projectionLength = cosTheta * distance;
            double perpendicularDistance = Math.sqrt(distance * distance - projectionLength * projectionLength);
            double radiusAtDistance = initialRadius + projectionLength * Math.tan(Math.toRadians(angle / 2.0));

            return perpendicularDistance <= radiusAtDistance;
        }

        return false;
    }




    public int LambertianLight(Triangle object, Vector3D point, Light light) {
        Vector3D normal = object.getNormal().normalize().multiplyByScalar(-1);

        // Calculate light direction vector from point to light source
        Vector3D lightDir;
        double intensity = light.getIntensity();

        if(light instanceof SpotLight){
            // For directional lights, negate the light's direction vector
            lightDir = ((SpotLight) light).getDirection().normalize().multiplyByScalar(-1);
        } else if (light instanceof PointLight) {
            // For point lights, calculate direction and apply distance attenuation
            Vector3D lightVector = light.getPosition().subtract(point);
            double distance = lightVector.length();
            lightDir = lightVector.normalize();

            // Apply light attenuation for point lights (inverse square law)
            double attenuation = 1.0 / (1.0 + 0.09 * distance + 0.032 * distance * distance);
            intensity *= attenuation;
        } else {
            // Default case
            lightDir = light.getPosition().subtract(point).normalize();
        }

        // Calculate cosine of angle between normal and light
        double dotNL = normal.dot(lightDir);

        // If light is behind the surface, return black (no contribution)
        if (dotNL <= 0) {
            return 0x000000;
        }

        // Get colors
        int objectColor = object.getColorInt(); // 0xRRGGBB
        int lightColor = light.getColorint(); // 0xRRGGBB

        // Extract components
        int or = (objectColor >> 16) & 0xFF;
        int og = (objectColor >> 8) & 0xFF;
        int ob = objectColor & 0xFF;

        int lr = (lightColor >> 16) & 0xFF;
        int lg = (lightColor >> 8) & 0xFF;
        int lb = lightColor & 0xFF;

        // Calculate lit color components
        double brightnessFactor = 0.7; // Increased brightness a bit
        int r = (int)((or * lr / 255.0) * intensity * dotNL * brightnessFactor);
        int g = (int)((og * lg / 255.0) * intensity * dotNL * brightnessFactor);
        int b = (int)((ob * lb / 255.0) * intensity * dotNL * brightnessFactor);

        // Clamp values
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return (r << 16) | (g << 8) | b;
    }
}