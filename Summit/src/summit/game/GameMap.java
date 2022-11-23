package summit.game;

import java.util.ArrayList;

import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.game.structure.Structure;
import summit.game.structure.TraderHouse;
import summit.game.tile.TileStack;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.Region;

public class GameMap implements Paintable, GameUpdateReciever, GameClickReciever{

    private ArrayList<Entity> entities;
    private ArrayList<Structure> structures;

    private Player player;

    private ColorFilter filter;
    private Paintable animation;

    private TileStack[][] tiles;
    private final int WIDTH;
    private final int HEIGHT;
    private final String NAME;

    private final long SEED;

    private boolean loaded;

    public GameMap(String name, final long seed, final int width, final int height) {
        tiles = new TileStack[height][width];
        //-----------   init map   --------------------------------------------
        for (int x = 0; x < tiles[0].length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                tiles[y][x] = new TileStack(x, y);
            }
        }
        //---------------------------------------------------------------------
        entities = new ArrayList<>();
        structures = new ArrayList<>();
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
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].update(e);
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
        if(animation != null)
            animation.setRenderLayer(e);
            
        e.addToLayer(RenderLayers.TOP_LAYER, this);

        Camera c = e.getCamera();
        int nx = Math.round(c.getX());
        int ny = Math.round(c.getY());

        //range of tiles to display
        int rwidth = (Renderer.WIDTH/16)+3;
        int rheight = (Renderer.HEIGHT/16)+3;

        for(int i = nx-rwidth/2; i < nx+rwidth/2 && i < tiles.length; i++){
            for(int j = ny-rheight/2; j < ny+rheight/2 && j < tiles[0].length; j++){
                if(i > -1 && j > -1)
                    tiles[j][i].setRenderLayer(e);
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
        e.getRenderer().filterRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT, filter);
    }

    //--------------------------------------------------------------------
    // getters and setters
    //--------------------------------------------------------------------
    
    public void addStructure(Structure s){
        this.structures.add(s);
    }

    public void removeStructure(Structure s){
        this.structures.remove(s);
    }

    public TileStack getTileAt(float x, float y){
        //out of bounds
        if(Math.round(y) < 0 || Math.round(y) >= tiles.length ||
            Math.round(x) < 0 || Math.round(x) > tiles[0].length)
            return null;
        return tiles[Math.round(y)][Math.round(x)];
    }

    public TileStack[][] getTiles() {
        return this.tiles;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
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

    public void remove(Entity e){
        entities.remove(e);
    }

    public void spawn(Entity e){
        entities.add(e);
    }
    
    public ColorFilter getFilter() {
        return this.filter;
    }

    public void setFilter(ColorFilter filter) {
        this.filter = filter;
    }

    public Paintable getAnimation() {
        return this.animation;
    }

    public void setAnimation(Paintable animation) {
        this.animation = animation;
    }
}
