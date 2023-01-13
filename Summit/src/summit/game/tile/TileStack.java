/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import java.io.Serializable;
import java.util.Stack;

import summit.game.GameClickReciever;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;

/**
 * 
 * The TileStack class is a container for holding and managing multiple
 * instances of the Tile class.
 * It implements the GameClickReciever, Paintable, and GameUpdateReciever
 * interfaces to allow for interaction and rendering of its contained tiles.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class TileStack implements Serializable, GameClickReciever, Paintable, GameUpdateReciever {

    private Stack<Tile> tiles;
    private float x, y;

    /**
     * 
     * Constructs a new TileStack object at the specified x and y coordinates on the
     * game map.
     * 
     * @param x the x coordinate of the TileStack on the game map
     * @param y the y coordinate of the TileStack on the game map
     */
    public TileStack(float x, float y) {
        tiles = new Stack<>();
        this.x = x;
        this.y = y;
    }

    /**
     * 
     * Pushes a new Tile object onto the top of the TileStack.
     * 
     * @param tile the Tile object to be added to the stack
     */
    public void pushTile(Tile tile) {
        tile.setDepth(tiles.size());
        tiles.push(tile);
    }

    /**
     * 
     * Removes and returns the top Tile object from the TileStack.
     * 
     * @return the top Tile object from the TileStack
     */
    public Tile popTile() {
        return tiles.pop();
    }

    /**
     * 
     * Returns the top tile in the stack, but does not remove it.
     * If the stack is empty, returns null.
     * 
     * @return the top tile in the stack, or null if the stack is empty
     */
    public Tile topTile() {
        if (tiles.isEmpty())
            return null;
        return tiles.peek();
    }

    /**
     * 
     * Handles a click event on the tile stack. The click event will be passed on to
     * the top tile in the stack.
     * 
     * @param e the game update event that contains information about the click
     *          event
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        tiles.peek().gameClick(e);
    }

    /**
     * 
     * Paints the tile stack on the game screen. The top tile in the stack will be
     * painted on top.
     * 
     * @param e the paint event that contains information about the graphics context
     */
    @Override
    public void paint(PaintEvent e) {
    }

    /**
     *
     * 
     * Sets the render layer for the top tile in the stack.
     * 
     * @param ope the OrderPaintEvent that contains the information about the
     *            current render layers
     */
    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        tiles.peek().setRenderLayer(ope);
        // for (int i = 0; i < tiles.size(); i++) {
        // Tile t = tiles.get(i);

        // if(t != null)
        // t.setRenderLayer(ope);
        // }
    }

    /**
     *
     * 
     * The update method is called every game loop and is used to update the state
     * of the TileStack object.
     * 
     * In this method, it first checks the if the second top tile should be iced or
     * not, based on the top tile.
     * 
     * Then it iterates through all the tiles in the stack and updates each one. If
     * any tile is destroyed, it is removed from the stack.
     * 
     * @param e The GameUpdateEvent that contains information about the current game
     *          state
     * 
     * @throws Exception
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {

        if (tiles.size() - 2 >= 0)
            tiles.get(tiles.size() - 2).setIced(topTile().isIced());

        for (int i = 0; i < tiles.size(); i++) {
            Tile t = tiles.get(i);
            t.update(e);
            if (t.destroyed()) {
                t.destroy(e);
                tiles.remove(i);
                i--;
                continue;
            }
        }

        // Tile req = peekTile().getReqPushTile();

        // if(req != null){
        // pushTile(req);
        // peekTile().setReqPushTile(null);
        // System.out.println(tiles);
        // }
    }

    /**
     * 
     * The reinit method is used to reset the state of the TileStack object. This
     * method iterates through all the tiles in the stack
     * and calls the reinit method for each tile.
     */
    public void reinit() {
        for (Tile tile : tiles) {
            tile.reinit();
        }
    }
}