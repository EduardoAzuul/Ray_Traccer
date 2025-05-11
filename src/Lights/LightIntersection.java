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

public class LightIntersection {
    List<Object3D> object3DList= new ArrayList<>();
    List<Light> lightList= new ArrayList<>();
    Object3D objectHit;
    Vector3D origin;
    Vector3D direction;
    Vector3D cameraOrigin;

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

    public Vector3D getCameraOrigin() {
        return cameraOrigin;
    }

    public void setCameraOrigin(Vector3D cameraOrigin) {
        this.cameraOrigin = cameraOrigin;
    }

    /***************************************************************/

    public LightIntersection(List<Object3D> object3DList, List<Light> lightList,
                             Object3D objectHit, Vector3D origin, Vector3D direction, Vector3D cameraOrigin)
    {
        setObject3DList(object3DList);
        setLightList(lightList);
        setObjectHit(objectHit);
        setOrigin(origin);
        setDirection(direction.normalize());
        setCameraOrigin(cameraOrigin);
    }

    /*************************************************************/

    public int lightsIntersectionMatte(Object3D object, Vector3D point) {
        // Variables to store color and lighting information
        int objectColor;
        int ambientColor;
        int finalColor;
        boolean useBlinnPhong = false;

        // Determine which color to use based on material
        if (object.getMaterial() instanceof BlingPhongMaterial) {
            useBlinnPhong = true;
            objectColor = object.getMaterial().getColor();
        } else {
            objectColor = object.getColorInt();
        }

        // Calculate ambient color using the determined object color
        ambientColor = calculateAmbientLight(objectColor, 0.15); // 15% ambient light
        finalColor = ambientColor;
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

            if (collision && !(light instanceof DirectionalLight)) { // Light is blocked, try next light
                continue;
            }

            // If we're using Blinn-Phong model and the object is a Triangle
            if (useBlinnPhong && object instanceof Triangle) {
                // Calculate the Blinn-Phong lighting for this light
                double lightFactor = blingLight((Triangle) object, point, light, cameraOrigin);

                // Convert the lighting factor to a color - passing objectColor, not object.getColorInt()
                int lightColor = applyBlinnPhongToColor(objectColor, light.getColorint(), lightFactor);
                finalColor = blendColors(finalColor, lightColor);
                anyLightHits = true;
                continue; // Skip the regular lighting calculations
            }

            //Handling of Directional Light
            if(light instanceof DirectionalLight){
                if (object instanceof Triangle) {
                    // Use the objectColor for LambertianLight calculations
                    Triangle triangle = (Triangle) object;
                    int lightColor = LambertianLightWithColor(triangle, point, (DirectionalLight) light, objectColor);
                    finalColor = blendColors(finalColor, lightColor);
                    anyLightHits = true;
                }
            }

            // Handle Spotlight
            else if (light instanceof SpotLight) {
                if (SpotLightHit((SpotLight) light, point)) {
                    if (object instanceof Triangle) {
                        // Use the objectColor for LambertianLight calculations
                        Triangle triangle = (Triangle) object;
                        int lightColor = LambertianLightWithColor(triangle, point, (SpotLight) light, objectColor);
                        finalColor = blendColors(finalColor, lightColor);
                        anyLightHits = true;
                    }
                }
            }

            // Handle point light
            else if (light instanceof PointLight) {
                if (object instanceof Triangle) {
                    // Use the objectColor for LambertianLight calculations
                    Triangle triangle = (Triangle) object;
                    int lightColor = LambertianLightWithColor(triangle, point, (PointLight) light, objectColor);
                    finalColor = blendColors(finalColor, lightColor);
                    anyLightHits = true;
                }
            }
        }

        // If no lights hit the point, use ambient light only
        return anyLightHits ? finalColor : ambientColor;
    }

    // New method that takes an explicit color parameter instead of using the object's color
    public int LambertianLightWithColor(Triangle object, Vector3D point, Light light, int color) {
        Vector3D normal = object.getNormal(point).normalize().multiplyByScalar(-1);

        double intensity = lightIntensity(object, point, light);
        Vector3D lightDir = lightDirection(object, point, light);

        // Calculate cosine of angle between normal and light
        double dotNL = normal.dot(lightDir);

        // If light is behind the surface, return black (no contribution)
        if (dotNL <= 0) {
            return 0x000000;
        }

        // Get colors - use the passed color instead of object.getColorInt()
        return addColorsWithDotNL(color, light, dotNL, intensity);
    }

    // Helper method to apply Blinn-Phong lighting factor to a color
    private int applyBlinnPhongToColor(int objectColor, int lightColor, double lightFactor) {
        int or = (objectColor >> 16) & 0xFF;
        int og = (objectColor >> 8) & 0xFF;
        int ob = objectColor & 0xFF;

        int lr = (lightColor >> 16) & 0xFF;
        int lg = (lightColor >> 8) & 0xFF;
        int lb = lightColor & 0xFF;

        // Calculate the final color with the lighting factor
        int r = (int)((or * lr / 255.0) * lightFactor);
        int g = (int)((og * lg / 255.0) * lightFactor);
        int b = (int)((ob * lb / 255.0) * lightFactor);

        // Clamp values
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return (r << 16) | (g << 8) | b;
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

    public double lightIntensity(Triangle object, Vector3D point, Light light){
        Vector3D lightDir;
        double intensity = light.getIntensity();
        if(light instanceof SpotLight) {

            Vector3D lightVector = light.getPosition().subtract(point);
            double distance = lightVector.length();

            double attenuation = 1.0 / (1.0 + 0.05 * distance * distance);
            intensity *= attenuation;

        } else if (light instanceof PointLight) {
            // For point lights, calculate direction and apply distance attenuation
            Vector3D lightVector = light.getPosition().subtract(point);
            double distance = lightVector.length();
            lightDir = lightVector.normalize();

            // Apply light attenuation for point lights (inverse square law)
            double attenuation = 1.0 / (1.0 + 0.05*distance * distance);
            intensity *= attenuation;
        } else {
            // Default case
            intensity = light.getIntensity();
        }
        return intensity;
    }

    public Vector3D lightDirection(Triangle object, Vector3D point, Light light) {
        Vector3D lightDir;
        double intensity = light.getIntensity();
        if(light instanceof SpotLight){
            // For directional lights, negate the light's direction vector
            lightDir = ((SpotLight) light).getDirection().normalize().multiplyByScalar(-1);
            Vector3D lightVector = light.getPosition().subtract(point);
            double distance = lightVector.length();
            lightDir = lightVector.normalize();

            // Apply light attenuation for point lights (inverse square law)
            double attenuation = 1.0 / (1.0 + 0.05*distance * distance);
            intensity *= attenuation;

        }else if (light instanceof DirectionalLight) {
            lightDir = ((DirectionalLight) light).getDirection();


        } else if (light instanceof PointLight) {
            // For point lights, calculate direction and apply distance attenuation
            Vector3D lightVector = light.getPosition().subtract(point);
            double distance = lightVector.length();
            lightDir = lightVector.normalize();

            // Apply light attenuation for point lights (inverse square law)
            double attenuation = 1.0 / (1.0 + 0.05*distance * distance);
            intensity *= attenuation;
        } else {
            // Default case
            lightDir = light.getPosition().subtract(point).normalize();
        }
        return lightDir;
    }

    public int LambertianLight(Triangle object, Vector3D point, Light light) {
        Vector3D normal = object.getNormal(point).normalize().multiplyByScalar(-1);

        double intensity = lightIntensity(object, point, light);
        Vector3D lightDir = lightDirection(object, point, light);

        // Calculate cosine of angle between normal and light
        double dotNL = normal.dot(lightDir);

        // If light is behind the surface, return black (no contribution)
        if (dotNL <= 0) {
            return 0x000000;
        }

        // Get colors
        return addColorsWithDotNL (object.getColorInt(), light, dotNL, intensity);
    }

    public double blingLight(Triangle object, Vector3D point, Light light, Vector3D cameraOrigin) {
        Vector3D normal = object.getNormal(point).normalize().multiplyByScalar(-1);
        BlingPhongMaterial material = (BlingPhongMaterial) object.getMaterial();

        // Ambient light
        double AMBIENT_LIGHT_INTENSITY = 0.5;
        double ambientComponent = material.getAmbient() * AMBIENT_LIGHT_INTENSITY;

        // Light direction
        Vector3D lightDir = lightDirection(object, point, light);
        double intensity = lightIntensity(object, point, light);

        // Diffuse light (Lambert)
        double diffuseComponent = material.getDiffuse() * intensity * Math.max(0, normal.dot(lightDir));

        // Specular light (Blinn-Phong)
        Vector3D viewDir = cameraOrigin.subtract(point).normalize();
        Vector3D halfway = lightDir.add(viewDir).normalize();
        double specAngle = Math.max(0, normal.dot(halfway));
        double specularComponent = material.getSpecular() * intensity * Math.pow(specAngle, material.getShininess());

        // Sum all components
        return ambientComponent + diffuseComponent + specularComponent;
    }


    private int addColorsWithDotNL(int objectColor, Light light, double dotNL, double intensity) {

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
        r = Math.min(255, max(0, r));
        g = Math.min(255, max(0, g));
        b = Math.min(255, max(0, b));

        return (r << 16) | (g << 8) | b;
    }
}