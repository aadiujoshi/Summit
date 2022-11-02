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

                Window window = new Window("summit", screen.width/2, screen.height/2);
                
                window.setState(WindowState.SELECTIONMENUS);

                Container menu1 = new Container(null ,0.4f, 0.5f, 0.3f, 0.5f);
                // Container menu2 = new Container(null ,0.666f, 0.5f, 0.55f, 0.85f);

                Container submenu1 = new Container(menu1, 0.5f, 0.5f, 0.5f, 0.5f);

                menu1.addComponent(submenu1);

                // Menu menu2 = new Menu(0.5f, 0.5f, 0.5f, 0.5f);

                // window.pushHomeContainer(menu1);
                // window.pushContainer(menu2);

            }
        });

        main.start();
    }
}
