package summit.testing;

import java.util.HashMap;

import summit.sound.Sound;
import summit.util.Settings;

public class Testing {

    final static Object key = "hello";

    public static void main(String[] args) {
        // System.out.println(Settings.getSetting("vsync"));
        // System.out.println(Settings.getSetting("vsync"));

        // Settings.changeSetting("vsync", 5);

        Sound.WALKING_HARD.play();

        // Sound.DUNGEON_SOUNDS.play();

        // try {
        //     Thread.sleep(10000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
