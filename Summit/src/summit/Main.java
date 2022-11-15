package summit;
import java.awt.Toolkit;

import summit.gfx.BufferedSprites;
import summit.gui.Window;

public class Main {
    public static void main(String[] args) {
        Thread main = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedSprites.loadSprites("C:/Users/aadiu/Desktop/Programming Files/Personal Projects/Java Projects/Summit Game/Summit/Summit/src/summit/resources");

                java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

                System.out.println(screen);

                Window window = new Window("Summit");
            }
        });

        main.start();
    }
}
