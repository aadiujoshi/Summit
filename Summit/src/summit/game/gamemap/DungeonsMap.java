package summit.game.gamemap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import summit.game.entity.mob.Player;
import summit.game.item.BlueKey;
import summit.game.item.GreenKey;
import summit.game.item.Item;
import summit.game.structure.BossRoomEntrance;
import summit.game.structure.ItemChest;
import summit.game.structure.RaisedStone;
import summit.game.tile.EmptyTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;
import summit.gfx.ColorFilter;
import summit.util.ScheduledEvent;
import summit.util.Sound;
import summit.util.Time;

public class DungeonsMap extends GameMap{

    private ScheduledEvent ambientSounds;

    public DungeonsMap(Player player, long seed) {
        super(player, seed, 128, 128);

        super.setFilter(new ColorFilter(-100, -100, -100));
        super.setRenderDist(10);

        int width = getWidth();
        int height = getHeight();

        boolean[][] genMap = new boolean[height][width];
        TileStack[][] tiles = super.getTiles();

        Random rand = new Random(seed);

        int sx = width/2;
        int sy = height/2;

        gen(rand, sx, sy, (Object)0, genMap);
        gen(rand, sx, sy, (Object)0, genMap);
        gen(rand, sx, sy, (Object)0, genMap);

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if(genMap[y][x]){
                    tiles[y][x].pushTile(new StoneTile(x, y));
                }
                else {
                    tiles[y][x].pushTile(new EmptyTile(x, y));
                    tiles[y][x].pushTile(new EmptyTile(x, y));
                }
            }
        }

        //find places to spawn chests
        ArrayList<Point> chest_locs = new ArrayList<>();

        for (int y = 1; y < tiles.length-1; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if(tiles[y][x].topTile().isBoundary() && !tiles[y-1][x].topTile().isBoundary()){

                    //5 percent chance that a chest spawns at this point
                    if(rand.nextDouble() < 0.05){
                        chest_locs.add(new Point(x,y-1));
                    }
                }
            }
        }

        //spawn the chests
        //guaranteed chance of 2 keys 
        int key1index = rand.nextInt(chest_locs.size());
        int key2index = rand.nextInt(chest_locs.size());

        //make sure keys are in different chests
        while(key2index == key1index){
            key2index = rand.nextInt(chest_locs.size());
        }
        
        int i = 0;
        for (Point point : chest_locs) {
            Item k = null;
            if(i == key1index)
                k = new BlueKey(null);
            if(i == key2index)
                k = new GreenKey(null);

            addStructure(new ItemChest(point.x, point.y, k, this));
            i++;
        }

        for (int j = 0; j < tiles.length; j++) {
            for (int j2 = 0; j2 < tiles[0].length; j2++) {
                if(tiles[j][j2].topTile().isBoundary() && !chest_locs.contains(new Point(j2, j))){
                    addStructure(new RaisedStone(j2, j, this));
                }
            }
        }



        //find 4x4 space under wall to put boss room entrance
        //"b" is boundary
        //"o" is open
        //[b][b]
        //[o][o] <- spawn here
        //[o][o]
        
        ArrayList<Point> door_locs = new ArrayList<>();

        for (int r = 1; r < tiles.length-1; r++) {
            for (int c = 0; c < tiles[0].length-1; c++) {
                //check top
                if(tiles[r+1][c].topTile().isBoundary() && tiles[r+1][c+1].topTile().isBoundary()){
                    //check this row
                    if(!tiles[r][c].topTile().isBoundary() && !tiles[r][c+1].topTile().isBoundary()){
                        //check bottom row
                        if(!tiles[r-1][c].topTile().isBoundary() && !tiles[r-1][c+1].topTile().isBoundary()){
                            door_locs.add(new Point(c,r));
                        }
                    }
                }
            }
        }

        Point doorCoord = door_locs.get(rand.nextInt(door_locs.size()));

        System.out.println(doorCoord);

        addStructure(new BossRoomEntrance(doorCoord.x+0.5f, doorCoord.y+0.5f, this));
    }

    private boolean[][] gen(Random rand, int x, int y, Object iterations, boolean[][] tiles){
        if((int)iterations >= 4000)
            return tiles;
        
        iterations = (int)iterations + 1;
        tiles[y][x] = true;
        double c = rand.nextInt(4);
        //left
        if(c == 0){
            if(x-1 > -1){
                gen(rand, x-1, y, iterations, tiles);
            }
        }
        //up
        if(c == 1){
            if(y-1 > -1){
                gen(rand, x, y-1, iterations, tiles);
            }
        }
        //down
        if(c == 2){
            if(y+1 < tiles.length){
                gen(rand, x, y+1, iterations, tiles);
            }
        }
        //right
        if(c == 3){
            if(x+1 < tiles[0].length){
                gen(rand, x+1, y, iterations, tiles);
            }
        }
        return tiles;
    }

    @Override
    public void setLoaded(boolean b){
        super.setLoaded(b);

        if(b){
            Sound.DUNGEON_SOUNDS.play();
            this.ambientSounds = new ScheduledEvent(Time.MS_IN_S*(60*5), ScheduledEvent.FOREVER) {
                @Override
                public void run() {
                    Sound.DUNGEON_SOUNDS.play();
                }
            };
        }
        if(!b){
            Sound.DUNGEON_SOUNDS.stop();
            this.ambientSounds = null;
        }
    }
}
