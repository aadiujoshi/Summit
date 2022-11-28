package summit;
import java.awt.Toolkit;

import summit.gfx.BufferedSprites;
import summit.gui.Window;
import summit.gui.WindowState;
import summit.util.Time;

public class Main {
    public static void main(String[] args) {
        Thread main = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedSprites.loadSprites("src/summit/resources");

                java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

                System.out.println(screen);

                Window summit = new Window("Summit");

                Time.nanoDelay(Time.NS_IN_S/4*3);

                // summit.setState(WindowState.NEWGAME);
            }
        }, "mainthread");

        main.start();
    }
}
