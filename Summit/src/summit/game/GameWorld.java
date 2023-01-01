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
import summit.gui.PauseButton;
import summit.gui.Window;
import summit.util.GraphicsScheduler;
import summit.util.Sound;
import summit.util.Time;

public class GameWorld implements Paintable, Serializable{

    private MainMap mainMap;
    
    private GameMap loadedMap;

    //just for debugging
    private volatile int tickSpeed = 0;

    private long gametime;
    private long elapsedtime;
    private final long START_TIME = Time.timeMs();

    /**
     * 
     */
    private final String SAVE_NAME = generateSaveName();

    private transient volatile boolean paused;
    private transient PauseButton pauseButton;

    //used for safer map transitioning
    //if not null, the loaded map is set to this on the next update tick and this is set to null
    private GameMap queuedNewMap;

    private TransitionAnimation mapTransition;

    private final long SEED;
    public static final int MS_PER_TICK = 15;
    private static final boolean DYNAMIC_TICKS = true;

    //Same object shared by all GameMaps 
    private Player player;

    private transient Window parentWindow;
    
    private transient Thread gameUpdateThread;

    /**
     * Use this constructor to create a new game
     * @param parentWindow
     */
    public GameWorld(Window parentWindow, long seed){
        this.parentWindow = parentWindow;
        SEED = seed;
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

        pauseButton = new PauseButton(w, this);

        mainMap.reinit();

        initUpdateThread();
    }

    public void initUpdateThread(){
        gameUpdateThread = new Thread(() -> {

            //needed if using dynamic ticks
            long prev_ns = Time.NS_IN_MS;

            while(!parentWindow.isClosed()){
                //show pause button
                if(paused)
                    continue;

                parentWindow.pushGameContainer(pauseButton);
                
                long startTime = Time.timeNs();
                
                this.elapsedtime = Time.timeMs() - startTime;

                //20 minutes
                this.gametime = (elapsedtime) % 1200000;

                GameUpdateEvent e = (DYNAMIC_TICKS) ? new GameUpdateEvent(this, prev_ns) : 
                                                    new GameUpdateEvent(this, MS_PER_TICK);
                
                if(loadedMap != null){
                    loadedMap.update(e);
                    // try{
                    //     loadedMap.update(e);
                    // } catch(ArrayIndexOutOfBoundsException ex) {
                    //     ex.printStackTrace();
                    // }
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

    public String getSaveName() {
		return this.SAVE_NAME;
	}

    private String generateSaveName(){
        String f = "";

        for (int i = 0; i < 16; i++) {
            f += (char)(Math.random()*59+64);
        }

        System.out.println(f);

        return f;
    }
}