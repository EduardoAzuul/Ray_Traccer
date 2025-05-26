package Materials;

/**
 * Base class representing a material with Phong lighting properties.
 * Defines the visual characteristics of a surface including its color,
 * and how it interacts with light (ambient, diffuse, and specular components).
 *
 * @author José Eduardo Moreno Paredes
 */
public class Material {
    /** Base color of the material (packed as 0xRRGGBB) */
    protected int color;
    /** Ambient reflection intensity (range 0.0 to 1.0) */
    protected double ambient;
    /** Diffuse reflection intensity (range 0.0 to 1.0) */
    protected double diffuse;
    /** Specular reflection intensity (range 0.0 to 1.0) */
    protected double specular;
    /** Shininess exponent for specular highlights (Phong model, ≥1) */
    protected double shininess;

    /**
     * Creates a new material with full lighting properties.
     * @param color The base color (packed as 0xRRGGBB)
     * @param ambient Ambient reflection coefficient (0.0-1.0)
     * @param diffuse Diffuse reflection coefficient (0.0-1.0)
     * @param specular Specular reflection coefficient (0.0-1.0)
     * @param shininess Specular exponent (≥1, higher values = tighter highlights)
     */
    public Material(int color, double ambient, double diffuse, double specular, double shininess) {
        this.color = color;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    /**
     * Creates a new material with default lighting properties
     * (ambient=0.1, diffuse=0.7, specular=0.2, shininess=10).
     * @param color The base color (packed as 0xRRGGBB)
     */
    public Material(int color) {
        this(color, 0.1, 0.7, 0.2, 10);
    }

    /**
     * Gets the material's base color
     * @return The color packed as 0xRRGGBB
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets the ambient reflection coefficient
     * @return Ambient intensity (0.0-1.0)
     */
    public double getAmbient() {
        return ambient;
    }

    /**
     * Gets the diffuse reflection coefficient
     * @return Diffuse intensity (0.0-1.0)
     */
    public double getDiffuse() {
        return diffuse;
    }

    /**
     * Gets the specular reflection coefficient
     * @return Specular intensity (0.0-1.0)
     */
    public double getSpecular() {
        return specular;
    }

    /**
     * Gets the shininess exponent
     * @return Specular exponent (≥1)
     */
    public double getShininess() {
        return shininess;
    }

    /**
     * Sets the material's base color
     * @param color The new color (packed as 0xRRGGBB)
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Sets the ambient reflection coefficient
     * @param ambient New ambient intensity (will be clamped to 0.0-1.0)
     */
    public void setAmbient(double ambient) {
        this.ambient = Math.max(0, Math.min(1, ambient));
    }

    /**
     * Sets the diffuse reflection coefficient
     * @param diffuse New diffuse intensity (will be clamped to 0.0-1.0)
     */
    public void setDiffuse(double diffuse) {
        this.diffuse = Math.max(0, Math.min(1, diffuse));
    }

    /**
     * Sets the specular reflection coefficient
     * @param specular New specular intensity (will be clamped to 0.0-1.0)
     */
    public void setSpecular(double specular) {
        this.specular = Math.max(0, Math.min(1, specular));
    }

    /**
     * Sets the shininess exponent
     * @param shininess New specular exponent (will be clamped to ≥1)
     */
    public void setShininess(double shininess) {
        this.shininess = Math.max(1, shininess);
    }
}