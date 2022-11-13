package summit;
import java.awt.Toolkit;

import summit.gfx.BufferedSprites;
import summit.gui.Window;

public class Main {
    public static void main(String[] args) {
        Thread main = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedSprites.loadSprites("Summit/Summit/src/summit/resources");

                java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

                System.out.println(screen);

                Window window = new Window("Summit");
            }
        });

        main.start();
    }
}
