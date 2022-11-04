package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdate;
import summit.game.GameUpdateEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class SnowTile extends Tile{

    public SnowTile(float width, float height, float x, float y){
        super(x, y, width, height);
    }

    public SnowTile(float x, float y){
        super(x, y);
    }

    @Override
    public void paint(PaintEvent e){
        e.getRenderer().renderGame(Sprite.SNOW_TILE, getX(), getY(), Renderer.FLIP_NONE, e.getCamera());
    }

    @Override
    public void gameClick(GameMap map, MouseEvent e){
        // if(map.getPlayer().inHand() instanceof ShovelTool){

        // }
    }

    @Override
    public void update(GameUpdateEvent e) {
        //do nothing
    }
}