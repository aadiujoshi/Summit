package summit.gfx;

import summit.game.GameMap;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.util.Direction;

/**
 * Expensive operation for shading raised tiles
 */
public class AmbientOcclusion implements Paintable{

    private float intensity;

    public AmbientOcclusion(float intesity){
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
                if(tiles[r][c] == null || !tiles[r][c].peekTile().isRaisedTile())
                    continue;

                Tile t = tiles[r][c].peekTile();

                String type = t.getName();

                //y is flipped

                if(r+1 < tiles.length && tiles[r+1][c] != null)
                    if(!type.equals(tiles[r+1][c].peekTile().getName()) && !tiles[r+1][c].peekTile().isRaisedTile())
                        ambientShadow(Direction.NORTH, c, r, e);

                if(r-1 > -1 && tiles[r-1][c] != null)
                    if(!type.equals(tiles[r-1][c].peekTile().getName()) && !tiles[r-1][c].peekTile().isRaisedTile())
                        ambientShadow(Direction.SOUTH, c, r, e);

                if(c-1 > -1 && tiles[r][c-1] != null)
                    if(!type.equals(tiles[r][c-1].peekTile().getName()) && !tiles[r][c-1].peekTile().isRaisedTile())
                        ambientShadow(Direction.WEST, c, r, e);

                if(c+1 < tiles[0].length && tiles[r][c+1] != null)
                    if(!type.equals(tiles[r][c+1].peekTile().getName()) && !tiles[r][c+1].peekTile().isRaisedTile())
                        ambientShadow(Direction.EAST, c, r, e);
            }   
        }
    }

    /**
     * x and y are center pos of the tile
     */
    private void ambientShadow(Direction d, int x, int y, PaintEvent e){

    }
}
