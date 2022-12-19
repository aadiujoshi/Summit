package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.PaintEvent;

public class IceTile extends Tile{

    private int health;

    public IceTile(float x, float y) {
        super(x, y);
        super.setIced(true);
        super.setBreakable(true);

        health = 3;
    }
    
    @Override
    public void gameClick(GameUpdateEvent e){
        health--;

        if(health <= 0){
            super.gameClick(e);
        }
    }

    //transparent
    @Override
    public void paint(PaintEvent e){
        // setColorFilter(new ColorFilter(0, 0, 50));
        
        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().filterRect(p.x-7, p.y-8, 16, 16, getColorFilter());
    }
}
