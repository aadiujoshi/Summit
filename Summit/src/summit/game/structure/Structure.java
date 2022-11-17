package summit.game.entity.structure;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.entity.Entity;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.GameClickReciever;
import summit.util.Region;

public class Structure extends Region implements Paintable, GameClickReciever, GameUpdateReciever {

    private String sprite;
    private GameMap innerMap;

    public Structure(float x, float y, float width, float height) {
        super(x, y, width, height);
    }


    @Override
    public void renderLayer(OrderPaintEvent ope) {
        ope.getRenderLayers().addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {

        e.getRenderer().renderGame(sprite, 
                                    getX()-0.5f, getY()+0.5f, Renderer.NO_OP | Renderer.OUTLINE_RED, 
                                    new ColorFilter(255, 255, 255), 
                                    e.getCamera());

        e.getRenderer().renderGame(sprite, 
                                    getX()-0.5f, getY()+0.5f, Renderer.NO_OP | Renderer.OUTLINE_RED, 
                                    null, 
                                    e.getCamera());
        
        // e.renderLater(new Light(getX(), getY(), 2.3f, 100, 100, 0));
    }

    @Override
    public void gameClick(GameClickEvent e) {
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        TileStack[][] tiles = e.getMap().getTiles();
        
        int startX = Math.round(getX()-getWidth()/2);
        int startY = Math.round(getX()-getHeight()/2);

        int endX = Math.round(getX()+getWidth()/2);
        int endY = Math.round(getX()+getHeight()/2);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile t = tiles[y][x].peekTile();
                t.setBoundary(true);
                // t.setRenderOp(t.getRenderOp() | Renderer.OUTLINE_RED);
                // t.setColorFilter(new ColorFilter(255, -255, -255));

                Light l = new Light(x, y, 1.2f, -70, -70, -70);
                l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);

                t.setLight(l);
            }
        }
    }

    public void setSprite(String sprite){
        this.sprite = sprite;
    }

     public void setInnerMap(GameMap map){
        this.innerMap = map;
    }

    public GameMap getInnerMap(){
        return this.innerMap;
    }
}
