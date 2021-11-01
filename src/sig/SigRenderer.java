package sig;
import javax.swing.JFrame;
import javax.vecmath.Vector3d;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener; 

public class SigRenderer implements MouseListener,MouseMotionListener{
    Triangle tri;
    public final static int SCREEN_WIDTH=1280;
    public final static int SCREEN_HEIGHT=720;
    public final static long TIMEPERTICK = 16666667l;
    public static double DRAWTIME=0;

    public void runGameLoop() {

    }

    SigRenderer(JFrame f) {
        tri = new Triangle(new Vector3D(),new Vector3D(),new Vector3D());
        System.out.println(tri);   

        Panel p = new Panel();

        new Thread() {
            public void run(){
                while (true) {
                    long startTime = System.nanoTime();
                    runGameLoop();
                    p.repaint();
                    long endTime = System.nanoTime();
                    long diff = endTime-startTime;
                    if (diff>TIMEPERTICK) { //Took longer than 1/60th of a second. No sleep.
                        System.err.println("Frame Drawing took longer than "+TIMEPERTICK+"ns to calculate ("+diff+"ns total)!");
                    } else {
                        try {
                            long sleepTime = TIMEPERTICK - diff;
                            long millis = (sleepTime)/1000000;
                            int nanos = (int)(sleepTime-(((sleepTime)/1000000)*1000000));
                            //System.out.println("FRAME DRAWING: Sleeping for ("+millis+"ms,"+nanos+"ns) - "+(diff)+"ns");
                            DRAWTIME = (double)diff/1000000;
                            f.setTitle("Game Loop: "+DRAWTIME+"ms");
                            Thread.sleep(millis,nanos);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

        f.getContentPane().addMouseListener(this);
        f.getContentPane().addMouseMotionListener(this);
        f.add(p);
        f.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public static void main(String[] args) {
        JFrame f = new JFrame("SigRenderer");
        new SigRenderer(f);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
