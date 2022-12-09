package summit;

import java.util.HashMap;

import summit.util.Settings;

public class Testing {

    final static Object key = "hello";

    public static void main(String[] args) {
        // System.out.println(Settings.getSetting("vsync"));
        System.out.println(Settings.getSetting("vsync"));

        Settings.changeSetting("vsync", 5);
    }
}
