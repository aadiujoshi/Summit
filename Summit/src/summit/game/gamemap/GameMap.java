/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.gamemap;

import java.io.Serializable;
import java.util.ArrayList;

import summit.game.GameClickReciever;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.animation.Animation;
import summit.game.animation.ForegroundAnimation;
import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.game.entity.mob.Player;
import summit.game.entity.mob.Skeleton;
import summit.game.entity.mob.Zombie;
import summit.game.entity.projectile.Projectile;
import summit.game.structure.Structure;
import summit.game.structure.Tree;
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
import summit.util.GameObject;
import summit.util.Region;
import summit.util.ScheduledEvent;
import summit.util.GameScheduler;

public class GameMap implements Serializable, Paintable, GameUpdateReciever, GameClickReciever{

    private ArrayList<Entity> spawnQueue;

    private ArrayList<Entity> entities;
    private ArrayList<Structure> structures;

    //assumes registered into scheduler
    private ArrayList<Animation> animations;

    //mob spawning
    private ScheduledEvent spawnMobs;

    //player
    private Player player;

    //render distance
    private int renderDist;
    
    //simulation distance for gameupdaterecievers
    private int simDist;

    //hostile mob cap
    private int hostileMobCap = 10;

    //stores the most recent of the player; used for when transitioning GameMaps
    private Camera camera = new Camera(0, 0);

    private ColorFilter filter;
    private ForegroundAnimation animation;
    private AmbientOcclusion ambientOcclusion;

    private TileStack[][] tiles;
    private final int WIDTH;
    private final int HEIGHT;
    private final String NAME = getClass().getSimpleName();

    private final long SEED;

    private boolean loaded;

    public GameMap(Player player, final long seed, int width, int height) {

        this.tiles = new TileStack[height][width];
        for (int x = 0; x < tiles[0].length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                tiles[y][x] = new TileStack(x, y);
            }
        }

        entities = new ArrayList<>();
        structures = new ArrayList<>();
        spawnQueue = new ArrayList<>();
        this.SEED = seed;
        this.WIDTH = tiles[0].length;
        this.HEIGHT = tiles.length;

        this.renderDist = 18;
        this.simDist = 50;

        this.setPlayer(player);
        this.camera = new Camera(width/2, height/2);

        this.animations = new ArrayList<>();
        this.ambientOcclusion = new AmbientOcclusion();

        this.spawnMobs = new ScheduledEvent(1500, ScheduledEvent.FOREVER) {
            @Override
            public void run() {
                //------------ spawn hostile mobs ---------------------------------------------------
                if(!isLoaded())
                    return;
                
                int h_count = hostileMobCount();
                
                float range = simDist/2f;
                
                //try to spawn
                for (int i = 0; i < hostileMobCap-h_count; i++) {
                    int mobType = (int)(Math.random()*3);

                    float offsetx = (float)(Math.random()*range-range/2);
                    float offsety = (float)(Math.random()*range-range/2);
                    
                    float nx = player.getX() + offsetx;
                    float ny = player.getY() + offsety;

                    MobEntity mob = null;

                    if(mobType == 0 || mobType == 1){
                        mob = new Zombie(nx, ny);
                    }

                    if(mobType == 2){
                        mob = new Skeleton(nx, ny);
                    }

                    if(mob.moveTo(getThis(), nx, ny) && mob.distance(player) >= 15){
                        spawn(mob);
                    }
                }

                //-----------------------------------------------------------------------------------
            }
        };

        GameScheduler.registerEvent(spawnMobs);
    }

    //saved world
    public void reinit(){
        if(animation != null)
            this.animation.reinit(2);

        if(spawnMobs != null)
            this.spawnMobs.reinit(1);

        for (TileStack[] ts1 : tiles) {
            for (TileStack ts : ts1) {
                ts.reinit();
            }
        }

        for (Animation pa : animations) {
            pa.reinit(2);
        }
        for (Entity e : entities) {
            if(e instanceof Player)
                continue;
            
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

        float reach = player.getReach();

        for(Structure r_struct : structures) {
            if(r_struct.contains(e.gameX(), e.gameY()) && 
                player.distance(r_struct) <= reach){
                r_struct.gameClick(e);
                return;
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            if(entity != null && entity.contains(e.gameX(), e.gameY()) && 
                Region.distance(entity.getX(), entity.getY(), player.getX(), player.getY()) <= reach){
                entity.gameClick(e);
                return;
            }
        }

        Tile tat = getTileAt(e.gameX(), e.gameY());

        if(tat != null && tat.distance(player) <= reach)
            tat.gameClick(e);
    }

    @Override
    public void update(GameUpdateEvent e) throws Exception{

        ArrayList<GameObject> updateObjects = objectsInDist(camera, simDist);
        TileStack[][] updateTiles = tilesInDist(camera, simDist);

        for (int i = 0; i < updateTiles.length; i++) {
            for (int j = 0; j < updateTiles[0].length; j++) {
                if(updateTiles[i][j] != null)
                    updateTiles[i][j].update(e);
            }
        }

        for (int i = 0; i < updateObjects.size(); i++) {
            
            GameObject gr = updateObjects.get(i);

            if(gr == null)
                continue;

            gr.update(e);

            if(updateObjects.get(i).is(Entity.destroyed)){
                ((Entity)gr).destroy(e);
                entities.remove((Entity)gr);
            }
        }
        
        //-----------------------------------------------------------------------------------

        //do collision
        ArrayList<GameObject> objects = objectsInDist(camera, simDist);

        for (int i = 0; i < objects.size(); i++) {
            GameObject gr1 = objects.get(i);

            if(!gr1.isEnabled())
                        continue;

            for (int j = 0; j < objects.size(); j++) {
                try{
                    GameObject gr2 = objects.get(j);

                    if(!gr2.isEnabled())
                        continue;

                    if(gr1 instanceof Entity){
                        getTileAt(gr1.getX(), gr1.getY()).collide(e, (Entity)gr1);
                        
                        if(i != j && gr1.overlap(gr2)){         
                            gr2.collide(e, (Entity)gr1);
                            
                            // if(!gr2.isMoveable())
                            //     ((Entity)gr1).clip(this);
                        }
                    }

                } catch(java.lang.IndexOutOfBoundsException ie){
                    System.out.println("UPDATE FAIL");
                }
            }
        }
        
        //actually spawn entities
        synchronized(spawnQueue){
            for (Entity spawn : spawnQueue) {
                entities.add(spawn);
            }
            
            spawnQueue.clear();
        }
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        //map filter
        e.addToLayer(RenderLayers.TOP_LAYER, this);

        //general animation (snowfall)
        if(animation != null)
            animation.setRenderLayer(e);
        
        //particles
        for (int i = 0; i < animations.size(); i++) {
            Animation a = animations.get(i);

            if(a == null || a.shouldTerminate()){
                animations.remove(i);
                i--;
                continue;
            }

            a.setRenderLayer(e);
        }

        //tiles in render distance (from cameraa position)
        TileStack[][] rdTiles = this.tilesInDist(e.getCamera(), renderDist);    

        for (int i = 0; i < rdTiles.length; i++) {
            for (int j = 0; j < rdTiles[0].length; j++) {
                if(rdTiles[i][j] != null)
                    rdTiles[i][j].setRenderLayer(e);
            }
        }

        //tile ambient occlusion
        ambientOcclusion.setRenderLayer(e);

        //front to back depth
        ArrayList<GameObject> sorted = objectsInDist(e.getCamera(), renderDist);

        for (int i = 0; i < sorted.size(); i++) {
            int lowestInd = i;

            for (int j = i+1; j < sorted.size(); j++) {
                float y1 = sorted.get(lowestInd).getY()-sorted.get(lowestInd).getHeight()/2;
                float y2 = sorted.get(j).getY() - sorted.get(j).getHeight()/2;

                if(y2 > y1)
                    lowestInd = j;
            }

            GameObject temp = sorted.get(i);
            sorted.set(i, sorted.get(lowestInd));
            sorted.set(lowestInd, temp);
        }
        
        //sorted GameRegions
        for (GameObject r: sorted) {
            r.setRenderLayer(e);
        }
    }

    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().filterRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT, filter);

        // e.getRenderer().renderText(
        //     objectsInDist(camera, renderDist).size()+"", 
        //         200,100, 0, new ColorFilter(java.awt.Color.RED.getRGB()));
    }

    //--------------------------------------------------------------------
    // getters and setters
    //--------------------------------------------------------------------

    @Deprecated
    public GameObject getContact(GameObject r){
        for (Structure s : structures) {
            if(r.overlap(s) && s != r)
                return s;
        }
        for (Entity e : entities) {
            if(r.overlap(e) && e != r)
                return e;
        }
        
        return null;
    }

    public int hostileMobCount(){
        int total = 0;

        for (int i = 0; i < entities.size(); i++) {
            if(entities.get(i).is(MobEntity.hostile))
                total++;
        }

        return total;
    }

    public ArrayList<GameObject> objectsInDist(Camera c, float dist){
        ArrayList<GameObject> all = new ArrayList<>();

        Region r = new Region(c.getX(), c.getY(), 0, 0);

        // float left = c.getX()-dist/2f;
        // float right = c.getX()+dist/2f;
        // float up = c.getY()+dist/2f;
        // float down = c.getY()-dist/2f;

        for (int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);

            if(e.distance(r) <= dist)
                all.add(e);

            // if(e.getX() > left && e.getX() < right && e.getY() < up && e.getY() > down)
            //     all.add(e);
        }
            
        for (int i = 0; i < structures.size(); i++){ 
            Structure s = structures.get(i);
            
            if(s.distance(r) <= dist)
                all.add(s);

            // if(s.getX() > left && s.getX() < right && s.getY() < up && s.getY() > down)
            //     all.add(s);
        }

        return all;
    }

    public TileStack[][] tilesInDist(Camera c, int dist){
        int nx = Math.round(c.getX());
        int ny = Math.round(c.getY());

        //range of tiles to display
        int rwidth = dist;
        int rheight = dist;

        TileStack[][] rdTiles = new TileStack[rheight][rwidth];

        for(int i = nx-rwidth/2; i < nx+rwidth/2; i++){
            for(int j = ny-rheight/2; j < ny+rheight/2; j++){
                if(i > -1 && j > -1 && i < tiles[0].length && j < tiles.length)
                    rdTiles[j-(ny-rheight/2)][i-(nx-rwidth/2)] = tiles[j][i];
            }
        }

        return rdTiles;
    }

    @Override
    public String toString(){
        String str = "";
        
        str += "---- ENTITES -----------------------------------------\n";
        for (Entity ent : entities) {
            if(ent instanceof Tree)
                continue;

            str += ent.toString()  + "\n";
        }

        str += "\n---- ITEMS -----------------------------------------\n";
        for (Entity ent : entities) {
            if(ent instanceof Tree)
                continue;

            str += ent.getName() + ":" + ent.getItems().toString() + "\n";
        }

        return str;
    }

    public void addStructure(Structure s){
        this.structures.add(s);
    }

    public Tile getTileAt(float x, float y){
        TileStack ts = getTileStackAt(x, y);
        return (ts == null || ts.topTile() == null) ? null : ts.topTile();
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

    public void addAnimation(Animation a){
        animations.add(a);
    }

    public void remove(Entity e){
        entities.remove(e);
    }

    public void spawn(Entity e){
        synchronized(spawnQueue){
            spawnQueue.add(e);
        }
    }
    
    public GameMap getThis(){
        return this;
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
    
    public int getSimDist() {
        return this.simDist;
    }

    public void setSimDist(int simDist) {
        this.simDist = simDist;
    }

    public int getRenderDist() {
        return this.renderDist;
    }

    public void setRenderDist(int renderDist) {
        this.renderDist = renderDist;
    }

    public void dontSpawnMobs(){
        spawnMobs.manualTerminate();
        spawnMobs = null;
    }

    @Deprecated
    public Entity entityAt(Entity loc) {
        for (Entity entity : entities) {
            if(entity.contains(loc.getX(), loc.getY()) && loc != entity);
                return entity;
        }
        return null;
    }
}
