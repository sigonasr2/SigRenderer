package sig;
import javax.swing.JFrame;
import javax.vecmath.Vector3d;

public class SigRenderer {
    Vector3d vec;

    SigRenderer(JFrame f) {
        vec = new Vector3d();
        System.out.println(vec);   
    }
    public static void main(String[] args) {
        JFrame f = new JFrame("SigRenderer");
        new SigRenderer(f);
    }
}
