/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.structure;

import java.util.ArrayList;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.gamemap.GameMap;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.RenderLayers;
import summit.util.GameRegion;

public abstract class Structure extends GameRegion {

    private ColorFilter shadow = new ColorFilter(-60, -60, -60);

    private ArrayList<Light> shadows;

    private GameMap parentMap;

    public Structure(float x, float y, float width, float height, GameMap parentMap) {
        super(x, y, width, height);
        super.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER);
        this.situate(parentMap);
        this.parentMap = parentMap;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        super.setRenderLayer(ope);
        
        for (Light s : shadows) {
            s.setRenderLayer(ope);
        }
    }

    @Override
    public void update(GameUpdateEvent e) {
        //do nothing
    }

    @Override
    public void collide(Entity e){
        //do nothing
    }

    public void situate(GameMap map){
        shadows = new ArrayList<>();

        TileStack[][] tiles = map.getTiles();

        int startX = Math.round(getX()-getWidth()/2);
        int startY = Math.round(getY()-getHeight()/2);

        int endX = Math.round(getX()+getWidth()/2);
        int endY = Math.round(getY()+getHeight()/2);

        for (int x1 = startX; x1 < endX; x1++) {
            for (int y1 = startY; y1 < endY; y1++) {
                if(x1 < 0 || x1 > map.getWidth() || y1 < 0 || y1 > map.getHeight())
                    continue;

                Tile t = tiles[y1][x1].topTile();
                t.setBoundary(true);

                Light l = new Light(x1, y1, 1.2f, shadow);
                l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);

                shadows.add(l);
            }
        }
    }
    
    public GameMap getParentMap() {
        return this.parentMap;
    }

    public void setParentMap(GameMap parentMap) {
        this.parentMap = parentMap;
    }


    public void setShadow(ColorFilter shadow){
        this.shadow = shadow;
    }
}
