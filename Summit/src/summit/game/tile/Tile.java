package summit.game.tile;

import summit.game.GameUpdate;
import summit.gfx.Paintable;
import summit.gui.GameClickListener;
import summit.util.Region;

public abstract class Tile extends Region implements GameClickListener, Paintable, GameUpdate {

    private boolean setToDestroy;

    // public boolean isSetToDestroy() {
    //     return this.setToDestroy;
    // }

    public void setDestroy(boolean setToDestroy) {
        this.setToDestroy = setToDestroy;
    }

    public Tile(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    public Tile(float x, float y){
        this(x, y, 1, 1);
    }

    // public boolean isSetToDestroy(){
        
    // }
}