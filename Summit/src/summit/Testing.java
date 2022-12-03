package summit;

import java.util.HashMap;

public class Testing {

    final static String key = "hello";

    public static void main(String[] args) {
        HashMap<String, Boolean> table = new HashMap<>();
        

        table.put(key, true);

        System.out.println(table.get(key));

        table.put(key, false);

        System.out.println(table.get(key));
    }
}
