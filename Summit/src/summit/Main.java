/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit;
import java.awt.Toolkit;

import summit.gfx.BufferedSprites;
import summit.gui.Window;
import summit.gui.WindowState;

public class Main {

    public static final String path = System.getProperty("user.dir") + "/src/summit/";
    public static final String os = System.getProperty("os.name");

    public static void main(String[] args) {
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        System.out.println(screen);
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("os.name"));

        BufferedSprites.loadSprites();

        Window summit = new Window("Summit");

        summit.setState(WindowState.SELECTIONMENUS);
    }
}
