package summit.game.structure;

import java.util.ArrayList;

import summit.game.GameClickReciever;
import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameRegion;
import summit.util.Region;

public abstract class Structure extends GameRegion implements GameUpdateReciever {

    private ColorFilter shadow = new ColorFilter(-100, -100, -100);

    private ArrayList<Light> shadows;

    public Structure(float x, float y, float width, float height, GameMap parentMap) {
        super(x, y, width, height);
        this.situate(parentMap);
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER, this);
        for (Light light : shadows) {
            light.setRenderLayer(ope);
        }
    }

    @Override
    public void update(GameUpdateEvent e) {
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

                Tile t = tiles[y1][x1].peekTile();
                t.setBoundary(true);

                Light l = new Light(x1, y1, 1.2f, shadow);
                l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);

                shadows.add(l);
            }
        }
    }

    public void setShadow(ColorFilter shadow){
        this.shadow = shadow;
    }
}
