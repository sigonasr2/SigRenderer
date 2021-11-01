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

    public Vector3d getNormal() {
        Vector3d AB = (Vector3d)A.clone();
        AB.sub(B);
        Vector3d BC = (Vector3d)B.clone();
        BC.sub(C);
        Vector3d crossP = new Vector3d();
        crossP.cross(AB,BC);
        crossP.normalize();
        return crossP;
    }
}
