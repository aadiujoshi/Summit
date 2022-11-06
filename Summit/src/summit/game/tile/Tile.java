package summit.game.tile;

import summit.game.GameUpdateReciever;
import summit.gfx.Paintable;
import summit.gui.GameClickReciever;
import summit.util.Region;

public abstract class Tile extends Region implements GameClickReciever, Paintable, GameUpdateReciever {

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
}