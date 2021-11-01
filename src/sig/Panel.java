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
        g.setColor(Color.BLACK);
        g.fillRect(0,0,SigRenderer.SCREEN_WIDTH,SigRenderer.SCREEN_HEIGHT);
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

        final int h=SigRenderer.SCREEN_HEIGHT;

        for (int x=0;x<SigRenderer.SCREEN_WIDTH;x++) {
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

            Color col;
            switch(SigRenderer.worldMap[mapX][mapY]) {
                case 1:{col=Color.RED;}break;
                case 2:{col=Color.GREEN;}break;
                case 3:{col=Color.BLUE;}break;
                case 4:{col=Color.WHITE;}break;
                default:{col=Color.YELLOW;}break;
            }

            if (side==1) {col=col.darker();}

            g.setColor(col);
            g.drawLine(x,drawStart,x,drawEnd);
        }
        endTime=System.nanoTime();
        SigRenderer.DRAWLOOPTIME = (endTime-startTime)/1000000f;
    }
}
