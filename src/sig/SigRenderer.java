package sig;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;

public class SigRenderer implements KeyListener,MouseListener,MouseMotionListener{
    public final static int SCREEN_WIDTH=1280;
    public final static int SCREEN_HEIGHT=720;
    public final static long TIMEPERTICK = 16666667l;
    public static float DRAWTIME=0;
    public static float DRAWLOOPTIME=0;

    public static double x = 22;
    public static double y = 12;
    public static double dirX = -1;
    public static double dirY = 0;
    public static double planeX = 0;
    public static double planeY = 0.66;
    public static double moveSpeed = 0.1;
    public static double rotSpeed = -Math.PI/64;

    boolean UP_KEY=false;
    boolean DOWN_KEY=false;
    boolean RIGHT_KEY=false;
    boolean LEFT_KEY=false;

    public static int[][] worldMap = new int[][]
    {
      {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,2,2,2,2,2,0,0,0,0,3,0,3,0,3,0,0,0,1},
      {1,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,2,0,0,0,2,0,0,0,0,3,0,0,0,3,0,0,0,1},
      {1,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,2,2,0,2,2,0,0,0,0,3,0,3,0,3,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,4,0,4,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,4,0,0,0,0,5,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,4,0,4,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,4,0,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
      {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    public void runGameLoop() {
        if (UP_KEY) {
            if (worldMap[(int)(x+dirX*moveSpeed)][(int)y]==0) {
                x+=dirX*moveSpeed;
            }
            if (worldMap[(int)x][(int)(y+dirY*moveSpeed)]==0) {
                y+=dirY*moveSpeed;
            }
        }
        if (DOWN_KEY) {
            if (worldMap[(int)(x-dirX*moveSpeed)][(int)y]==0) {
                x-=dirX*moveSpeed;
            }
            if (worldMap[(int)x][(int)(y-dirY*moveSpeed)]==0) {
                y-=dirY*moveSpeed;
            }
        }
        if (LEFT_KEY) {
            double oldDirX=dirX;
            dirX=dirX*Math.cos(-rotSpeed)-dirY*Math.sin(-rotSpeed);
            dirY=oldDirX*Math.sin(-rotSpeed)+dirY*Math.cos(-rotSpeed);
            double oldPlaneX=planeX;
            planeX=planeX*Math.cos(-rotSpeed)-planeY*Math.sin(-rotSpeed);
            planeY=oldPlaneX*Math.sin(-rotSpeed)+planeY*Math.cos(-rotSpeed);
        }
        if (RIGHT_KEY) {
            double oldDirX=dirX;
            dirX=dirX*Math.cos(rotSpeed)-dirY*Math.sin(rotSpeed);
            dirY=oldDirX*Math.sin(rotSpeed)+dirY*Math.cos(rotSpeed);
            double oldPlaneX=planeX;
            planeX=planeX*Math.cos(rotSpeed)-planeY*Math.sin(rotSpeed);
            planeY=oldPlaneX*Math.sin(rotSpeed)+planeY*Math.cos(rotSpeed);
        }
    }

    SigRenderer(JFrame f) {

        Panel p = new Panel();

        new Thread() {
            public void run(){
                while (true) {
                    long startTime = System.nanoTime();
                    runGameLoop();
                    p.repaint();
                    Toolkit.getDefaultToolkit().sync();
                    long endTime = System.nanoTime();
                    long diff = endTime-startTime;
                    try {
                        long sleepTime = TIMEPERTICK - diff;
                        long millis = (sleepTime)/1000000;
                        int nanos = (int)(sleepTime-(((sleepTime)/1000000)*1000000));
                        //System.out.println("FRAME DRAWING: Sleeping for ("+millis+"ms,"+nanos+"ns) - "+(diff)+"ns");
                        DRAWTIME = (float)diff/1000000;
                        f.setTitle("Game Loop: "+DRAWTIME+"ms, Draw Loop: "+DRAWLOOPTIME+"ms");
                        if (sleepTime>0) {
                            Thread.sleep(millis,nanos);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        f.getContentPane().addMouseListener(this);
        f.getContentPane().addMouseMotionListener(this);
        f.addKeyListener(this);
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

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:{UP_KEY=true;}break;
            case KeyEvent.VK_DOWN:{DOWN_KEY=true;}break;
            case KeyEvent.VK_LEFT:{LEFT_KEY=true;}break;
            case KeyEvent.VK_RIGHT:{RIGHT_KEY=true;}break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:{UP_KEY=false;}break;
            case KeyEvent.VK_DOWN:{DOWN_KEY=false;}break;
            case KeyEvent.VK_LEFT:{LEFT_KEY=false;}break;
            case KeyEvent.VK_RIGHT:{RIGHT_KEY=false;}break;
        }
    }
}
