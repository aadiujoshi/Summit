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

/**
 * 
 * The ItemChest class is a Structure that serves as a container for items that
 * can be picked up by the player.
 * 
 * It has a sprite of a closed chest and when clicked it changes to an open
 * chest sprite.
 * 
 * The chest also creates a space under it and adds random items in it.
 * 
 * This class also overrides the reinit method to reset the items inside the
 * chest and the gameClick method to allow the player to pick up the items from
 * the chest.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class ItemChest extends Structure {

    private ItemStorage items;

    // gamekey is one of the keys the player is trying to find

    /**
     * 
     * Creates a new instance of the ItemChest class.
     * 
     * @param x         the x position of the ItemChest on the map
     * 
     * @param y         the y position of the ItemChest on the map
     * 
     * @param gameKey   the item the player is trying to find
     * 
     * @param parentMap the GameMap that the ItemChest is currently on
     */
    public ItemChest(float x, float y, Item gameKey, GameMap parentMap) {
        super(x, y, 1, 1, parentMap);
        super.setSprite(Sprite.CHEST_CLOSED);
        super.setColorFilter(new ColorFilter(0, 0, 0));
        super.setShadow(ColorFilter.NOFILTER);
        super.situate(parentMap);

        // make space under chest
        try {
            TileStack[][] tiles = parentMap.getTiles();

            tiles[(int) y - 1][(int) x - 1] = new TileStack(x - 1, y - 1);
            tiles[(int) y - 1][(int) x] = new TileStack(x, y - 1);
            tiles[(int) y - 1][(int) x + 1] = new TileStack(x + 1, y - 1);

            tiles[(int) y - 1][(int) x - 1].pushTile(new StoneTile(x - 1, y - 1));
            tiles[(int) y - 1][(int) x].pushTile(new StoneTile(x, y - 1));
            tiles[(int) y - 1][(int) x + 1].pushTile(new StoneTile(x + 1, y - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print(e + " at item chest generation");
        }

        this.items = new ItemStorage(null);

        Player p = parentMap.getPlayer();

        items.addItems(new ArrowItem(null), (int) (Math.random() * 10));
        items.addItems(new StickItem(null), (int) (Math.random() * 10));
        items.addItems(new BoneItem(null), (int) (Math.random() * 10));
        items.addItems(new GoldCoin(null), (int) (Math.random() * 10));

        items.addItems(new GreenKey(null), 0);
        items.addItems(new BlueKey(null), 0);

        if (gameKey != null)
            items.get(gameKey.getSprite()).push(gameKey);

        // System.out.println(x + " " + y);
    }

    /**
     * The reinit() method of the ItemChest class is used to reset the state of the
     * object
     * and the items inside the chest.
     */
    @Override
    public void reinit() {
        items.reinit();
    }

    /**
     * The gameClick method is responsible for updating the state of the ItemChest
     * object when it is clicked by the player.
     * In this implementation, the method allows the player to pick up the items
     * from the chest and changes the chest sprite to open.
     * 
     * @param e GameUpdateEvent object that contains information about the current
     *          game state.
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        Player p = e.getMap().getPlayer();

        p.pickupItems(items);
        items.clear();

        super.setSprite(Sprite.CHEST_OPENED);
    }
}
