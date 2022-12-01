package summit.game.tile;

import java.io.Serializable;
import java.util.Stack;

import summit.game.GameUpdateEvent;
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

    public Tile peekTile(){
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
        for(Tile t: tiles){
            if(t != null)
                t.setRenderLayer(ope);
        }
    }

    @Override
    public void update(GameUpdateEvent e) {
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
        
        if(peekTile().getReqPushTile() != null){
            pushTile(peekTile().getReqPushTile());
            peekTile().setReqPushTile(null);
        }
    }
}