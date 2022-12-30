package summit.game.gamemap;

import summit.game.entity.mob.IceKing;
import summit.game.entity.mob.Player;
import summit.game.tile.IceTile;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;

public class BossRoom extends GameMap{

    public BossRoom(Player player, long seed) {
        super(player, seed, 40, 30);

        super.setFilter(new ColorFilter(-110, -100, -30));
        super.setCamera(new Camera(getWidth()/2, getHeight()/2));
        super.dontSpawnMobs();

        TileStack[][] tiles = super.getTiles();

        //fill with iced snow
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[0].length; c++) {
                IceTile s = new IceTile(c, r);
                s.setColorFilter(new ColorFilter(-50, -50, -50));
                s.setBreakable(Tile.UNBREAKABLE);
                s.setBoundary(true);

                tiles[r][c].pushTile(s);

                if(Math.random() > 0.7){
                    IceTile s_ = new IceTile(c, r);
                    s_.setColorFilter(new ColorFilter(-50, -50, -50));
                    s_.setBreakable(Tile.UNBREAKABLE);
                    s_.setBoundary(true);

                    tiles[r][c].pushTile(s_);
                }
            }
        }

        int roomWidth = 16;
        int roomHeight = 16;

        int sr = tiles.length/2 - roomHeight/2;
        int sc = tiles[0].length/2 - roomWidth/2;
        int er = tiles.length/2 + roomHeight/2;
        int ec = tiles[0].length/2 + roomWidth/2;

        //cut out walkable area
        for (int r = sr; r < er; r++) {
            for (int c = sc; c < ec; c++) {
                tiles[r][c] = new TileStack(c, r);

                StoneTile s = new StoneTile(c, r);
                s.setBreakable(Tile.UNBREAKABLE);

                tiles[r][c].pushTile(s);
            }
        }

        spawn(new IceKing(20, 15));
    }
}
