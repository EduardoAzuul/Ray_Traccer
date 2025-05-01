package Materials;

public class Material {
    protected int color;              // Color base del material (RGB en 0xRRGGBB)
    protected double ambient;         // Intensidad ambiental (0 a 1)
    protected double diffuse;         // Intensidad difusa (0 a 1)
    protected double specular;        // Intensidad especular (0 a 1)
    protected double shininess;       // Tamaño del highlight especular (Phongs, >=1)

    public Material(int color, double ambient, double diffuse, double specular, double shininess) {
        this.color = color;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    public Material(int color) {
        setColor(color);
    }

    // Getters y setters...

    public int getColor() {
        return color;
    }

    public double getAmbient() {
        return ambient;
    }

    public double getDiffuse() {
        return diffuse;
    }

    public double getSpecular() {
        return specular;
    }

    public double getShininess() {
        return shininess;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setAmbient(double ambient) {
        this.ambient = ambient;
    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(double specular) {
        this.specular = specular;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }
}
