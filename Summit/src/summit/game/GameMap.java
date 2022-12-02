/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import summit.game.animation.ForegroundAnimation;
import summit.game.animation.ParticleAnimation;
import summit.game.animation.Scheduler;
import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.game.structure.Structure;
import summit.game.structure.TraderHouse;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.gfx.AmbientOcclusion;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameRegion;
import summit.util.Region;

public class GameMap implements Serializable, Paintable, GameUpdateReciever, GameClickReciever{

    private ArrayList<Entity> spawnQueue;

    private ArrayList<Entity> entities;
    private ArrayList<Structure> structures;

    //handles broken block animations
    //assumes NOT registered into scheduler
    private Vector<ParticleAnimation> particleAnimations;

    //player
    private Player player;

    //render distance for entitys and structures
    private int rd_x = 20;
    private int rd_y = 20;

    //stores the most recent of the player; used for when transitioning GameMaps
    private Camera camera = new Camera(0, 0);

    private ColorFilter filter;
    private ForegroundAnimation animation;
    private AmbientOcclusion ambientOcclusion;

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
        spawnQueue = new ArrayList<>();
        this.NAME = name;
        this.SEED = seed;
        this.WIDTH = width;
        this.HEIGHT = height;

        this.particleAnimations = new Vector<>();
        this.ambientOcclusion = new AmbientOcclusion(25);
    }

    //saved world
    public void reinit(){
        this.animation.reinit();
        for (ParticleAnimation pa : particleAnimations) {
            pa.reinit();
        }
        for (Entity e : entities) {
            e.reinit();
        }
        for (Structure s : structures) {
            s.reinit();
        }
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        if(e.gameX() > getWidth() || e.gameX() < 0 ||
            e.gameY() > getHeight() || e.gameY() < 0)
                return;

        for(Structure r_struct : structures) {
            if(r_struct.contains(e.gameX(), e.gameY()) && 
                Region.distance(r_struct.getX(), r_struct.getY(), player.getX(), player.getY()) <= 3.5){
                r_struct.gameClick(e);
                return;
            }
        }
        for(Entity entity : entities) {
            if(entity.contains(e.gameX(), e.gameY()) && 
                Region.distance(entity.getX(), entity.getY(), player.getX(), player.getY()) <= 3.5){
                entity.gameClick(e);
                return;
            }
        }
        if(Region.distance(player.getX(), player.getY(), e.gameX(), e.gameY()) <= 3.5)
            getTileStackAt(e.gameX(), e.gameY()).gameClick(e);
    }

    @Override
    public void update(GameUpdateEvent e) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].update(e);
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            Entity et = entities.get(i);
            et.update(e);
            if(entities.get(i).destroyed()){
                et.destroy(e);
                entities.remove(i);
                i--;
            }
        }
        for (Structure h : structures) {
            h.update(e);
        }

        for (Entity spawn : spawnQueue) {
            entities.add(spawn);
        }

        spawnQueue.clear();
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        //general animation (snowfall)
        if(animation != null)
            animation.setRenderLayer(e);
        
        //e
        for (int i = 0; i < particleAnimations.size(); i++) {
            particleAnimations.get(i).setRenderLayer(e);

            if(particleAnimations.get(i).terminate()){
                particleAnimations.remove(i);
                i--;
                continue;
            }
        }

        //tiles in render distance (from cameraa position)
        TileStack[][] rdTiles = this.tilesInRD(e.getCamera());

        for (int i = 0; i < rdTiles.length; i++) {
            for (int j = 0; j < rdTiles[0].length; j++) {
                if(rdTiles[i][j] != null)
                    rdTiles[i][j].setRenderLayer(e);
            }
        }

        //tile ambient occlusion
        ambientOcclusion.setRenderLayer(e);

        //sort GameRegions (entities and structures) from bottom to up 
        ArrayList<Region> sorted = new ArrayList<>();

        for (Entity entity : entities) {
            if(Region.distance(entity.getX(), entity.getY(), camera.getX(), camera.getY()) <= rd_x/2f)
                sorted.add(entity);
        }
        for (Structure s : structures) {
            if(Region.distance(s.getX(), s.getY(), camera.getX(), camera.getY()) <= rd_y/2f)
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

        //sorted GameRegions
        for (Region r: sorted) {
            ((Paintable)r).setRenderLayer(e);
        }
        
        //map filter
        e.addToLayer(RenderLayers.TOP_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().filterRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT, filter);
    }

    //--------------------------------------------------------------------
    // getters and setters
    //--------------------------------------------------------------------
    
    //returns
    public GameRegion getContact(GameRegion r){
        for (Structure s : structures) {
            if(r.overlap(s))
                return s;
        }
        for (Entity e : entities) {
            if(r.overlap(e))
                return e;
        }
        
        return null;
    }

    public TileStack[][] tilesInRD(Camera c){
        int nx = Math.round(c.getX());
        int ny = Math.round(c.getY());

        //range of tiles to display
        int rwidth = (Renderer.WIDTH/16)+3;
        int rheight = (Renderer.HEIGHT/16)+3;

        TileStack[][] rdTiles = new TileStack[rheight][rwidth];

        for(int i = nx-rwidth/2; i < nx+rwidth/2 && i < tiles.length; i++){
            for(int j = ny-rheight/2; j < ny+rheight/2 && j < tiles[0].length; j++){
                if(i > -1 && j > -1)
                    rdTiles[j-(ny-rheight/2)][i-(nx-rwidth/2)] = tiles[j][i];
            }
        }

        return rdTiles;
    }

    public void addStructure(Structure s){
        this.structures.add(s);
    }

    public void removeStructure(Structure s){
        this.structures.remove(s);
    }

    public Tile getTileAt(float x, float y){
        TileStack ts = getTileStackAt(x, y);
        return (ts == null) ? null : ts.peekTile();
    }

    public TileStack getTileStackAt(float x, float y){
        //out of bounds
        if(Math.round(y) < 0 || Math.round(y) >= tiles.length ||
            Math.round(x) < 0 || Math.round(x) >= tiles[0].length)
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
        player.setCamera(this.camera);
        if(this.player == null)
            entities.add(player);
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

    public void addParticleAnimation(ParticleAnimation pa){
        particleAnimations.add(pa);
    }

    public void remove(Entity e){
        entities.remove(e);
    }

    public void spawn(Entity e){
        spawnQueue.add(e);
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

    public void setAnimation(ForegroundAnimation animation) {
        this.animation = animation;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Entity entityAt(float x, float y) {
        for (Entity entity : entities) {
            if(entity.contains(x, y));
                return entity;
        }
        return null;
    }
}
