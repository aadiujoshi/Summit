package summit;
import summit.gfx.BufferedSprites;
import summit.gfx.PaintEvent;
import summit.gui.Window;
import summit.gui.WindowState;
import summit.gui.menu.Container;

import java.awt.Toolkit;

public class Main {
    public static void main(String[] args) {
        Thread main = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedSprites.loadSprites("Summit\\Summit\\src\\summit\\resources");

                java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

                System.out.println(screen);

                Window window = new Window("Summit", screen.width/2, screen.height/2);
                
            }
        });

        main.start();
    }
}
