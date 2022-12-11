package summit.forbhattacharya;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.image.BufferedImage;

public class CaveGenVisualization extends JPanel{

    private static JFrame window;

    private int width = 128;
    private int height = 128;

    private int color = 0x00ff00;

    private int stack = 0;

    public static void main(String args[]) {
        new CaveGenVisualization();
    }

    public CaveGenVisualization(){
        window = new JFrame("testing thing for summit game map generation");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(width, width);

        super.setPreferredSize(new java.awt.Dimension(width, height));

        window.add(this);

        window.addKeyListener(new java.awt.event.KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == 'r')
                    repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }

        });

        window.setVisible(true);
    }

    @Override
    public void paint(Graphics gr){
        Graphics2D g = (Graphics2D) gr;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random((long)(Math.random()*Long.MAX_VALUE));

        expand(rand, width/2, height/2, img);
        System.out.println("finished expanding");
        stack = 0;

        // Graphics g2 = img.getGraphics();
        // g2.setColor(Color.RED);
        // g2.fillRect(0, 0, width, height);

        g.drawImage(img, 0,0, super.getWidth(), super.getHeight(), null);

    }

    private void expand(Random rand, int x, int y, BufferedImage img){
        if(stack >= 7500)
            return;
        stack++;

        img.setRGB(x, y, color);
        
        double c = rand.nextInt(4);

        //left
        if(c == 0){
            if(x-1 > -1){
                expand(rand, x-1, y, img);
            }
        }

        //up
        if(c == 1){
            if(y-1 > -1){
                expand(rand, x, y-1, img);
            }
        }

        //down
        if(c == 2){
            if(y+1 < height){
                expand(rand, x, y+1, img);
            }
        }

        //right
        if(c == 3){
            if(x+1 < width){
                expand(rand, x+1, y, img);
            }
        }

        // left
        // if(x-1 > -1 && !tiles[y][x-1]){
        //     if(rand.nextDouble() > 0.3){
        //         expand(tiles, rand, x-1, y, img);
        //     }
        // }

        // //up
        // if(y-1 > -1 && !tiles[y-1][x]){
        //     if(rand.nextDouble() > 0.3){
        //         expand(tiles, rand, x, y-1, img);
        //     }
        // }

        // //down
        // if(y+1 < height && !tiles[y+1][x]){
        //     if(rand.nextDouble() > 0.3){
        //         expand(tiles, rand, x, y+1, img);
        //     }
        // }

        // //right
        // if(x+1 < width && !tiles[y][x+1]){
        //     if(rand.nextDouble() > 0.3){
        //         expand(tiles, rand, x+1, y, img);
        //     }
        // }
    }
}