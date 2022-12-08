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

    public static final String os = System.getProperty("os.name");
    
    public static final String path = System.getProperty("user.dir") + 
                                        (os.contains("Windows") ? "\\Summit\\src\\summit\\" : "/Summit/src/summit/");

    public static void main(String[] args) {
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        System.out.println("\n" + screen + "\n");
        System.out.println(path + "\n");
        System.out.println(System.getProperty("os.name") + "\n");

        BufferedSprites.loadSprites();

        Window summit = new Window("Summit");

        summit.setState(WindowState.SELECTIONMENUS);
    }
}
