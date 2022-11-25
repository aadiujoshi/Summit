package summit.game.entity.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.util.GameRegion;

public class Item extends GameRegion{

    public Item(float x, float y, float width, float height) {
        super(x, y, width, height);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void collide(Entity g) {
        
    }
}
