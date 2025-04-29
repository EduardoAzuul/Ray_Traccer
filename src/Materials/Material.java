package Materials;

public abstract class Material {
    int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    Material(int color) {
        setColor(color);
    }
}
