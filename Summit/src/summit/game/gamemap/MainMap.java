package summit.game.gamemap;

import java.util.Random;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.mapgenerator.OpenSimplexNoise;
import summit.game.structure.TraderHouse;
import summit.game.structure.Tree;
import summit.game.tile.GrassTile;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;
import summit.game.tile.WaterTile;
import summit.gfx.ColorFilter;

public class MainMap extends GameMap{

    public MainMap(long seed) {
        super(seed, 128, 128);
        
        int width = 128;
        int height = 128;

        OpenSimplexNoise gen = new OpenSimplexNoise(seed);  
        Random random = new Random(seed);

        double[][] heightMap = new double[width][height];
        
        TileStack[][] tiles = super.getTiles();

        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                tiles[i][j].pushTile(new StoneTile(j, i));
                heightMap[i][j] = gen.eval(j/10.0, i/10.0);
            }
        }

        //base tiles
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                double val = heightMap[i][j];
                TileStack t = tiles[i][j];
                if(val < 0){
                    if(val < -0.5){
                        t.pushTile(new WaterTile(j, i));
                    }
                } else if(val > 0){
                    t.pushTile(new GrassTile(j, i));
                    if(val > 0.3)
                        t.pushTile(new SnowTile(j, i));
                }
            }
        }

        //trees!!!
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                double val = gen.eval(j/10.0, i/10.0);;
                
                if(val > 0.6){
                    if(random.nextInt(4) == 0)
                        spawn(new Tree(j, i));
                }
            }
        }

        addStructure(new TraderHouse(19.5f, 19f, this));
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);
    }
}
