package summit.game;

import java.util.ArrayList;

import summit.game.entity.Entity;
import summit.game.entity.mob.PlayerEntity;
import summit.game.structure.Structure;
import summit.game.structure.TraderHouse;
import summit.game.tile.TileStack;
import summit.gfx.Camera;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;
import summit.util.Region;

public class GameMap implements Paintable, GameUpdateReciever, GameClickReciever{

    private ArrayList<Entity> entities;
    private ArrayList<Structure> structures;

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
        structures = new ArrayList<>();
        structures.add(new TraderHouse(19.5f, 19f));
        this.NAME = name;
        this.SEED = seed;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    @Override
    public void gameClick(GameClickEvent e) {
        for(Structure r_struct : structures) {
            if(r_struct.contains(e.gameX(), e.gameY())){
                r_struct.gameClick(e);
                return;
            }
        }
        for(Entity entity : entities) {
            if(entity.contains(e.gameX(), e.gameY())){
                entity.gameClick(e);
                return;
            }
        }

        getTileAt(e.gameX(), e.gameY()).gameClick(e);

    }

    @Override
    public void update(GameUpdateEvent e) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j].update(e);
            }
        }

        for (Entity entity : entities) {
            entity.update(e);
        }
        for (Structure h : structures) {
            h.update(e);
        }
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        Camera c = e.getCamera();
        int nx = Math.round(c.getX());
        int ny = Math.round(c.getY());

        //range of tiles to display
        int rwidth = (Renderer.WIDTH/16)+3;
        int rheight = (Renderer.HEIGHT/16)+3;

        for(int i = nx-rwidth/2; i < nx+rwidth/2 && i < map.length; i++){
            for(int j = ny-rheight/2; j < ny+rheight/2 && j < map[0].length; j++){
                if(i > -1 && j > -1)
                    map[j][i].setRenderLayer(e);
            }
        }

        ArrayList<Region> sorted = new ArrayList<>();

        for (Entity entity : entities) {
            sorted.add(entity);
        }
        for (Structure s : structures) {
            sorted.add(s);
        }

        for (int i = 0; i < sorted.size(); i++) {
            int lowestInd = i;

            for (int j = i+1; j < sorted.size(); j++) {
                float y1 = sorted.get(lowestInd).getY()-sorted.get(lowestInd).getHeight()/2;
                float y2 = sorted.get(j).getY() - sorted.get(j).getHeight()/2;

                if(y2 > y1)
                    lowestInd = j;
            }

            Region temp = sorted.get(i);
            sorted.set(i, sorted.get(lowestInd));
            sorted.set(lowestInd, temp);
        }

        for (Region r: sorted) {
            ((Paintable)r).setRenderLayer(e);
        }
    }

    @Override
    public void paint(PaintEvent e) {
        
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

    public TileStack[][] getTiles() {
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
        entities.add(player);
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

    public void spawn(Entity e){
        entities.add(e);
    }
}
