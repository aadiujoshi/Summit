package summit.gfx;

import java.awt.Point;

import summit.game.GameMap;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.util.Direction;

/**
 * Expensive operation for shading raised tiles
 */
public class AmbientOcclusion implements Paintable{

    private int intensity;

    public AmbientOcclusion(int intesity){
        this.intensity = intesity;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.addToLayer(RenderLayers.TILE_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {
        TileStack[][] tiles = e.getLoadedMap().tilesInRD(e.getCamera());

        for(int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[0].length; c++) {
                if(tiles[r][c] == null)
                    continue;

                Tile t = tiles[r][c].peekTile();

                String type = t.getName();
                int d = t.getDepth();

                for(int r1 = r-1; r1 < tiles.length; r1++) {
                    for (int c1 = c-1; c1 < tiles[0].length; c1++) {
                        if(!Renderer.inArrBounds(r1, c1, tiles.length, tiles[0].length) || tiles[r1][c1] == null || (r1 == r && c1 == c))
                            continue;
                        
                        Tile t1 = tiles[r1][c1].peekTile();

                        if(!t1.getName().equals(type) && t1.getDepth() != d){
                            if(r1 == r-1 && c1 == c-1)
                                ambientShadow(Direction.SW, r1, c1, e);

                            if(r1 == r-1 && c1 == c)
                                ambientShadow(Direction.SOUTH, (int)t.getX(), (int)t.getY(), e);

                            if(r1 == r-1 && c1 == c+1)
                                ambientShadow(Direction.SE, (int)t.getX(), (int)t.getY(), e);

                            if(r1 == r && c1 == c-1)
                                ambientShadow(Direction.WEST, (int)t.getX(), (int)t.getY(), e);

                            if(r1 == r && c1 == c+1)
                                ambientShadow(Direction.EAST, (int)t.getX(), (int)t.getY(), e);

                            if(r1 == r+1 && c1 == c-1)
                                ambientShadow(Direction.NW, (int)t.getX(), (int)t.getY(), e);
                            
                            if(r1 == r+1 && c1 == c)
                                ambientShadow(Direction.NORTH, (int)t.getX(), (int)t.getY(), e);

                            if(r1 == r+1 && c1 == c+1)
                                ambientShadow(Direction.NE, (int)t.getX(), (int)t.getY(), e);
                        }
                    }
                }
            }   
        }
    }

    /**
     * x and y are center pos of the tile 
     * </p>
     * direction is relative to the tile
     */
    private void ambientShadow(Direction d, int gx, int gy, PaintEvent e){
        Point p = Renderer.toPixel(gx, gy, e.getCamera());
        GameMap map = e.getLoadedMap();
        float spread = 10;

        Renderer r = e.getRenderer();

        if(d == Direction.WEST){
            int s = (int)(p.x-spread-8);
            for (int x = s; x < p.x-7; x++) {

                int val = (int)(-intensity*((x-s)/spread));

                r.filterRect(x, p.y-8, 1, 16, new ColorFilter(val, val, val));
            }
        }
        if(d == Direction.EAST){
            int s = (int)(p.x+spread+8);

            for (int x = s; x > p.x+8; x--) {
                int val = (int)(-intensity*((s-x)/spread));

                r.filterRect(x, p.y-8, 1, 16, new ColorFilter(val, val, val));
            }
        }
        if(d == Direction.NORTH){
            int s = (int)(p.y-spread-8);

            for (int y = s; y < p.y-8; y++) {
                int val = (int)(-intensity*((y-s)/spread));

                r.filterRect(p.x-7, y, 16, 1, new ColorFilter(val, val, val));
            }
        }
        if(d == Direction.SOUTH){
            int s = (int)(p.y+spread+8);

            for (int y = s; y > p.y+7; y--) {
                int val = (int)(-intensity*((s-y)/spread));

                r.filterRect(p.x-7, y, 16, 1, new ColorFilter(val, val, val));
            }
        }

        // if(d == Direction.NW){
        //     //check if enclosed
        //     if(map.getTileAt(gx, gy))
        // }
    }
}
