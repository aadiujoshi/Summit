/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit;
import java.awt.Toolkit;

import summit.gfx.BufferedSprites;
import summit.gui.Window;

public class Main {

    // public static final String os = System.getProperty("os.name");

    //extra path incase folder names are different
    public static final String path = "";

    /** 
     * @param args
     */
    public static void main(String[] args) {
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        System.out.println("\n" + screen + "\n");
        System.out.println(System.getProperty("user.dir") + "\n");
        System.out.println(System.getProperty("os.name") + "\n");

        BufferedSprites.loadSprites();
        
        Window summit = new Window("Summit");

        // summit.setState(WindowState.SELECTIONMENUS);
    }

    
    /** 
     * @return String
     */
    //util method for unique string
    public static String generateSaveKey(){
        String f = "";

        for (int i = 0; i < 16; i++) {
            int c = (int)(Math.random()*59+64);
            if(!Character.isAlphabetic(c)){
                f+="L";
                continue;
            }
            f+=(char)c;
        }
        
        return f;
    }
}
