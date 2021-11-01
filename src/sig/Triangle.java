package sig;

public class Triangle {
    Vector3D A,B,C;
    Triangle(Vector3D A,Vector3D B,Vector3D C) {
        this.A=A;
        this.B=B;
        this.C=C;
    }
    @Override
    public String toString() {
        return "Triangle [A=" + A + ", B=" + B + ", C=" + C + "]";
    }
}
