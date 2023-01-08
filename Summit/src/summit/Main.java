/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit;
import java.awt.Toolkit;

import summit.gfx.BufferedSprites;
import summit.gui.Window;

/*
 *  vsync 0
 *  ambient_occlusion true
 *  threads 1
*/

public class Main {

    // public static final String os = System.getProperty("os.name");

    //extra path incase folder names are different
    public static final String path = "";

    public static void main(String[] args) {
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        System.out.println("\n" + screen + "\n");
        System.out.println(System.getProperty("user.dir") + "\n");
        System.out.println(System.getProperty("os.name") + "\n");

        BufferedSprites.loadSprites();

        Window summit = new Window("Summit");

        // summit.setState(WindowState.SELECTIONMENUS);
    }
}
