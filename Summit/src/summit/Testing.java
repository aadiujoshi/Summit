package summit;

import java.util.HashMap;

public class Testing {

    final static Object key = "hello";

    public static void main(String[] args) {
        // HashMap<String, Boolean> table = new HashMap<>();
        
        // table.put(key, true);

        // System.out.println(table.get(key));

        // table.put(key, false);

        // System.out.println(table.get(key));

        // long a = 9223372036854775807L;

        // System.out.println(Long.toBinaryString(a));
        // System.out.println(a);
        // System.out.println(a >> 32);

        String s = get(Float.class);

        System.out.println(s);
    }

    static <T> T get(Class<? extends Object> T){
        return (T)key;
    }
}
