package summit.forbhattacharya;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.image.BufferedImage;

//GOOD SEEDS:
/*
 * 7612044935570407424
 * 2774791137792783360
 * 7346731479933566976
 * 
 * 
 */

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
        window.setSize(1280, 720);

        super.setPreferredSize(new java.awt.Dimension(1280, 720));

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

        long seed = (long)(Math.random()*Long.MAX_VALUE);
        // long seed = 5906817065274444800L;

        Random rand = new Random(seed);

        gen(rand, width/2, height/2, (Object)0, img);
        gen(rand, width/2, height/2, (Object)0, img);
        gen(rand, width/2, height/2, (Object)0, img);

        System.out.println("finished expanding seed: " + seed);
        stack = 0;



        // Graphics g2 = img.getGraphics();
        // g2.setColor(Color.RED);
        // g2.fillRect(0, 0, width, height);

        g.drawImage(img, 0,0, super.getWidth(), super.getHeight(), null);
    }

    private void gen(Random rand, int x, int y, Object iterations, BufferedImage tiles){
        if((int)iterations >= 5000)
            return;

        iterations = (int)iterations + 1;

        tiles.setRGB(x, y, color);

        double c = rand.nextInt(4);

        //left
        if(c == 0){
            if(x-1 > -1){
                gen(rand, x-1, y, iterations, tiles);
            }
        }

        //up
        if(c == 1){
            if(y-1 > -1){
                gen(rand, x, y-1, iterations, tiles);
            }
        }

        //down
        if(c == 2){
            if(y+1 < tiles.getHeight()){
                gen(rand, x, y+1, iterations, tiles);
            }
        }

        //right
        if(c == 3){
            if(x+1 < tiles.getWidth()){
                gen(rand, x+1, y, iterations, tiles);
            }
        }
    }
}