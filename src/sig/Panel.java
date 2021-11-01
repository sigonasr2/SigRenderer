package sig;
import javax.swing.JPanel;
import javax.vecmath.Vector3f;

import java.awt.Graphics;
import java.awt.Color;

import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.ColorModel;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

public class Panel extends JPanel implements Runnable {
    long startTime = System.nanoTime();
    long endTime = System.nanoTime();
    public int pixel[];
    public int width=SigRenderer.SCREEN_WIDTH;
    public int height=SigRenderer.SCREEN_HEIGHT;
    private Image imageBuffer;   
    private MemoryImageSource mImageProducer;   
    private ColorModel cm;    
    private Thread thread;

    public Panel() {
        super(true);
        thread = new Thread(this, "MyPanel Thread");
    }

    /**
     * Get Best Color model available for current screen.
     * @return color model
     */
    protected static ColorModel getCompatibleColorModel(){        
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();        
        return gfx_config.getColorModel();
    }

    /**
     * Call it after been visible and after resizes.
     */
    public void init(){        
        cm = getCompatibleColorModel();
        width = getWidth();
        height = getHeight();
        int screenSize = width * height;
        if(pixel == null || pixel.length < screenSize){
            pixel = new int[screenSize];
        }        
        mImageProducer =  new MemoryImageSource(width, height, cm, pixel,0, width);
        mImageProducer.setAnimated(true);
        mImageProducer.setFullBufferUpdates(true);  
        imageBuffer = Toolkit.getDefaultToolkit().createImage(mImageProducer);        
        if(thread.isInterrupted() || !thread.isAlive()){
            thread.start();
        }
    }
    /**
    * Do your draws in here !!
    * pixel is your canvas!
    */
    public /* abstract */ void render(){
        int[] p = pixel; // this avoid crash when resizing

        final int h=SigRenderer.SCREEN_HEIGHT;
        if(p.length != width * height) return;        
            for(int x=0; x < width; x++){
                double cameraX = 2*x/(double)SigRenderer.SCREEN_WIDTH-1;
                double rayDirX = SigRenderer.dirX+SigRenderer.planeX*cameraX;
                double rayDirY = SigRenderer.dirY+SigRenderer.planeY*cameraX;

                int mapX = (int)SigRenderer.x;
                int mapY = (int)SigRenderer.y;

                double sideDistX;
                double sideDistY;

                double deltaDistX=Math.abs(1/rayDirX);
                double deltaDistY=Math.abs(1/rayDirY);
                double perpWallDist=0;

                int stepX;
                int stepY;

                int hit=0;
                int side=0;

                if (rayDirX<0) {
                    stepX=-1;
                    sideDistX=(SigRenderer.x-mapX)*deltaDistX;
                } else {
                    stepX=1;
                    sideDistX=(mapX+1d-SigRenderer.x)*deltaDistX;
                }
                if (rayDirY<0) {
                    stepY=-1;
                    sideDistY=(SigRenderer.y-mapY)*deltaDistY;
                } else {
                    stepY=1;
                    sideDistY=(mapY+1d-SigRenderer.y)*deltaDistY;
                }

                while (hit==0) {
                    if (sideDistX<sideDistY) {
                        sideDistX+=deltaDistX;
                        mapX+=stepX;
                        side=0;
                    } else {
                        sideDistY+=deltaDistY;
                        mapY+=stepY;
                        side=1;
                    }
                    if (SigRenderer.worldMap[mapX][mapY]>0) {hit=1;}

                    if (side==0) {
                        perpWallDist=sideDistX-deltaDistX;
                    } else {
                        perpWallDist=sideDistY-deltaDistY;
                    }
                }

                int lineHeight = (int)(h/perpWallDist);
                int drawStart=-lineHeight/2+h/2;
                if (drawStart<0) {
                    drawStart=0;
                }
                int drawEnd=lineHeight/2+h/2;
                if (drawEnd>=h) {drawEnd=h-1;}

                /*Color col; //FLAT COLOR CHOOSER.
                switch(SigRenderer.worldMap[mapX][mapY]) {
                    case 1:{col=Color.RED;}break;
                    case 2:{col=Color.GREEN;}break;
                    case 3:{col=Color.BLUE;}break;
                    case 4:{col=Color.WHITE;}break;
                    default:{col=Color.YELLOW;}break;
                }

                if (side==1) {col=col.darker();}

                g.setColor(col);
                g.drawLine(x,drawStart,x,drawEnd);*/

                int texNum=SigRenderer.worldMap[mapX][mapY]-1;

                double wallX;
                if (side==0) {
                    wallX=SigRenderer.y+perpWallDist*rayDirY;
                } else {
                    wallX=SigRenderer.x+perpWallDist*rayDirX;
                }
                wallX-=Math.floor(wallX);

                int texX=(int)(wallX*(double)SigRenderer.TEXTURE_WIDTH);
                if (side==0&&rayDirX>0){texX=SigRenderer.TEXTURE_WIDTH-texX-1;}
                if (side==1&&rayDirY<0){texX=SigRenderer.TEXTURE_WIDTH-texX-1;}

                double step=1d*SigRenderer.TEXTURE_HEIGHT/lineHeight;
                double texPos=(drawStart-h/2+lineHeight/2)*step;
            for(int y=0; y<height; y++){
                if (y>=drawStart&&y<drawEnd) {
                    int texY=(int)texPos&(SigRenderer.TEXTURE_HEIGHT-1);
                    texPos+=step;
                    Color col = new Color(SigRenderer.textures[texNum][SigRenderer.TEXTURE_HEIGHT*texY+texX]);
                    if (side==1) {col=col.darker();}
                    p[ x + y * width] = col.getRGB();
                } else {
                    p[ x + y * width] = 0;
                }
            }
        }        
        i += 1;
        j += 1;    
        endTime=System.nanoTime();      
        SigRenderer.DRAWLOOPTIME=(endTime-startTime)/1000000f;
    }    
    private int i=1,j=256;

    public void repaint() {
        super.repaint();
        startTime = System.nanoTime();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // perform draws on pixels
        render();
        // ask ImageProducer to update image
        mImageProducer.newPixels();            
        // draw it on panel          
        g.drawImage(this.imageBuffer, 0, 0, this);  
    }
    
    /**
     * Overrides ImageObserver.imageUpdate.
     * Always return true, assuming that imageBuffer is ready to go when called
     */
    @Override
    public boolean imageUpdate(Image image, int a, int b, int c, int d, int e) {
        return true;
    }
    @Override
    public void run() {
        while (true) {
            // request a JPanel re-drawing
            repaint();                                  
            try {Thread.sleep(5);} catch (InterruptedException e) {}
        }
    }
}
