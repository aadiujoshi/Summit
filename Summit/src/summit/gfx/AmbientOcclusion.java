/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

import java.awt.Point;
import java.io.Serializable;

import summit.game.gamemap.GameMap;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.util.Direction;
import summit.util.Region;
import summit.util.Settings;

/**
 * Operation for shading tiles and creating depth
 */
public class AmbientOcclusion implements Serializable, Paintable{

    private int intensity;
    private int spread;

    public AmbientOcclusion(){
        setVals();
    }
    
    /**
     * Get ao intensity from settings file
     */
    private void setVals(){
        this.intensity = ((int)Settings.getSetting("ambient_occlusion"))*3;
        this.spread = (int)(intensity/(20/9f));
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        this.setVals();

        if(intensity == 0)
            return;

        ope.addToLayer(RenderLayers.TILE_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {
        TileStack[][] tiles = e.getLoadedMap().tilesInRD(e.getCamera());

        for(int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[0].length; c++) {
                if(tiles[r][c] == null)
                    continue;
                    
                if(tiles[r][c].topTile().getLight() != Light.NO_LIGHT)
                    continue;

                Tile t = tiles[r][c].topTile();
                
                ambientShadow(t, e);
            }   
        }
    }

    /**
     * shade the valid area AROUND this tile
     */
    private void ambientShadow(Tile tile, PaintEvent e){

        int gx = (int)tile.getX();
        int gy = (int)tile.getY();

        Point p = Renderer.toPixel(gx, gy, e.getCamera());
        GameMap map = e.getLoadedMap();
        Renderer ren = e.getRenderer();

        //relative to the center tile
        Tile t_left = map.getTileAt(gx-1, gy);
        Tile t_right = map.getTileAt(gx+1, gy);
        Tile t_up = map.getTileAt(gx, gy+1);
        Tile t_down = map.getTileAt(gx, gy-1);

        int comp_depth = tile.getDepth();

        TileStack[][] tiles = map.getTiles();

        //------------------  SHADE SIDES  ----------------------------------------------------

        int ind = 0;

        for (int ty = gy-1; ty <= gy+1; ty++) {
            for (int tx = gx-1; tx <= gx+1; tx++, ind++) {
                if(!Renderer.inArrBounds(ty, tx, tiles.length, tiles[0].length) || 
                    tiles[ty][tx] == null || 
                    (tx == gx && ty == gy)){

                    continue;
                }
                
                Tile borderTile = map.getTileAt(tx, ty);

                if(borderTile.getDepth() == comp_depth){
                    continue;
                }

                //------------  sides  -----------------------
                //south
                if(ind == 1){
                    if(t_down != null && t_down.getDepth() != comp_depth){
                        int s = (int)(p.y+spread+8);

                        for (int y = s; y > p.y+7; y--) {
                            int val = (int)(-intensity*((s-y)/(spread*1f)));

                            ren.filterRect(p.x-7, y, 16, 1, new ColorFilter(val, val, val));
                        }
                    }
                }

                //west
                if(ind == 3){
                    if(t_left != null && t_left.getDepth() != comp_depth){
                        int s = (int)(p.x-spread-8);
                        for (int x = s; x < p.x-7; x++) {

                            int val = (int)(-intensity*((x-s)/(spread*1f)));

                            ren.filterRect(x, p.y-8, 1, 16, new ColorFilter(val, val, val));
                        }
                    }
                }

                //east
                if(ind == 5){
                    if(t_right != null && t_right.getDepth() != comp_depth){
                        int s = (int)(p.x+spread+8);

                        for (int x = s; x > p.x+8; x--) {
                            int val = (int)(-intensity*((s-x)/(spread*1f)));

                            ren.filterRect(x, p.y-8, 1, 16, new ColorFilter(val, val, val));
                        }
                    }
                }

                //north
                if(ind == 7){
                    if(t_up != null && t_up.getDepth() != comp_depth){
                        int s = (int)(p.y-spread-8);

                        for (int y = s; y < p.y-8; y++) {
                            int val = (int)(-intensity*((y-s)/(spread*1f)));

                            ren.filterRect(p.x-7, y, 16, 1, new ColorFilter(val, val, val));
                        }
                    }
                }
                

                //------------  corners  -----------------------
                
                //current comparing tile depth
                int comp = map.getTileAt(tx, ty).getDepth();

                int[][] frame = e.getRenderer().getFrame();

                //south west
                if(ind == 0){
                    if(t_left != null && t_down != null){
                        if(t_left.getDepth() == comp && t_down.getDepth() == comp){
                            drawQuadrant(Direction.SW, p.x-8, p.y+8, frame);
                        }
                    }
                }
                
                //south east
                if(ind == 2){
                    if(t_right != null && t_down != null){
                        if(t_right.getDepth() == comp && t_down.getDepth() == comp){
                            drawQuadrant(Direction.SE, p.x+9, p.y+8, frame);
                        }
                    }
                }

                //north west
                if(ind == 6){
                    if(t_left != null && t_up != null){
                        if(t_left.getDepth() == comp && t_up.getDepth() == comp){
                            drawQuadrant(Direction.NW, p.x-8, p.y-9, frame);
                        }
                    }
                }

                //north east
                if(ind == 8){
                    if(t_right != null && t_up != null){
                        if(t_right.getDepth() == comp && t_up.getDepth() == comp){
                            drawQuadrant(Direction.NE, p.x+9, p.y-9, frame);
                        }
                    }
                }
            }
        }
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
