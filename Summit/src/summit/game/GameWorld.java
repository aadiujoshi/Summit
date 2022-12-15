/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

import java.io.Serializable;

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
import summit.sound.Sound;
import summit.util.Time;

public class GameWorld implements Paintable, Serializable{

    private MainMap mainMap;
    
    private GameMap loadedMap;

    private long gametime;
    private long elapsedtime;
    private final long START_TIME = Time.timeMs();

    private final String SAVE_NAME;

    private transient volatile boolean paused;

    private transient PauseButton pauseButton;

    //used for safer map transitioning
    //if not null, the loaded map is set to this on the next update tick and this is set to null
    private GameMap queuedNewMap;

    private final long SEED;

    //Same object shared by all GameMaps 
    private Player player;

    private transient Window parentWindow;
    
    private transient Thread gameUpdateThread;

    /**
     * Use this constructor to create a new game
     * @param parentWindow
     */
    public GameWorld(final String name, Window parentWindow, long seed){
        this.parentWindow = parentWindow;
        SEED = seed;
        gametime = 0;
        SAVE_NAME = name;

        player = new Player(0, 0);
        mainMap = new MainMap(player, seed);
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
            int prevDelay = 1;
            while(!parentWindow.isClosed()){
                //show pause button
                parentWindow.pushGameContainer(pauseButton);

                long startTime = Time.timeNs();

                if(paused)
                    continue;
                
                this.elapsedtime = Time.timeMs() - startTime;

                //20 minutes
                this.gametime = (elapsedtime) % 1200000;

                GameUpdateEvent e = new GameUpdateEvent(this, prevDelay);
                
                if(loadedMap != null){
                    try{
                        loadedMap.update(e);
                    } catch(Exception fsda) {
                        
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
                }
                
                prevDelay = (int)(Time.timeNs()-startTime);
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
    }

    public long getGametime() {
        return this.gametime;
    }

    public Player getPlayer(){
        return this.player;
    }

    @Override
    public void paint(PaintEvent e){
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
}