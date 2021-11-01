package sig;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;

public class SigRenderer implements KeyListener,MouseListener,MouseMotionListener{
    public final static int SCREEN_WIDTH=1280;
    public final static int SCREEN_HEIGHT=720;
    public final static int TEXTURE_WIDTH=64;
    public final static int TEXTURE_HEIGHT=64;
    public final static long TIMEPERTICK = 16666667l;
    public static float DRAWTIME=0;
    public static float DRAWLOOPTIME=0;

    public static double x = 22;
    public static double y = 11.5;
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
        {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,7,7,7,7,7,7,7,7},
        {4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
        {4,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
        {4,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
        {4,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
        {4,0,4,0,0,0,0,5,5,5,5,5,5,5,5,5,7,7,0,7,7,7,7,7},
        {4,0,5,0,0,0,0,5,0,5,0,5,0,5,0,5,7,0,0,0,7,7,7,1},
        {4,0,6,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,0,0,0,8},
        {4,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,1},
        {4,0,8,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,0,0,0,8},
        {4,0,0,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,7,7,7,1},
        {4,0,0,0,0,0,0,5,5,5,5,0,5,5,5,5,7,7,7,7,7,7,7,1},
        {6,6,6,6,6,6,6,6,6,6,6,0,6,6,6,6,6,6,6,6,6,6,6,6},
        {8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
        {6,6,6,6,6,6,0,6,6,6,6,0,6,6,6,6,6,6,6,6,6,6,6,6},
        {4,4,4,4,4,4,0,4,4,4,6,0,6,2,2,2,2,2,2,2,3,3,3,3},
        {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,0,0,0,2},
        {4,0,0,0,0,0,0,0,0,0,0,0,6,2,0,0,5,0,0,2,0,0,0,2},
        {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,2,0,2,2},
        {4,0,6,0,6,0,0,0,0,4,6,0,0,0,0,0,5,0,0,0,0,0,0,2},
        {4,0,0,5,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,2,0,2,2},
        {4,0,6,0,6,0,0,0,0,4,6,0,6,2,0,0,5,0,0,2,0,0,0,2},
        {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,0,0,0,2},
        {4,4,4,4,4,4,4,4,4,4,1,1,1,2,2,2,2,2,2,3,3,3,3,3}
      };

    public static int[][] textures = new int[8][TEXTURE_WIDTH*TEXTURE_HEIGHT];
    public static BufferedImage buffer = new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_ARGB);
    public static BufferedImage buffer2 = new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_ARGB);

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

    BufferedImage toCompatibleImage(BufferedImage image)
{
    // obtain the current system graphical settings
    GraphicsConfiguration gfxConfig = GraphicsEnvironment.
        getLocalGraphicsEnvironment().getDefaultScreenDevice().
        getDefaultConfiguration();

    /*
     * if image is already compatible and optimized for current system 
     * settings, simply return it
     */
    if (image.getColorModel().equals(gfxConfig.getColorModel()))
        return image;

    // image is not optimized, so create a new image that is
    BufferedImage newImage = gfxConfig.createCompatibleImage(
            image.getWidth(), image.getHeight(), image.getTransparency());

    // get the graphics context of the new image to draw the old image on
    Graphics2D g2d = newImage.createGraphics();

    // actually draw the image and dispose of context no longer needed
    g2d.drawImage(image, 0, 0, null);
    g2d.dispose();

    // return the new optimized image
    return newImage; 
}

    SigRenderer(JFrame f) {

        for (int x=0;x<TEXTURE_WIDTH;x++) {
            for (int y=0;y<TEXTURE_HEIGHT;y++) {
                int xorcolor = (x*256/TEXTURE_WIDTH)^(y*256/TEXTURE_HEIGHT);
                int ycolor=y*256/TEXTURE_HEIGHT;
                int xycolor=y*128/TEXTURE_HEIGHT+x*128/TEXTURE_WIDTH;
                textures[0][TEXTURE_WIDTH*y+x]=65536*254*((x!=y&&x!=TEXTURE_WIDTH-y)?1:0);
                textures[1][TEXTURE_WIDTH*y+x]=xycolor+256*xycolor+65536*xycolor;
                textures[2][TEXTURE_WIDTH*y+x]=256*xycolor+65536*xycolor;
                textures[3][TEXTURE_WIDTH*y+x]=xorcolor+256*xorcolor+65536*xorcolor;
                textures[4][TEXTURE_WIDTH*y+x]=256*xorcolor;
                textures[5][TEXTURE_WIDTH*y+x]=65536*192*((x%16>=1&&y%16>=1)?1:0);
                textures[6][TEXTURE_WIDTH*y+x]=65536*ycolor;
                textures[7][TEXTURE_WIDTH*y+x]=128+256*128+65536*128;
            }
        }

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

        p.init();
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
