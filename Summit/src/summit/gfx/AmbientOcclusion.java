package summit.gfx;

import java.awt.Point;

import summit.game.GameMap;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.util.Direction;
import summit.util.Region;

/**
 * Expensive operation for shading raised tiles
 */
public class AmbientOcclusion implements Paintable{

    private int intensity;
    private int spread;

    public AmbientOcclusion(int intesity){
        this.intensity = intesity;
        this.spread = (int)(intesity/(20/9f));
        // this.spread = 7;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.addToLayer(RenderLayers.TILE_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {
        //NOT READY YET
        // if(true) return;

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
                                ambientShadow(Direction.SW, t, e);

                            if(r1 == r-1 && c1 == c)
                                ambientShadow(Direction.SOUTH, t, e);

                            if(r1 == r-1 && c1 == c+1)
                                ambientShadow(Direction.SE, t, e);

                            if(r1 == r && c1 == c-1)
                                ambientShadow(Direction.WEST, t, e);

                            if(r1 == r && c1 == c+1)
                                ambientShadow(Direction.EAST, t, e);

                            if(r1 == r+1 && c1 == c-1)
                                ambientShadow(Direction.NW, t, e);
                            
                            if(r1 == r+1 && c1 == c)
                                ambientShadow(Direction.NORTH, t, e);

                            if(r1 == r+1 && c1 == c+1)
                                ambientShadow(Direction.NE, t, e);
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
    private void ambientShadow(Direction d, Tile t, PaintEvent e){

        int gx = (int)t.getX();
        int gy = (int)t.getY();

        Point p = Renderer.toPixel(gx, gy, e.getCamera());
        GameMap map = e.getLoadedMap();
        Renderer r = e.getRenderer();

        if(d == Direction.WEST){
            int s = (int)(p.x-spread-8);
            for (int x = s; x < p.x-7; x++) {

                int val = (int)(-intensity*((x-s)/(spread*1f)));

                r.filterRect(x, p.y-8, 1, 16, new ColorFilter(val, val, val));
            }
            return;
        }
        if(d == Direction.EAST){
            int s = (int)(p.x+spread+8);

            for (int x = s; x > p.x+8; x--) {
                int val = (int)(-intensity*((s-x)/(spread*1f)));

                r.filterRect(x, p.y-8, 1, 16, new ColorFilter(val, val, val));
            }

            return;
        }
        if(d == Direction.NORTH){
            int s = (int)(p.y-spread-8);

            for (int y = s; y < p.y-8; y++) {
                int val = (int)(-intensity*((y-s)/(spread*1f)));

                r.filterRect(p.x-7, y, 16, 1, new ColorFilter(val, val, val));
            }

            return;
        }
        if(d == Direction.SOUTH){
            int s = (int)(p.y+spread+8);

            for (int y = s; y > p.y+7; y--) {
                int val = (int)(-intensity*((s-y)/(spread*1f)));

                r.filterRect(p.x-7, y, 16, 1, new ColorFilter(val, val, val));
            }

            return;
        }

        int[][] frame = e.getRenderer().getFrame();

        if(d == Direction.NW){
            if(validCorner(d, t, map)){
                drawQuadrant(d, p.x-8, p.y-9, frame);
            }
        }
        if(d == Direction.NE){
            if(validCorner(d, t, map)){
                drawQuadrant(d, p.x+9, p.y-9, frame);
            }
        }
        if(d == Direction.SW){
            if(validCorner(d, t, map)){
                drawQuadrant(d, p.x-8, p.y+8, frame);
            }
        }
        if(d == Direction.SE){
            if(validCorner(d, t, map)){
                drawQuadrant(d, p.x+9, p.y+8, frame);
            }
        }
    }

    /*
     * 
     * S = shaded tile
     * E = enclosing tile
     * R = relative tile (determine direction)
     * 
     * NW:
     * [S][E]
     * [E][R]
     * 
     * 
     */
    private boolean validCorner(Direction d, Tile rel, GameMap map){
        if(true) return false;

        int rx = (int)rel.getX();
        int ry = (int)rel.getY();

        Tile t_left = map.getTileAt(rx-1, ry);
        Tile t_right = map.getTileAt(rx+1, ry);
        Tile t_up = map.getTileAt(rx, ry+1);
        Tile t_down = map.getTileAt(rx, ry-1);

        if(d == Direction.NW){
            if(t_left != null && t_up != null && (t_left.getName().equals(t_up.getName()))){
                return true;
            }
        }   
        if(d == Direction.NE){
            if(t_right != null && t_up != null && (t_right.getName().equals(t_up.getName()))){
                return true;
            }
        }
        if(d == Direction.SW){
            if(t_right != null && t_down != null && (t_right.getName().equals(t_down.getName()))){
                return true;
            }
        }
        if(d == Direction.SE){
            if(t_left != null && t_down != null && (t_left.getName().equals(t_down.getName()))){
                return true;
            }
        }

        return false;
    }

    //draw the corners of the ambient shadow
    private void drawQuadrant(Direction dir, int cx, int cy, int[][] frame){
        
        for(int xx = (int)(cx-spread); xx < (int)(cx+spread); xx++){
            for (int yy = (int)(cy-spread); yy < (int)(cy+spread); yy++) {
                if(!Renderer.inArrBounds(yy, xx, frame.length, frame[0].length))
                    continue;

                if(dir == Direction.NW)
                    if(xx > cx || yy > cy)
                        continue;
                if(dir == Direction.NE)
                    if(xx < cx || yy > cy)
                        continue;
                if(dir == Direction.SW)
                    if(xx > cx || yy < cy)
                        continue;
                if(dir == Direction.SE)
                    if(xx < cx || yy < cy)
                        continue;

                float d = Region.distance(cx, cy, xx, yy);

                if(d <= spread){
                    int val = (int)( -(intensity) -((d/spread)*(-(intensity))));

                    ColorFilter filt = new ColorFilter(val, val, val);
                    frame[yy][xx] = filt.filterColor(frame[yy][xx]);
                }
            }
        }
    }
}
