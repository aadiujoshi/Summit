package summit.game.tile;

import summit.game.GameUpdateReciever;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gui.GameClickReciever;
import summit.util.Region;

public abstract class Tile extends Region implements GameClickReciever, Paintable, GameUpdateReciever {

    //if true -> entitys cannot pass through this tile
    private boolean boundary;

    private String sprite;
    
    public Tile(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    public Tile(float x, float y){
        this(x, y, 1, 1);
    }
    
    @Override
    public void paint(PaintEvent e){
        e.getRenderer().renderGame(sprite, getX(), getY(), 0, e.getCamera());
    } 


    //-------------  getters and setters  ----------------------------------------------------------

    public String getSprite() {
        return this.sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public boolean isBoundary() {
        return this.boundary;
    }

    public void setBoundary(boolean bounded) {
        this.boundary = bounded;
    }

}