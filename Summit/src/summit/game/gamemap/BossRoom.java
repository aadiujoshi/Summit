package summit.game.gamemap;

import summit.game.entity.mob.Player;
import summit.game.tile.IceTile;
import summit.game.tile.SnowTile;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;

public class BossRoom extends GameMap{

    public BossRoom(Player player, long seed) {
        super(player, seed, 40, 30);

        TileStack[][] tiles = super.getTiles();

        //fill with iced snow
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[0].length; c++) {
                IceTile s = new IceTile(c, r);
                s.setBreakable(Tile.UNBREAKABLE);
                s.setBoundary(true);

                tiles[r][c].pushTile(s);

                if(Math.random() > 0.7){
                    IceTile s_ = new IceTile(c, r);
                    s_.setBreakable(Tile.UNBREAKABLE);
                    s_.setBoundary(true);

                    tiles[r][c].pushTile(s_);
                }
            }
        }

        int roomWidth = 10;
        int roomHeight = 10;

        int sr = tiles.length/2 - roomHeight/2;
        int sc = tiles[0].length/2 - roomWidth/2;
        int er = tiles.length/2 + roomHeight/2;
        int ec = tiles[0].length/2 + roomWidth/2;

        System.out.println(sr + "  " + sc + "  "+ er + "  " + ec);

        //cut out walkable area
        for (int r = sr; r < er; r++) {
            for (int c = sc; c < ec; c++) {
                tiles[r][c] = new TileStack(c, r);

                SnowTile s = new SnowTile(c, r);
                s.setBreakable(Tile.UNBREAKABLE);

                tiles[r][c].pushTile(s);
            }
        }
    }
}
