package summit.game;

import java.util.ArrayList;
import java.util.List;

import summit.game.entity.Entity;
import summit.game.entity.mob.PlayerEntity;
import summit.game.tile.TileStack;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;

public class GameMap implements Paintable, GameUpdateReciever{
    
    private List<Entity> entities;

    private PlayerEntity player;

    private TileStack[][] map;
    private final int WIDTH;
    private final int HEIGHT;
    private final String NAME;

    private final long SEED;

    private boolean loaded;

    public GameMap(String name, final long seed, final int width, final int height) {
        map = new TileStack[height][width];
        entities = new ArrayList<>();
        this.NAME = name;
        this.SEED = seed;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    @Override
    public void update(GameUpdateEvent e) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j].update(e);
            }
        }
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update(e);
        }
    }

    @Override
    public void paint(PaintEvent e) {
        Camera c = e.getCamera();
        int nx = Math.round(c.getX());
        int ny = Math.round(c.getY());

        //range of tiles to display
        int rwidth = (Renderer.WIDTH/16)+3;
        int rheight = (Renderer.HEIGHT/16)+3;

        for(int i = nx-rwidth/2; i < nx+rwidth/2 && i < map.length; i++){
            for(int j = ny-rheight/2; j < ny+rheight/2 && j < map[0].length; j++){
                if(i > -1 && j > -1)
                    map[j][i].paint(e);
            }
        }

        
    }

    //--------------------------------------------------------------------
    // getters and setters
    //--------------------------------------------------------------------
    
    public TileStack getTileAt(float x, float y){
        //out of bounds
        if(Math.round(y) < 0 || Math.round(y) >= map.length ||
            Math.round(x) < 0 || Math.round(x) > map[0].length)
            return null;
        return map[Math.round(y)][Math.round(x)];
    }

    public TileStack[][] getMap() {
        return this.map;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
    
    public String getName(){
        return this.NAME;
    }

    public int getWidth(){
        return this.WIDTH;
    }

    public int getHeight(){
        return this.HEIGHT;
    }

    public long getSeed(){
        return this.SEED;
    }

}
