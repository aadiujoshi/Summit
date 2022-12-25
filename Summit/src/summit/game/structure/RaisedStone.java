package summit.game.structure;

import java.awt.Point;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class RaisedStone extends Structure{

    public RaisedStone(float x, float y, GameMap parentMap) {
        super(x, y, 1, 1, parentMap);
        super.setSpriteOffsetY(0.25f);
        super.setSprite(Sprite.RAISED_STONE);
        super.setShadow(ColorFilter.NOFILTER);
        super.setEnabled(false);
        super.situate(parentMap);
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().fillRect((int)(1+p.x-getWidth()/2*16), (int)(p.y-getHeight()/2*16), (int)(getWidth()*16), (int)(getHeight()*16), Renderer.toIntRGB(0, 255, 0));
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
    }
}
