package sig;
import javax.swing.JPanel;
import javax.vecmath.Vector3d;

import java.awt.Graphics;
import java.awt.Color;

public class Panel extends JPanel{
    public void paintComponent(Graphics g) {
        Vector3d origin = new Vector3d(0,0,10);
        for (int x=0;x<SigRenderer.SCREEN_WIDTH;x++) {
            for (int y=0;y<SigRenderer.SCREEN_HEIGHT;y++) {
                Vector3d dir = new Vector3d((-SigRenderer.SCREEN_WIDTH/2d+x),(-SigRenderer.SCREEN_HEIGHT/2d+y),SigRenderer.SCREEN_WIDTH);
                if (SigRenderer.tri.rayTriangleIntersect(origin, dir)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(SigRenderer.SCREEN_WIDTH-x,SigRenderer.SCREEN_HEIGHT-y,1,1);
                    //System.out.println("Intersects at "+origin+"/"+dir);
                }
                if (SigRenderer.tri2.rayTriangleIntersect(origin, dir)) {
                    g.setColor(Color.BLUE);
                    g.fillRect(SigRenderer.SCREEN_WIDTH-x,SigRenderer.SCREEN_HEIGHT-y,1,1);
                    //System.out.println("Intersects at "+origin+"/"+dir);
                }
            }
        }
    }
}
