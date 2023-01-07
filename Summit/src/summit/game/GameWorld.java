/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

import java.io.Serializable;

import summit.game.animation.TransitionAnimation;
import summit.game.entity.mob.Player;
import summit.game.gamemap.GameMap;
import summit.game.gamemap.MainMap;
import summit.gfx.Camera;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gui.Container;
import summit.gui.PauseButton;
import summit.gui.Window;
import summit.util.GraphicsScheduler;
import summit.util.Sound;
import summit.util.Time;

/**
 * This is the class which contains all game data and objects, and manages 
 * the creation of game updates
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class GameWorld implements Paintable, Serializable{

    /** The {@link MainMap} where the game starts */
    private MainMap mainMap;
    
    /**The current loaded {@link GameMap} that recieves game updates*/
    private GameMap loadedMap;

    /** Ignore this field, only used for debugging*/
    private volatile int tickSpeed = 0;

    /** The current time in the game, used for game mechanics*/
    private long gametime;

    /**Total elapsed in-game time*/
    private long elapsedtime;

    /**Start time of this game session */
    private long sessionStartTime = Time.timeMs();

    /** A randomized key generated at creation to distinguish GameWorlds */
    private final String SAVE_KEY = generateSaveKey();

    private final String NAME;
    
    /**If the game update loop is paused*/
    private transient volatile boolean paused;

    /**The GUI {@link Container} */
    private transient PauseButton pauseButton;

    /**Whether or not the {@code gameUpdateThread} should be terminated to end the current game session. */
    private transient volatile boolean terminate;

    public static final int GAME_NOT_COMPLETED = 0;
    public static final int GAME_VICTORY = 1;
    public static final int GAME_OVER_PLAYER_DEAD = 2;

    private int completion = GAME_NOT_COMPLETED;

    /**
     * Used for safer map transitioning. 
     * If {@code queuedNewMap} is not null, the {@code loadedMap} is set to 
     * {@code queuedNewMap} after the current 
     * update tick is finished processing and {@code queuedNewMap} is set to null
     * 
     * @see GameWorld#setLoadedMap(GameMap)
     */
    private GameMap queuedNewMap;

    /**
     * The map transition animation. A visual effect which is applied when 
     * a {@code GameMap} transition occurs.
     */
    private TransitionAnimation mapTransition;

    /**The fixed seed of this game world */
    private final long SEED;

    /**Fixed game update intervals*/
    public static final int MS_PER_TICK = 10;

    /**
     * If true, game updates happen dynamically instead of at fixed intervals.
     * However, If the game is running slow, there can be input delay and higher
     * latency. This is the default game tick option.
    */
    private static final boolean DYNAMIC_TICKS = true;

    /**The global {@link Player} object*/
    private Player player;

    /**
     * The parent {@link Window} object. It is reset after deserilization
     * @see GameWorld#reinit(Window)
    */
    private transient Window parentWindow;
    
    /** The main game update thread. It is reinitialized after deserilization
     * @see GameWorld#initUpdateThread(Window)
    */
    private transient Thread gameUpdateThread;

    /**
     * Use this constructor to create a new game
     * 
     * @param parentWindow The current {@link Window} object
     * @param seed A long number for procedural map generation
     */
    public GameWorld(String name, Window parentWindow, long seed){
        this.parentWindow = parentWindow;
        SEED = seed;
        NAME = name;
        gametime = 0;
        
        player = new Player(0, 0);
        mainMap = new MainMap(player, seed);
        mainMap.setLoaded(true);
        loadedMap = mainMap;

        player.setCamera(loadedMap.getCamera());

        this.pauseButton = new PauseButton(parentWindow, this);
        
        initUpdateThread();
    }

    // must be called after loading from save
    public void reinit(Window w){
        this.parentWindow = w;
        this.sessionStartTime = Time.timeMs();

        pauseButton = new PauseButton(w, this);

        mainMap.reinit();
        player.reinit();

        initUpdateThread();
    }

    public void initUpdateThread(){
        gameUpdateThread = new Thread(() -> {

            //needed if using dynamic ticks
            long prev_ns = Time.NS_IN_MS;

            while(!parentWindow.isClosed()){
                if(terminate)
                    break;

                //show pause button
                if(paused)
                    continue;

                parentWindow.pushGameContainer(pauseButton);
                
                long startTime = Time.timeNs();
                
                this.elapsedtime = Time.timeMs() - startTime;

                //20 minutes
                this.gametime = (elapsedtime) % 1200000;

                GameUpdateEvent e = (DYNAMIC_TICKS) ? new GameUpdateEvent(this, prev_ns) : 
                                                    new GameUpdateEvent(this, MS_PER_TICK*Time.NS_IN_MS);
                                                    
                if(loadedMap != null){
                    // loadedMap.update(e);
                    try{
                        loadedMap.update(e);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
                //change gamemaps
                if(queuedNewMap != null){
                    this.loadedMap.setLoaded(false);
                    this.queuedNewMap.setLoaded(true);
                    this.queuedNewMap.setPlayer(player);
                    player.setCamera(queuedNewMap.getCamera());
                    this.loadedMap = queuedNewMap;
                    this.queuedNewMap = null;

                    this.mapTransition = null;
                    this.mapTransition = new TransitionAnimation();
                    GraphicsScheduler.registerEvent(mapTransition);
                }

                long delay_ns = Time.timeNs()-startTime;
                
                if(DYNAMIC_TICKS){
                    prev_ns = delay_ns;
                    this.tickSpeed = (int)prev_ns;

                } else {
                    Time.nanoDelay((MS_PER_TICK*Time.NS_IN_MS)-delay_ns);
                }
            }

            Sound.stopAll();
            System.out.println("Game Update Thread Terminated");
        });
        
        gameUpdateThread.start();
    }
    
    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.getRenderLayers().addToLayer(RenderLayers.TOP_LAYER, this);

        ope.setCamera(loadedMap.getCamera().clone());

        if(loadedMap != null){
            loadedMap.setRenderLayer(ope);
        }

        if(mapTransition != null){
            mapTransition.setRenderLayer(ope);
        }
    }

    public long getGametime() {
        return this.gametime;
    }

    public Player getPlayer(){
        return this.player;
    }

    //generate an gameupdateevent for this gameworld instance | used for gameclicks etc
    public GameUpdateEvent instanceEvent(){
        return new GameUpdateEvent(this, 0);
    }

    @Override
    public void paint(PaintEvent e){
        //debug tickspeed
        // if(DYNAMIC_TICKS){
        //     e.getRenderer().renderText((tickSpeed/1000000)+"", 100, 100, 0, null);
        // }
            
    }

    public GameMap getLoadedMap() {
        return this.loadedMap;
    }

    public void setLoadedMap(GameMap newMap) {
        this.queuedNewMap = newMap;
    }

    public Camera getCamera() {
        return this.loadedMap.getCamera();
    }

    public long getElapsedTime(){
        return this.elapsedtime;
    }

    public Window getParentWindow() {
        return this.parentWindow;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    public void pause(){
        this.paused = true;
    }

    public void unpause(){
        this.paused = false;
    }

    public void terminate() {
        this.terminate = true;
    }
    
    public int getCompletion() {
        return this.completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    } 

    /**
     * Get the distint Save Key of this GameWorld
     * 
     * @return This GameWorld {@code SAVE_KEY}
     */
    public String getSaveKey() {
		return this.SAVE_KEY;
	}

    private String generateSaveKey(){
        String f = "";

        for (int i = 0; i < 16; i++) {
            int c = (int)(Math.random()*59+64);
            if(c == 92){
                f+="L";
                continue;
            }
            f+=(char)c;
        }

        System.out.println(f);

        return f;
    }
    
    public String getName() {
        return this.NAME;
    }

    public long getSeed(){
        return this.SEED;
    }

    public String toString(){
        String str = "";

        str += "\t\t----------------- CURRENT MAP -----------------\n\n";
        str += loadedMap.toString();

        str += "\t\t----------------- QUEUED MAP -----------------\n\n";
        if(queuedNewMap != null){
            str += loadedMap.toString();
        } else {
            str += "No queued map";
        }
        
        return str;
    }
}