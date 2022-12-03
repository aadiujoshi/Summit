package summit.game.entity;

import java.io.Serializable;
import java.util.HashMap;

public class GameTags implements Serializable{
    private HashMap<String, Boolean> tags;

    public GameTags() {
        tags = new HashMap<>();
    }

    public boolean is(String s){
        return tags.get(s);
    }

    public void set(String property, boolean b){
        tags.put(property, b);
    }
}
