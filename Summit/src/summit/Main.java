/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit;
import java.awt.Toolkit;

import summit.gfx.BufferedSprites;
import summit.gui.Window;
import summit.gui.WindowState;
import summit.util.Time;

public class Main {
    public static void main(String[] args) {
        BufferedSprites.loadSprites();

        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        System.out.println(screen);
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("os.name"));

        Window summit = new Window("Summit");

        Time.nanoDelay(Time.NS_IN_S/4*3);

        // summit.setState(WindowState.NEWGAME);
    }
}
