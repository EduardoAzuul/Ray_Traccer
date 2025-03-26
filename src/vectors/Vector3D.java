package vectors;

public class Vector3D {
    public Point p;

    /// ////////////////////////////////////////
    /// PASALO A INGLES!!!!!!!!!!!!!!!!!!!!!!
    // SIEMPRE NORMALIZA
    // Constructor
    public Vector3D(double x, double y, double z) {
        this.p = new Point(x, y, z);

    }

    // Producto punto
    public double punto(Vector3D v) {
        return this.p.x * v.p.x + this.p.y * v.p.y + this.p.z * v.p.z;
    }

    // Producto cruz
    public Vector3D cruz(Vector3D v) {
        double x = this.p.y * v.p.z - this.p.z * v.p.y;
        double y = this.p.z * v.p.x - this.p.x * v.p.z;
        double z = this.p.x * v.p.y - this.p.y * v.p.x;
        return new Vector3D(x, y, z);
    }

    // Magnitud
    public double magnitud() {
        return Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
    }

    // Normalizar
    public void normalize() {
        double mag = magnitud();
        if (mag != 0) {
            p.x /= mag;
            p.y /= mag;
            p.z /= mag;
        }
    }

    // Amplitud (ángulo respecto al eje X en radianes)
    public double amplitud() {
        return Math.acos(p.x / magnitud());
    }

    // Suma de vectores
    public Vector3D suma(Vector3D v) {
        return new Vector3D(this.p.x + v.p.x, this.p.y + v.p.y, this.p.z + v.p.z);
    }

    // Resta de vectores
    public Vector3D resta(Vector3D v) {
        return new Vector3D(this.p.x - v.p.x, this.p.y - v.p.y, this.p.z - v.p.z);
    }

    // Multiplicación escalar
    public Vector3D multiplicacionEscalar(double escalar) {
        return new Vector3D(this.p.x * escalar, this.p.y * escalar, this.p.z * escalar);
    }
}


