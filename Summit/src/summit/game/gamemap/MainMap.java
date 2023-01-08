package summit.game.gamemap;

import java.util.Random;

import summit.game.GameUpdateEvent;
import summit.game.animation.ForegroundAnimation;
import summit.game.entity.mob.Player;
import summit.game.entity.mob.Skeleton;
import summit.game.gamemap.mapgenerator.OpenSimplexNoise;
import summit.game.structure.DungeonsEntrance;
import summit.game.structure.TraderHouse;
import summit.game.structure.Tree;
import summit.game.tile.GrassTile;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;
import summit.game.tile.WaterTile;
import summit.gfx.ColorFilter;
import summit.gfx.Renderer;

public class MainMap extends GameMap{

    public MainMap(Player player, long seed) {
        super(player, seed, 128, 128);
        
        super.setAnimation(new ForegroundAnimation(4, 3, Renderer.toIntRGB(200, 200, 250)));
        super.setFilter(new ColorFilter(-60, -50, 0));
        // super.setFilter(new ColorFilter(0, 0, -50));
        // super.setFilter(new ColorFilter(0x00B16A));

        int width = getWidth();
        int height = getHeight();

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
        
        //just for testing
        spawn(new Skeleton(60, 60));

        addStructure(new TraderHouse(19.5f, 19f, this));



        addStructure(new DungeonsEntrance( 
                            getWidth()/2+0.5f, 
                            getHeight()/2-3+0.5f, 
                            this));
    }

    @Override
    public void update(GameUpdateEvent e) throws Exception{
        super.update(e);
    }

    private void iceTiles(){
        
        TileStack[][] tiles = getTiles();

        int iced = 0;

        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[0].length; c++) {
                if(tiles[r][c].topTile().getName().equals("SnowTile")){
                    iced++;
                    continue;
                }

                if(Math.random() < 0.00001)
                    tiles[r][c].topTile().setIced(true);
            }
        }
        
        if(iced == getWidth()*getHeight())
            System.out.println("GAME OVER");
    }
}
