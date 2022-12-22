package summit.game.structure;

import java.util.HashMap;
import java.util.Map;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.Player;
import summit.game.gamemap.GameMap;
import summit.game.item.ArrowItem;
import summit.game.item.BoneItem;
import summit.game.item.GoldCoin;
import summit.game.item.Item;
import summit.game.item.StickItem;
import summit.gfx.Sprite;

public class ItemChest extends Structure{

    private HashMap<Item, Integer> items;
    private int gameKey;

    //gamekey is one of the keys the player is trying to find
    public ItemChest(float x, float y, int gameKey, GameMap parentMap) {
        super(x, y, 1, 1, parentMap);
        super.setSprite(Sprite.LAVA_TILE);
        this.items = new HashMap<>();

        this.gameKey = gameKey;

        Player p = parentMap.getPlayer();

        items.put(new BoneItem(p), (int)(Math.random()*20));
        items.put(new ArrowItem(p), (int)(Math.random()*20));
        items.put(new GoldCoin(p), (int)(Math.random()*20));
        items.put(new StickItem(p), (int)(Math.random()*20));
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        
        Player p = e.getMap().getPlayer();
        
        for (Map.Entry<Item, Integer> item : items.entrySet()) {
            p.addItems(item.getKey(), item.getKey().getSprite(), item.getValue());
        }

        if(gameKey > -1){
            p.getObtainedKeys()[gameKey] = true;
        }

        super.setSprite(Sprite.MENUBOX5);
    }
}
