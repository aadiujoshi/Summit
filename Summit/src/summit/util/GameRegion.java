package summit.util;

import summit.game.GameClickReciever;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;

/**
 * The superclass for basically anything in the actual game
 */
public abstract class GameRegion extends Region implements Paintable, GameClickReciever{

    private String sprite;

    private float spriteOffsetX;
    private float spriteOffsetY;
    
    private int renderOp;
    private ColorFilter filter = ColorFilter.NOFILTER;

    public GameRegion(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void paint(PaintEvent e) {
        if(this.contains(e.gameX(), e.gameY()))
            setRenderOp(getRenderOp() | Renderer.OUTLINE_BLUE | Renderer.OUTLINE_GREEN | Renderer.OUTLINE_RED);
        else
            setRenderOp(getRenderOp() & ~Renderer.OUTLINE_BLUE & ~Renderer.OUTLINE_GREEN & ~Renderer.OUTLINE_RED);

        e.getRenderer().renderGame(sprite, 
                                    getX()+spriteOffsetX, getY()+spriteOffsetY, renderOp,
                                    filter,
                                    e.getCamera());
    }

    public float getSpriteOffsetX() {
        return this.spriteOffsetX;
    }

    public void setSpriteOffsetX(float spriteOffsetX) {
        this.spriteOffsetX = spriteOffsetX;
    }

    public float getSpriteOffsetY() {
        return this.spriteOffsetY;
    }

    public void setSpriteOffsetY(float spriteOffsetY) {
        this.spriteOffsetY = spriteOffsetY;
    }
    
    public void setSprite(String sprite){
        this.sprite = sprite;
    }

    public void setRenderOp(int r){
        renderOp = r;
    }

    public int getRenderOp(){
        return renderOp;
    }

    public void setColorFilter(ColorFilter cf){
        this.filter = cf;
    }

    public ColorFilter getColorFilter(){
        return this.filter;
    }

}
