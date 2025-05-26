package Materials;

/**
 * Represents a material using the Blinn-Phong shading model with optional
 * reflectivity and refraction properties. Extends the basic Material class
 * to support more advanced lighting effects.
 *
 * @author José Eduardo Moreno Paredes
 */
public class BlingPhongMaterial extends Material {
    /** The index of refraction for transparent materials (1.0 = no refraction) */
    private double refraction;
    /** The reflectivity coefficient (0.0 to 1.0) */
    private double reflectivity;

    /**
     * Gets the index of refraction for this material
     * @return The current refraction index
     */
    public double getRefraction() {
        return refraction;
    }

    /**
     * Sets the index of refraction for this material
     * @param refraction The new refraction index (should be ≥ 1.0)
     */
    public void setRefraction(double refraction) {
        this.refraction = refraction;
    }

    /**
     * Gets the reflectivity coefficient of this material
     * @return The current reflectivity value between 0.0 and 1.0
     */
    public double getReflectivity() {
        return reflectivity;
    }

    /**
     * Sets the reflectivity coefficient of this material
     * @param reflectivity The new reflectivity value (will be clamped to 0.0-1.0)
     */
    public void setReflectivity(double reflectivity) {
        this.reflectivity = Math.max(0, Math.min(1, reflectivity));
    }

    /**
     * Creates a basic Blinn-Phong material without reflectivity or refraction
     * @param color The base color of the material (RGB packed as integer)
     * @param ambient The ambient lighting coefficient
     * @param diffuse The diffuse reflection coefficient
     * @param specular The specular reflection coefficient
     * @param shininess The shininess exponent for specular highlights
     */
    public BlingPhongMaterial(int color, double ambient, double diffuse,
                              double specular, double shininess) {
        super(color, ambient, diffuse, specular, shininess);
        setRefraction(0);
        setReflectivity(0);
    }

    /**
     * Creates an advanced Blinn-Phong material with reflectivity and refraction
     * @param color The base color of the material (RGB packed as integer)
     * @param ambient The ambient lighting coefficient
     * @param diffuse The diffuse reflection coefficient
     * @param specular The specular reflection coefficient
     * @param shininess The shininess exponent for specular highlights
     * @param transparency The transparency level (0.0 = opaque, 1.0 = fully transparent)
     * @param reflectivity The reflectivity coefficient (0.0 to 1.0)
     */
    public BlingPhongMaterial(int color, double ambient, double diffuse,
                              double specular, double shininess,
                              double transparency, double reflectivity) {
        super(color, ambient, diffuse, specular, shininess);
        setRefraction(Math.max(transparency, 0));
        setReflectivity(Math.max(0, Math.min(1, reflectivity)));
    }
}