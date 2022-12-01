/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.awt.event.KeyEvent;

public class Controls {
    
    private Controls(){}

    public static volatile boolean W;
    public static volatile boolean A;
    public static volatile boolean S;
    public static volatile boolean D;
    public static volatile boolean UP;
    public static volatile boolean DOWN;
    public static volatile boolean LEFT;
    public static volatile boolean RIGHT;
    public static volatile boolean SHIFT;
    public static volatile boolean SPACEBAR;

    //toggleable not press and release
    public static volatile boolean E;

    public static void setPress(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                W = true;
                break;
            case KeyEvent.VK_A:
                A = true;
                break;
            case KeyEvent.VK_S:
                S = true;
                break;
            case KeyEvent.VK_D:
                D = true;
                break;
            case KeyEvent.VK_UP:
                UP = true;
                break;
            case KeyEvent.VK_DOWN:
                DOWN = true;
                break;
            case KeyEvent.VK_LEFT:
                LEFT = true;
                break;
            case KeyEvent.VK_RIGHT:
                RIGHT = true;
                break;
            case KeyEvent.VK_SHIFT:
                SHIFT = true;
                break;
            case KeyEvent.VK_SPACE:
                SPACEBAR = true;
                break;
            
            //---------- togglable -----------------

            case KeyEvent.VK_E:
                E = !E;
                break;
            case KeyEvent.VK_ESCAPE:
                E = false;
                break;
        }
    }

    public static void setRelease(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                W = false;
                break;
            case KeyEvent.VK_A:
                A = false;
                break;
            case KeyEvent.VK_S:
                S = false;
                break;
            case KeyEvent.VK_D:
                D = false;
                break;
            case KeyEvent.VK_UP:
                UP = false;
                break;
            case KeyEvent.VK_DOWN:
                DOWN = false;
                break;
            case KeyEvent.VK_LEFT:
                LEFT = false;
                break;
            case KeyEvent.VK_RIGHT:
                RIGHT = false;
                break;
            case KeyEvent.VK_SHIFT:
                SHIFT = false;
                break;
            case KeyEvent.VK_SPACE:
                SPACEBAR = false;
                break;
            
            //--------- toggleable --------------------------

            case KeyEvent.VK_E:
                break;
        }
    }
}
