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

public class TileStack implements Serializable, GameClickReciever, Paintable, GameUpdateReciever {

    private Stack<Tile> tiles;
    private float x, y;
    
    public TileStack(float x, float y){
        tiles = new Stack<>();
        this.x = x;
        this.y = y;
    }

    public void pushTile(Tile tile){
        tile.setDepth(tiles.size());
        tiles.push(tile);
    }

    public Tile popTile(){
        return tiles.pop();
    }

    public Tile topTile(){
        if(tiles.isEmpty())
            return null;
        return tiles.peek();
    }

    @Override
    public void gameClick(GameUpdateEvent e){
        tiles.peek().gameClick(e);
    }

    @Override 
    public void paint(PaintEvent e){
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        for (int i = 0; i < tiles.size(); i++) {
            Tile t = tiles.get(i);

            if(t != null)
                t.setRenderLayer(ope);
        }
    }

    @Override
    public void update(GameUpdateEvent e) {
        
        if(tiles.size()-2 >= 0)
            tiles.get(tiles.size()-2).setIced(topTile().isIced());

        for(int i = 0; i < tiles.size(); i++){
            Tile t = tiles.get(i);
            t.update(e);
            if(t.destroyed()){
                t.destroy(e);
                tiles.remove(i);
                i--;
                continue;
            }
        }

        // Tile req = peekTile().getReqPushTile();

        // if(req != null){
        //     pushTile(req);
        //     peekTile().setReqPushTile(null);
        //     System.out.println(tiles);
        // }
    }

    public void reinit(){
        for (Tile tile : tiles) {
            tile.reinit();
        }
    }
}