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
        //crossP.normalize();
        return crossP;
    }
    public double distanceFromOrigin() {
        return getNormal().dot(A);
    }

    public boolean rayTriangleIntersect(Vector3d origin,Vector3d dir) {
        Vector3d N = getNormal();

        double NrayDir = N.dot(dir);
        if (NrayDir<=0.001) { //Very small, so it's parallel.
            return false;
        }

        double d = distanceFromOrigin();

        double T=(getNormal().dot(origin)+d)/(NrayDir);

        if (T<0) {return false;} //Triangle is behind the ray.

        //System.out.println("Not behind.");

        Vector3d scaleMult = (Vector3d)dir.clone();
        scaleMult.scale(T);
        Vector3d P = (Vector3d)origin.clone();
        P.add(scaleMult);

        Vector3d C;

        Vector3d edge0 = (Vector3d)B.clone(); edge0.sub(A);
        Vector3d vp0 = (Vector3d)P.clone(); vp0.sub(A);
        C = new Vector3d(); C.cross(edge0,vp0);
        if (N.dot(C)<0) {return false;}

        Vector3d edge1 = (Vector3d)this.C.clone(); edge1.sub(B);
        Vector3d vp1 = (Vector3d)P.clone(); vp1.sub(B);
        C = new Vector3d(); C.cross(edge1,vp1);
        if (N.dot(C)<0) {return false;}

        Vector3d edge2 = (Vector3d)A.clone(); edge2.sub(this.C);
        Vector3d vp2 = (Vector3d)P.clone(); vp2.sub(this.C);
        C = new Vector3d(); C.cross(edge2,vp2);
        if (N.dot(C)<0) {return false;}

        return true;
    }
}
