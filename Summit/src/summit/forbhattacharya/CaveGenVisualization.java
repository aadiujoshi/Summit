package summit.forbhattacharya;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;

public class CaveGenVisualization extends JPanel{

    private static JFrame window;

    private int width = 500;
    private int height = 500;

    private int color = 0x00ff00;

    private int stack = 0;

    public CaveGenVisualization(){
        window = new JFrame("testing thing for summit game map generation");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(width, width);

        this.setPreferredSize(new java.awt.Dimension(width, height));

        window.add(this);

        window.setVisible(true);
    }

    @Override
    public void paint(Graphics gr){
        Graphics2D g = (Graphics2D) gr;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Random rand = new Random(100L);
        
        expand(new boolean[height][width], rand, 0, 0, img);

        System.out.println("finished expanding");

        stack = 0;

        g.drawImage(img, null, 0, 0);

    }

    private void expand(boolean[][] tiles, Random rand, int x, int y, BufferedImage img){
        if(stack > 5000)
            return;
        
        tiles[y][x] = true;

        stack++;

        img.setRGB(x, y, color);

        // System.out.println(x +  "   "  + y);

        //left
        if(x-1 > -1 && !tiles[y][x-1]){
            if(rand.nextDouble() > 0.3){
                expand(tiles, rand, x-1, y, img);
            }
        }

        //up
        if(y-1 > -1 && !tiles[y-1][x]){
            if(rand.nextDouble() > 0.3){
                expand(tiles, rand, x, y-1, img);
            }
        }

        //down
        if(y+1 < height && !tiles[y+1][x]){
            if(rand.nextDouble() > 0.3){
                expand(tiles, rand, x, y+1, img);
            }
        }

        //right
        if(x+1 < width && !tiles[y][x+1]){
            if(rand.nextDouble() > 0.3){
                expand(tiles, rand, x+1, y, img);
            }
        }
    }


    public static void main(String args[]) {
        new CaveGenVisualization();
    }
}