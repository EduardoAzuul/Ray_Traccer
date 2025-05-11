package ResuableElements;

import Materials.BlingPhongMaterial;
import vectors.Vector3D;

public class TipicalMaterials {

    public static final BlingPhongMaterial GOLD = new BlingPhongMaterial(
            new Vector3D(255, 215, 0).getRGB(),  // Gold color
            0.24725,  // ambient
            0.75164,  // diffuse
            0.628281, // specular
            32        // shininess
    );

    public static final BlingPhongMaterial SILVER = new BlingPhongMaterial(
            new Vector3D(192, 192, 192).getRGB(), // Silver color
            0.19225,
            0.50754,
            0.508273,
            32
    );

    public static final BlingPhongMaterial BRONZE = new BlingPhongMaterial(
            new Vector3D(205, 127, 50).getRGB(), // Bronze color
            0.2125,
            0.714,
            0.393548,
            25.6
    );

    public static final BlingPhongMaterial PLASTIC_RED = new BlingPhongMaterial(
            new Vector3D(255,0,0).getRGB(),
            0.3,
            0.6,
            0.8,
            10
    );

    public static final BlingPhongMaterial RUBBER_BLACK = new BlingPhongMaterial(
            new Vector3D(10,10,10).getRGB(),
            0.02,
            0.01,
            0.4,
            10
    );

    public static final BlingPhongMaterial EMERALD = new BlingPhongMaterial(
            new Vector3D(80, 200, 120).getRGB(), // greenish emerald tone
            0.0215,
            0.1745,
            0.393548,
            76.8
    );

    public static final BlingPhongMaterial MARBLE = new BlingPhongMaterial(
            new Vector3D(245, 245, 245).getRGB(), // Light grayish-white for marble
            0.25,   // ambient
            0.4,    // diffuse
            0.3,    // specular
            10      // shininess (marble has soft highlights)
    );

    public static final BlingPhongMaterial SHINY_METAL = new BlingPhongMaterial(
            new Vector3D(220, 220, 220).getRGB(), // Slightly brighter silver
            0.07,
            0.2,
            0.9,
            110
    );

}
