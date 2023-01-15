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

/**
 * 
 * The GameMap class represents a game map which contains all the entities,
 * structures, and tiles in the game.
 * 
 * It also implements Serializable, Paintable, GameUpdateReciever,
 * GameClickReciever to handle the game's logic, rendering,
 * 
 * and user interactions.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class GameMap implements Serializable, Paintable, GameUpdateReciever, GameClickReciever {

    private ArrayList<Entity> spawnQueue;

    private ArrayList<Entity> entities;
    private ArrayList<Structure> structures;

    // assumes registered into scheduler
    private ArrayList<Animation> animations;

    // mob spawning
    private ScheduledEvent spawnMobs;

    // player
    private Player player;

    // render distance
    private int renderDist;

    // simulation distance for gameupdaterecievers
    private int simDist;

    // hostile mob cap
    private int hostileMobCap = 10;

    // stores the most recent of the player; used for when transitioning GameMaps
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

    /**
     * 
     * Constructor for the GameMap class.
     * 
     * @param player the player object in the game.
     * @param seed   the seed for generating the map.
     * @param width  the width of the map.
     * @param height the height of the map.
     */
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
        this.camera = new Camera(width / 2, height / 2);

        this.animations = new ArrayList<>();
        this.ambientOcclusion = new AmbientOcclusion();

        this.spawnMobs = new ScheduledEvent(1500, ScheduledEvent.FOREVER) {
            @Override
            public void run() {
                // ------------ spawn hostile mobs
                // ---------------------------------------------------
                if (!isLoaded())
                    return;

                int h_count = hostileMobCount();

                float range = simDist / 2f;

                // try to spawn
                for (int i = 0; i < hostileMobCap - h_count; i++) {
                    int mobType = (int) (Math.random() * 3);

                    float offsetx = (float) (Math.random() * range - range / 2);
                    float offsety = (float) (Math.random() * range - range / 2);

                    float nx = player.getX() + offsetx;
                    float ny = player.getY() + offsety;

                    MobEntity mob = null;

                    if (mobType == 0 || mobType == 1) {
                        mob = new Zombie(nx, ny);
                    }

                    if (mobType == 2) {
                        mob = new Skeleton(nx, ny);
                    }

                    if (mob.moveTo(getThis(), nx, ny) && mob.distance(player) >= 15) {
                        spawn(mob);
                    }
                }

                // -----------------------------------------------------------------------------------
            }
        };

        GameScheduler.registerEvent(spawnMobs);
    }

    // saved world

    /**
     * 
     * reinit method is used to reinitialize the game map after loading a saved
     * world.
     */
    public void reinit() {
        if (animation != null)
            this.animation.reinit(2);

        if (spawnMobs != null)
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
            if (e instanceof Player)
                continue;

            e.reinit();
        }
        for (Structure s : structures) {
            s.reinit();
        }
    }

    /**
     * 
     * gameClick method is called when a click event occurs in the game. It checks
     * if the click event is within the boundaries of the map,
     * and checks if the player has reached the structure or entity that was clicked
     * on. If so, it will call the gameClick method for that structure or entity.
     * 
     * @param e the GameUpdateEvent containing information about the click event.
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        if (e.gameX() > getWidth() || e.gameX() < 0 ||
                e.gameY() > getHeight() || e.gameY() < 0)
            return;

        float reach = player.getReach();

        for (Structure r_struct : structures) {
            if (r_struct.contains(e.gameX(), e.gameY()) &&
                    player.distance(r_struct) <= reach) {
                r_struct.gameClick(e);
                return;
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            if (entity != null && entity.contains(e.gameX(), e.gameY()) &&
                    Region.distance(entity.getX(), entity.getY(), player.getX(), player.getY()) <= reach) {
                entity.gameClick(e);
                return;
            }
        }

        Tile tat = getTileAt(e.gameX(), e.gameY());

        if (tat != null && tat.distance(player) <= reach)
            tat.gameClick(e);
    }

    /**
     * 
     * update method is called when an update event occurs in the game. It updates
     * the objects and tiles in the simulation distance,
     * checks for collisions and spawns entities that are in the spawnQueue.
     * 
     * @param e the GameUpdateEvent containing information about the update event.
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {

        ArrayList<GameObject> updateObjects = objectsInDist(camera, simDist);
        TileStack[][] updateTiles = tilesInDist(camera, simDist);

        for (int i = 0; i < updateTiles.length; i++) {
            for (int j = 0; j < updateTiles[0].length; j++) {
                if (updateTiles[i][j] != null)
                    updateTiles[i][j].update(e);
            }
        }

        for (int i = 0; i < updateObjects.size(); i++) {

            GameObject gr = updateObjects.get(i);

            if (gr == null)
                continue;

            gr.update(e);

            if (updateObjects.get(i).is(Entity.destroyed)) {
                ((Entity) gr).destroy(e);
                entities.remove((Entity) gr);
            }
        }

        // -----------------------------------------------------------------------------------

        // do collision
        ArrayList<GameObject> objects = objectsInDist(camera, simDist);

        for (int i = 0; i < objects.size(); i++) {
            GameObject gr1 = objects.get(i);

            if (!gr1.isEnabled())
                continue;

            for (int j = 0; j < objects.size(); j++) {
                try {
                    GameObject gr2 = objects.get(j);

                    if (!gr2.isEnabled())
                        continue;

                    if (gr1 instanceof Entity) {
                        getTileAt(gr1.getX(), gr1.getY()).collide(e, (Entity) gr1);

                        if (i != j && gr1.overlap(gr2)) {
                            gr2.collide(e, (Entity) gr1);

                            // if(!gr2.isMoveable())
                            // ((Entity)gr1).clip(this);
                        }
                    }

                } catch (java.lang.IndexOutOfBoundsException ie) {
                    System.out.println("UPDATE FAIL");
                }
            }
        }

        // actually spawn entities
        synchronized (spawnQueue) {
            for (Entity spawn : spawnQueue) {
                entities.add(spawn);
            }

            spawnQueue.clear();
        }
    }

    /**
     * 
     * setRenderLayer method is called to set the order in which the objects in the
     * game should be rendered.
     * 
     * @param e the OrderPaintEvent containing information about the render order.
     */
    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        // map filter
        e.addToLayer(RenderLayers.TOP_LAYER, this);

        // general animation (snowfall)
        if (animation != null)
            animation.setRenderLayer(e);

        // particles
        for (int i = 0; i < animations.size(); i++) {
            Animation a = animations.get(i);

            if (a == null || a.shouldTerminate()) {
                animations.remove(i);
                i--;
                continue;
            }

            a.setRenderLayer(e);
        }

        // tiles in render distance (from cameraa position)
        TileStack[][] rdTiles = this.tilesInDist(e.getCamera(), renderDist);

        for (int i = 0; i < rdTiles.length; i++) {
            for (int j = 0; j < rdTiles[0].length; j++) {
                if (rdTiles[i][j] != null)
                    rdTiles[i][j].setRenderLayer(e);
            }
        }

        // tile ambient occlusion
        ambientOcclusion.setRenderLayer(e);

        // front to back depth
        ArrayList<GameObject> sorted = objectsInDist(e.getCamera(), renderDist);

        for (int i = 0; i < sorted.size(); i++) {
            int lowestInd = i;

            for (int j = i + 1; j < sorted.size(); j++) {
                float y1 = sorted.get(lowestInd).getY() - sorted.get(lowestInd).getHeight() / 2;
                float y2 = sorted.get(j).getY() - sorted.get(j).getHeight() / 2;

                if (y2 > y1)
                    lowestInd = j;
            }

            GameObject temp = sorted.get(i);
            sorted.set(i, sorted.get(lowestInd));
            sorted.set(lowestInd, temp);
        }

        // sorted GameRegions
        for (GameObject r : sorted) {
            r.setRenderLayer(e);
        }
    }

    /**
     * 
     * paint method is called to render the game map. It draws the tiles within the
     * render distance from the camera,
     * and applies the ambient occlusion and color filter to the game map.
     * 
     * @param e the PaintEvent containing the Graphics object for rendering.
     */
    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().filterRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT, filter);

        // e.getRenderer().renderText(
        // objectsInDist(camera, renderDist).size()+"",
        // 200,100, 0, new ColorFilter(java.awt.Color.RED.getRGB()));
    }

    // --------------------------------------------------------------------
    // getters and setters
    // --------------------------------------------------------------------
    /**
     * 
     * getContact method is used to get the first object that the given object
     * overlaps with, that is not itself.
     * 
     * @param r the object to check for overlaps
     * @return the first object that overlaps with the given object, or null if no
     *         overlap is found.
     */
    @Deprecated
    public GameObject getContact(GameObject r) {
        for (Structure s : structures) {
            if (r.overlap(s) && s != r)
                return s;
        }
        for (Entity e : entities) {
            if (r.overlap(e) && e != r)
                return e;
        }

        return null;
    }

    /**
     * 
     * hostileMobCount method is used to count the number of hostile mobs in the
     * game map.
     * 
     * @return the number of hostile mobs in the game map.
     */
    public int hostileMobCount() {
        int total = 0;

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).is(MobEntity.hostile))
                total++;
        }

        return total;
    }

    /**
     * 
     * objectsInDist method is used to get an ArrayList of GameObjects within a
     * certain distance from a given Camera object.
     * 
     * @param c    the Camera object to measure the distance from.
     * @param dist the distance to check for GameObjects within.
     * @return an ArrayList of GameObjects within the given distance from the Camera
     *         object.
     */
    public ArrayList<GameObject> objectsInDist(Camera c, float dist) {
        ArrayList<GameObject> all = new ArrayList<>();

        Region r = new Region(c.getX(), c.getY(), 0, 0);

        // float left = c.getX()-dist/2f;
        // float right = c.getX()+dist/2f;
        // float up = c.getY()+dist/2f;
        // float down = c.getY()-dist/2f;

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            if (e.distance(r) <= dist)
                all.add(e);

            // if(e.getX() > left && e.getX() < right && e.getY() < up && e.getY() > down)
            // all.add(e);
        }

        for (int i = 0; i < structures.size(); i++) {
            Structure s = structures.get(i);

            if (s.distance(r) <= dist)
                all.add(s);

            // if(s.getX() > left && s.getX() < right && s.getY() < up && s.getY() > down)
            // all.add(s);
        }

        return all;
    }

    /**
     * 
     * tilesInDist method is used to get a 2D array of TileStack objects within a
     * certain distance from a given Camera object.
     * 
     * @param c    the Camera object to measure the distance from.
     * @param dist the distance to check for TileStack objects within.
     * @return a 2D array of TileStack objects within the given distance from the
     *         Camera object.
     */
    public TileStack[][] tilesInDist(Camera c, int dist) {
        int nx = Math.round(c.getX());
        int ny = Math.round(c.getY());

        // range of tiles to display
        int rwidth = dist;
        int rheight = dist;

        TileStack[][] rdTiles = new TileStack[rheight][rwidth];

        for (int i = nx - rwidth / 2; i < nx + rwidth / 2; i++) {
            for (int j = ny - rheight / 2; j < ny + rheight / 2; j++) {
                if (i > -1 && j > -1 && i < tiles[0].length && j < tiles.length)
                    rdTiles[j - (ny - rheight / 2)][i - (nx - rwidth / 2)] = tiles[j][i];
            }
        }

        return rdTiles;
    }

    /**
     * 
     * toString method is used to get a string representation of the game map's
     * entities and items.
     * 
     * @return a string representation of the game map's entities and items.
     */
    @Override
    public String toString() {
        String str = "";

        str += "---- ENTITES -----------------------------------------\n";
        for (Entity ent : entities) {
            if (ent instanceof Tree)
                continue;

            str += ent.toString() + "\n";
        }

        str += "\n---- ITEMS -----------------------------------------\n";
        for (Entity ent : entities) {
            if (ent instanceof Tree)
                continue;

            str += ent.getName() + ":" + ent.getItems().toString() + "\n";
        }

        return str;
    }

    /**
     * 
     * addStructure method is used to add a Structure object to the game map's
     * structures ArrayList.
     * 
     * @param s the Structure object to add to the game map.
     */
    public void addStructure(Structure s) {
        this.structures.add(s);
    }

    /**
     * 
     * Returns the top tile at a given x, y coordinate on the map.
     * 
     * @param x The x coordinate on the map.
     * @param y The y coordinate on the map.
     * @return The top tile at the given x, y coordinate. Returns null if there is
     *         no tile at that coordinate or if the coordinate is out of bounds.
     */
    public Tile getTileAt(float x, float y) {
        TileStack ts = getTileStackAt(x, y);
        return (ts == null || ts.topTile() == null) ? null : ts.topTile();
    }

    /**
     * 
     * Returns the tile stack at a given x, y coordinate on the map.
     * 
     * @param x The x coordinate on the map.
     * @param y The y coordinate on the map.
     * @return The tile stack at the given x, y coordinate. Returns null if the
     *         coordinate is out of bounds.
     */
    public TileStack getTileStackAt(float x, float y) {
        // out of bounds
        if (Math.round(y) < 0 || Math.round(y) >= tiles.length ||
                Math.round(x) < 0 || Math.round(x) >= tiles[0].length)
            return null;
        return tiles[Math.round(y)][Math.round(x)];
    }

    /**
     * 
     * Returns the 2D array of TileStacks representing the entire map.
     * 
     * @return the 2D array of TileStacks representing the entire map.
     */
    public TileStack[][] getTiles() {
        return this.tiles;
    }

    /**
     * 
     * Returns whether the map has been loaded or not.
     * 
     * @return true if the map has been loaded, false otherwise.
     */
    public boolean isLoaded() {
        return this.loaded;
    }

    /**
     * 
     * Sets whether the map has been loaded or not.
     * 
     * @param loaded true if the map has been loaded, false otherwise.
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * 
     * Returns the player object associated with this map.
     * 
     * @return the player object associated with this map.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * 
     * Sets the player object associated with this map.
     * 
     * @param player the player object to associate with this map.
     */
    public void setPlayer(Player player) {
        player.setCamera(this.camera);
        if (this.player == null)
            entities.add(player);
        this.player = player;
    }

    /**
     * 
     * Returns the name of this map.
     * 
     * @return the name of this map.
     */
    public String getName() {
        return this.NAME;
    }

    /**
     * 
     * Returns the width of this map.
     * 
     * @return the width of this map.
     */
    public int getWidth() {
        return this.WIDTH;
    }

    /**
     * 
     * Returns the height of this map.
     * 
     * @return the height of this map.
     */
    public int getHeight() {
        return this.HEIGHT;
    }

    /**
     * 
     * Returns the seed used to generate this map.
     * 
     * @return the seed used to generate this map.
     */
    public long getSeed() {
        return this.SEED;
    }

    /**
     * Adds an animation to the map's animation list.
     *
     * @param a The animation to be added.
     */
    public void addAnimation(Animation a) {
        animations.add(a);
    }

    /**
     * Removes an entity from the map's entity list.
     * 
     * @param e The entity to be removed.
     */
    public void remove(Entity e) {
        entities.remove(e);
    }

    /**
     * Spawns an entity into the map's entity list.
     * 
     * @param e The entity to be spawned.
     */
    public void spawn(Entity e) {
        synchronized (spawnQueue) {
            spawnQueue.add(e);
        }
    }

    /**
     * Returns the current instance of the GameMap.
     * 
     * @return The current GameMap.
     */
    public GameMap getThis() {
        return this;
    }

    /**
     * Returns the current filter applied to the map.
     * 
     * @return The current filter.
     */
    public ColorFilter getFilter() {
        return this.filter;
    }

    /**
     * Sets the filter applied to the map.
     * 
     * @param filter The filter to be applied.
     */
    public void setFilter(ColorFilter filter) {
        this.filter = filter;
    }

    /**
     * Returns the current animation applied to the map.
     * 
     * @return The current animation.
     */
    public Paintable getAnimation() {
        return this.animation;
    }

    /**
     * Sets the animation applied to the map.
     * 
     * @param animation The animation to be applied.
     */
    public void setAnimation(ForegroundAnimation animation) {
        this.animation = animation;
    }

    /**
     * Returns the current camera used by the map.
     * 
     * @return The current camera.
     */
    public Camera getCamera() {
        return this.camera;
    }

    /**
     * Sets the camera used by the map.
     * 
     * @param camera The camera to be used.
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Returns the distance for simulation.
     * 
     * @return The simulation distance.
     */
    public int getSimDist() {
        return this.simDist;
    }

    /**
     * Sets the distance for simulation.
     * 
     * @param simDist The simulation distance.
     */
    public void setSimDist(int simDist) {
        this.simDist = simDist;
    }

    /**
     * Returns the distance for rendering.
     * 
     * @return The render distance.
     */
    public int getRenderDist() {
        return this.renderDist;
    }

    /**
     * Sets the distance for rendering.
     * 
     * @param renderDist The render distance.
     */
    public void setRenderDist(int renderDist) {
        this.renderDist = renderDist;
    }

    /**
     * 
     * Stops the {@code spawnMobs} {@code ScheduledEvent} from spawning any more mobs.
     */
    public void dontSpawnMobs() {
        spawnMobs.manualTerminate();
        spawnMobs = null;
    }

    /**
     * Returns the first entity at a given location
     * 
     * @param loc the location to check
     * @return the first entity at the location
     */
    @Deprecated
    public Entity entityAt(Entity loc) {
        for (Entity entity : entities) {
            if (entity.contains(loc.getX(), loc.getY()) && loc != entity)
                ;
            return entity;
        }
        return null;
    }
}
