package Lights;

import Objects.ObjObject;
import Objects.Object3D;
import Objects.Sphere;
import Objects.Triangle;
import vectors.Intersection;
import vectors.Ray;
import vectors.Vector3D;

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

        for(Light light: lightList) {    //Check for every light
            Vector3D rayDirection =light.getPosition().subtract(point);
            Ray ray = new Ray(point, rayDirection);
            boolean colision = false;

            for (Object3D obj : getObject3DList()) {    //Checks it there was a colition before the light
                double dist = -1.0;

                if (obj instanceof Sphere sphere) {
                    dist = Intersection.sphere(ray, sphere, 0.1, Double.MAX_VALUE);
                }
                else if (obj instanceof Triangle triangle) {
                    dist = Intersection.triangle(ray, triangle, 0.1, Double.MAX_VALUE, 1e-4);
                }
                else if (obj instanceof ObjObject objObject) {
                    dist = Intersection.obj(ray, objObject,0.1, Double.MAX_VALUE, 1e-4);
                }

                // Simplified condition
                if (dist != -1.0) { //Breaks as soon as somthing colides
                    colision = true;
                    break;
                }
            }

            if (colision) { //jumps to the next light
                continue;
            }

            if (light instanceof DirectionLight){
                if(DirectionalLightHit((DirectionLight) light,point)){
                    if (object instanceof Triangle) {
                        //System.out.println("Lambert light");
                        return LambertianLight((Triangle) object, point, (DirectionLight) light);
                    }
                }
            }
        }
        return object.getColorInt();
    }

    public boolean DirectionalLightHit(DirectionLight directionLight, Vector3D point) {
        Vector3D direction = directionLight.getDirection().normalize();  // Ensure normalized direction
        double radius = directionLight.getRadius();

        // Calculate vector from light's effective origin to the point
        Vector3D vectorToPoint = point.subtract(origin);

        // Project vectorToPoint onto the light direction to get distance along light axis
        double distanceAlongLight = vectorToPoint.dot(direction);

        // Calculate the projection onto the plane perpendicular to light direction
        Vector3D projection = vectorToPoint.subtract(direction.multiplyByScalar(distanceAlongLight));

        // Calculate squared distance from center (faster than actual distance)
        double squaredDistance = projection.dot(projection);
        double squaredRadius = radius * radius;

        return squaredDistance <= squaredRadius;
    }


    public int LambertianLight(Triangle object, Vector3D point, DirectionLight directionLight) {
        Vector3D normal = object.getNormal().normalize();
        Vector3D lightDir = directionLight.getDirection().normalize().multiplyByScalar(-1);
        double intensity = directionLight.getIntensity();

        // Calculate cosine of angle between normal and light
        double dotNL = normal.dot(lightDir);

        // If light is behind the surface, return unlit color
        if (dotNL <= 0) {
            return object.getColorInt();
        }

        // Get colors
        int objectColor = object.getColorInt(); // 0xRRGGBB
        int lightColor = directionLight.getColorint(); // 0xRRGGBB

        // Extract components
        int or = (objectColor >> 16) & 0xFF;
        int og = (objectColor >> 8) & 0xFF;
        int ob = objectColor & 0xFF;

        int lr = (lightColor >> 16) & 0xFF;
        int lg = (lightColor >> 8) & 0xFF;
        int lb = lightColor & 0xFF;

        // Calculate lit color components with reduced brightness
        // Changes made here:
        // 1. Added a brightness factor (0.5) to reduce overall intensity
        // 2. Changed the multiplication order for better color blending
        double brightnessFactor = 0.5; // Adjust this between 0.1-1.0 for desired brightness
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