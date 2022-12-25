package summit.game.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.Player;
import summit.game.gamemap.GameMap;
import summit.game.item.ArrowItem;
import summit.game.item.BoneItem;
import summit.game.item.GoldCoin;
import summit.game.item.Item;
import summit.game.item.ItemStorage;
import summit.game.item.StickItem;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.Sprite;

public class ItemChest extends Structure{

    private ItemStorage items;
    
    //gamekey is one of the keys the player is trying to find
    public ItemChest(float x, float y, Item gameKey, GameMap parentMap) {
        super(x, y, 1, 1, parentMap);
        super.setSprite(Sprite.CHEST_CLOSED);
        // super.setLight(new Light(x, y, 4, new ColorFilter(0xffffff)));
        super.setColorFilter(new ColorFilter(0, 0, 0));
        super.setShadow(ColorFilter.NOFILTER);
        super.situate(parentMap);

        //make space under chest
        try{
            TileStack[][] tiles = parentMap.getTiles();

            tiles[(int)y-1][(int)x-1] = new TileStack(x-1, y-1);
            tiles[(int)y-1][(int)x] = new TileStack(x, y-1);
            tiles[(int)y-1][(int)x+1] = new TileStack(x+1, y-1);

            tiles[(int)y-1][(int)x-1].pushTile(new StoneTile(x-1, y-1));
            tiles[(int)y-1][(int)x].pushTile(new StoneTile(x, y-1));
            tiles[(int)y-1][(int)x+1].pushTile(new StoneTile(x+1, y-1));
        } catch(ArrayIndexOutOfBoundsException e) {
            //hit edge of map
        }

        this.items = new ItemStorage(null);
        
        Player p = parentMap.getPlayer();

        items.put(Sprite.ARROW_ITEM, new Stack<Item>());
        items.put(Sprite.STICK_ITEM, new Stack<Item>());
        items.put(Sprite.BONE_ITEM, new Stack<Item>());
        items.put(Sprite.GOLD_COIN, new Stack<Item>());

        items.put(Sprite.GREEN_KEY, new Stack<Item>());
        items.put(Sprite.BLUE_KEY, new Stack<Item>());

        addItems(new BoneItem(p), (int)(Math.random()*10));
        addItems(new ArrowItem(p), (int)(Math.random()*10));
        addItems(new GoldCoin(p), (int)(Math.random()*10));
        addItems(new StickItem(p), (int)(Math.random()*10));

        if(gameKey != null)
            items.get(gameKey.getSprite()).push(gameKey);
        
        // System.out.println(x + "  " + y);
    }

    public void addItems(Item copy, int num){
        
        for(int i = 0; i < num; i++){
            Item c = copy.copy();
            c.setOwner(null);
            items.get(copy.getSprite()).push(c);
        }
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        Player p = e.getMap().getPlayer();
        
        p.pickupItems(items);
        items.clear();

        super.setSprite(Sprite.CHEST_OPENED);
    }
}
