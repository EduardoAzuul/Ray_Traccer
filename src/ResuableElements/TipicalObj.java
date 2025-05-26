package ResuableElements;

import Materials.BlingPhongMaterial;
import Materials.Material;
import Objects.ObjObject;
import vectors.Vector3D;

import java.util.ArrayList;
import java.util.List;

import static ResuableElements.TipicalMaterials.*;

public class TipicalObj {

    public static ObjObject createTeapot(Material material) {
        String path = "ObjFiles/Objs/SmallTeapot.obj";
        Vector3D color = new Vector3D(255, 255, 255);
        Vector3D rotation = new Vector3D(0, 0, 0);
        Vector3D position = new Vector3D(0, 0, -40);
        Vector3D scale = new Vector3D(8.0, 8.0, 8.0);
        //Vector3D color, Vector3D rotation, Vector3D position, Vector3D scale, String objPath
        ObjObject obj = new ObjObject(material, color, rotation, position, scale, path);
        return obj;
    }

    public static ObjObject createAngel(Material material) {
        String path = "ObjFiles/Objs/Angle.obj";
        Vector3D color = new Vector3D(255, 255, 255);
        Vector3D rotation = new Vector3D(0, 0, 0);
        Vector3D position = new Vector3D(0, -20, -50);
        Vector3D scale = new Vector3D(8.0, 8.0, 8.0);

        ObjObject obj = new ObjObject(material, color, rotation, position, scale, path);
        return obj;
    }

    public static ObjObject createFloor(Material material) {
        String path = "ObjFiles/Objs/square.obj";
        Vector3D color = new Vector3D(255, 255, 255);
        Vector3D rotation = new Vector3D(-90, 0, 0);
        Vector3D position = new Vector3D(0, -20, -50);
        Vector3D scale = new Vector3D(80.0, 80.0, 80.0);

        ObjObject obj = new ObjObject(material, color, rotation, position, scale, path);
        return obj;
    }

    public static List<ObjObject> createAngelGrid() {
        List<ObjObject> angels = new ArrayList<>();

        double spacingX = 25.0;
        double spacingZ = 35.0;
        double startX = 0;
        double startZ = -50.0;

        Vector3D rotation = new Vector3D(0, 0, 0);
        Vector3D scale = new Vector3D(8.0, 8.0, 8.0);
        Vector3D color = new Vector3D(255, 255, 255); // base color (real lighting comes from material)

        String path = "ObjFiles/Objs/Angle.obj";

        BlingPhongMaterial[] materials = new BlingPhongMaterial[] {

                TipicalMaterials.GOLD,
                SHINY_METAL,
                RUBY,
                MARBLE,
                TipicalMaterials.BRONZE,
                SILVER,

        };

        for (int i = 0; i < 6; i++) {
            int row = i % 3;
            int col = i / 3;

            double posX=0;
            if(col == 0) {
                posX = startX + spacingX;
            }
            else {
                posX = startX - spacingX;
            }
            double posZ = startZ - row * spacingZ;
            Vector3D position = new Vector3D(posX, -20, posZ);

            ObjObject angel = new ObjObject(materials[i], color, rotation, position, scale, path);
            angels.add(angel);
        }

        return angels;
    }

    public static List<ObjObject> createTeapotGrid() {
        List<ObjObject> teapots = new ArrayList<>();

        double spacingX = 25.0;
        double spacingZ = 35.0;
        double startX = 0;
        double startZ = -50.0;

        Vector3D rotation = new Vector3D(0, 0, 0);
        Vector3D scale = new Vector3D(8.0, 8.0, 8.0);
        Vector3D color = new Vector3D(255, 255, 255); // base color (real lighting comes from material)

        String path = "ObjFiles/Objs/SmallTeapot.obj";

        BlingPhongMaterial[] materials = new BlingPhongMaterial[] {
                TipicalMaterials.GOLD,
                TipicalMaterials.SILVER,
                TipicalMaterials.BRONZE,
                TipicalMaterials.EMERALD,
                TipicalMaterials.MARBLE,
                TipicalMaterials.SHINY_METAL
        };

        for (int i = 0; i < 6; i++) {
            int row = i % 3;
            int col = i / 3;

            double posX=0;
            if(col == 0) {
                posX = startX + spacingX;
            }
            else {
                posX = startX - spacingX;
            }
            double posZ = startZ - row * spacingZ;
            Vector3D position = new Vector3D(posX, -20, posZ);

            ObjObject angel = new ObjObject(materials[i], color, rotation, position, scale, path);
            teapots.add(angel);
        }

        return teapots;
    }

    public static List<ObjObject> createCornellBox() {
        List<ObjObject> walls = new ArrayList<>();

        Vector3D scale = new Vector3D(80.0, 80.0, 80.0);
        Vector3D color = new Vector3D(255, 255, 255);
        String path = "ObjFiles/Objs/square.obj";

        // Pared izquierda (roja)
        walls.add(new ObjObject(
                TipicalMaterials.PLASTIC_RED,
                color,
                new Vector3D(0, 90, 0),
                new Vector3D(-40, 0, -50),
                scale,
                path
        ));

        // Pared derecha (verde)
        walls.add(new ObjObject(
                TipicalMaterials.PLASTIC_GREEN,
                color,
                new Vector3D(0, -90, 0),
                new Vector3D(40, 0, -50),
                scale,
                path
        ));

        // Techo (blanco)
        walls.add(new ObjObject(
                TipicalMaterials.MARBLE,
                color,
                new Vector3D(90, 0, 0),
                new Vector3D(0, 40, -50),
                scale,
                path
        ));

        // Piso (blanco)
        walls.add(new ObjObject(
                TipicalMaterials.MARBLE,
                color,
                new Vector3D(-90, 0, 0),
                new Vector3D(0, -40, -50),
                scale,
                path
        ));

        // Fondo (blanco)
        walls.add(new ObjObject(
                TipicalMaterials.MARBLE,
                color,
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, -90),
                scale,
                path
        ));

        return walls;
    }

}
