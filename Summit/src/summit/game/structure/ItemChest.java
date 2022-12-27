package summit.game.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.Player;
import summit.game.gamemap.GameMap;
import summit.game.item.ArrowItem;
import summit.game.item.BlueKey;
import summit.game.item.BoneItem;
import summit.game.item.GoldCoin;
import summit.game.item.GreenKey;
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
            System.out.print(e + " at item chest generation");
        }

        this.items = new ItemStorage(null);
        
        Player p = parentMap.getPlayer();

        items.addItems(new ArrowItem(null), (int)(Math.random()*10));
        items.addItems(new StickItem(null), (int)(Math.random()*10));
        items.addItems(new BoneItem(null), (int)(Math.random()*10));
        items.addItems(new GoldCoin(null), (int)(Math.random()*10));

        items.addItems(new GreenKey(null), 0);
        items.addItems(new BlueKey(null), 0);

        if(gameKey != null)
            items.get(gameKey.getSprite()).push(gameKey);
        
        // System.out.println(x + "  " + y);
    }
    
    @Override
    public void reinit(){
        items.reinit();
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        Player p = e.getMap().getPlayer();
        
        p.pickupItems(items);
        items.clear();

        super.setSprite(Sprite.CHEST_OPENED);
    }
}
