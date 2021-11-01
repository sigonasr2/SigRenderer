package sig;
import javax.swing.JPanel;
import javax.vecmath.Vector3f;

import java.awt.Graphics;
import java.awt.Color;

public class Panel extends JPanel{
    long startTime = System.nanoTime();
    long endTime = System.nanoTime();

    public void repaint() {
        super.repaint();
        startTime = System.nanoTime();
    }
    public void paintComponent(Graphics g) {
        g.clearRect(0,0,SigRenderer.SCREEN_WIDTH,SigRenderer.SCREEN_HEIGHT);
        //Vector3f origin = new Vector3f(Math.cos(SigRenderer.rot)*10,0,10+Math.sin(SigRenderer.rot)*10);
        /*for (int x=0;x<SigRenderer.SCREEN_WIDTH/RESOLUTION;x++) {
            for (int y=0;y<SigRenderer.SCREEN_HEIGHT/RESOLUTION;y++) {
                Vector3f dir = new Vector3f((-SigRenderer.SCREEN_WIDTH/2d+x*RESOLUTION),(-SigRenderer.SCREEN_HEIGHT/2d+y*RESOLUTION),SigRenderer.SCREEN_WIDTH);
                if (SigRenderer.tri.rayTriangleIntersect(origin, dir)) {
                    g.setColor(Color.BLACK);
                    g.fillRect((int)(SigRenderer.SCREEN_WIDTH-x*RESOLUTION),(int)(SigRenderer.SCREEN_HEIGHT-y*RESOLUTION),(int)RESOLUTION,(int)RESOLUTION);
                    //System.out.println("Intersects at "+origin+"/"+dir);
                } else
                if (SigRenderer.tri2.rayTriangleIntersect(origin, dir)) {
                    g.setColor(Color.BLUE);
                    g.fillRect((int)(SigRenderer.SCREEN_WIDTH-x*RESOLUTION),(int)(SigRenderer.SCREEN_HEIGHT-y*RESOLUTION),(int)RESOLUTION,(int)RESOLUTION);
                    //System.out.println("Intersects at "+origin+"/"+dir);
                }
            }
        }*/
        for (Pixel p:SigRenderer.pixels) {
            if (!g.getColor().equals(p.col)) {
                g.setColor(p.col);
            }
            g.fillRect(p.x,p.y,(int)SigRenderer.RESOLUTION,(int)SigRenderer.RESOLUTION);
        }
        endTime=System.nanoTime();
        SigRenderer.DRAWLOOPTIME = (endTime-startTime)/1000000f;
    }
}
