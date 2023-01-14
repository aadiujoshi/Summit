/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import summit.game.GameUpdateEvent;

public class Controls {
    private static ArrayList<ControlsReciever> cr = new ArrayList<>();

    private Controls(){}

    public static volatile boolean W;
    public static volatile boolean A;
    public static volatile boolean S;
    public static volatile boolean D;

    public static volatile boolean Q;
    public static volatile boolean R;
    public static volatile boolean T;
    public static volatile boolean F;
    public static volatile boolean C;

    public static volatile boolean UP;
    public static volatile boolean DOWN;
    public static volatile boolean LEFT;
    public static volatile boolean RIGHT;
    public static volatile boolean SHIFT;
    public static volatile boolean SPACEBAR;

    //toggleable ; NOT press and release
    public static volatile boolean E;

    
    /** 
     * @param e
     * @param ev
     */
    public static void setPress(KeyEvent e, GameUpdateEvent ev){
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
            case KeyEvent.VK_Q:
                Q = true;
                break;
            case KeyEvent.VK_R:
                R = true;
                break;
            case KeyEvent.VK_T:
                T = true;
                break;
            case KeyEvent.VK_F:
                F = true;
                break;
            case KeyEvent.VK_C:
                C = true;
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

        for (ControlsReciever controlsReciever : cr) {
            controlsReciever.keyPress(ev);
        }
    }

    
    /** 
     * @param e
     * @param ev
     */
    public static void setRelease(KeyEvent e, GameUpdateEvent ev){
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
            case KeyEvent.VK_Q:
                Q = false;
                break;
            case KeyEvent.VK_R:
                R = false;
                break;
            case KeyEvent.VK_T:
                T = false;
                break;
            case KeyEvent.VK_F:
                F = false;
                break;
            case KeyEvent.VK_C:
                C = false;
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

        for (ControlsReciever controlsReciever : cr) {
            controlsReciever.keyRelease(ev);
        }
    }

    
    /** 
     * @param contR
     */
    public static synchronized void addControlsReciever(ControlsReciever contR){
        synchronized(cr){
            cr.add(contR);
        }
    } 
}
