package sig;
import javax.vecmath.Vector3d;

public class Triangle {
    Vector3d A,B,C;
    Triangle(Vector3d A,Vector3d B,Vector3d C) {
        this.A=A;
        this.B=B;
        this.C=C;
    }
    @Override
    public String toString() {
        return "Triangle [A=" + A + ", B=" + B + ", C=" + C + "]";
    }
}
