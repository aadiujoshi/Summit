package summit.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Stack;

import summit.game.animation.SnowfallAnimation;
import summit.game.entity.mob.Player;
import summit.game.mapgenerator.GameMapGenerator;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gui.Container;
import summit.gui.Window;
import summit.util.Time;

public class GameWorld implements Paintable, Serializable{

    private HashMap<String, GameMap> maps;
    private GameMap loadedMap;

    //used for safer map transitioning
    //if not null, the loaded map is set to this on the next update tick and this is set to null
    private GameMap bufferedNewMap;

    private final long SEED;

    // private Camera camera;

    //Same object shared by all GameMaps 
    private Player player;

    private transient Window parentWindow;

    private transient Stack<Container> gameContainers;

    private transient Thread gameUpdateThread;

    /**
     * Use this constructor to create a new game
     * @param parentWindow
     */
    public GameWorld(Window parentWindow, long seed){
        this.parentWindow = parentWindow;
        SEED = seed;
        maps = new HashMap<>();

        GameMap stage1 = GameMapGenerator.generateStage1(seed);
        GameMap stage2 = GameMapGenerator.generateStage2(seed);
        GameMap stage3 = GameMapGenerator.generateStage3(seed);

        maps.put(stage1.getName(), stage1);
        maps.put(null, stage2);
        maps.put(null, stage3);
        
        loadedMap = maps.get("stage1"); 
        
        stage1.setCamera(new Camera(30, 30));

        player = new Player(30, 30, stage1.getCamera());

        stage1.setPlayer(player);
        stage1.setAnimation(new SnowfallAnimation(4, 3));
        stage1.setFilter(new ColorFilter(-60, -50, 0));

        player.setCamera(stage1.getCamera());
        
        initUpdateThread();
    }

    public void initUpdateThread(){
        gameUpdateThread = new Thread(new Runnable(){

            @Override
            public void run(){
                //in milliseconds
                int prevDelay = 1;
                while(true){
                    // Time.nanoDelay(Time.NS_IN_MS*5);
                    long startTime = Time.timeNs();
                    
                    invokeGameUpdates(prevDelay);
                    
                    prevDelay = (int)(Time.timeNs()-startTime);
                }
            }
        });

        gameUpdateThread.start();
    }

    private void invokeGameUpdates(int deltaTime){
        GameUpdateEvent e = new GameUpdateEvent(this, deltaTime, parentWindow.mouseX(), parentWindow.mouseY(), false);
        
        if(loadedMap != null)
            loadedMap.update(e);
        
        if(bufferedNewMap != null){
            this.loadedMap.setLoaded(false);
            this.bufferedNewMap.setLoaded(true);
            this.bufferedNewMap.setPlayer(player);
            player.setCamera(bufferedNewMap.getCamera());
            this.loadedMap = bufferedNewMap;
            this.bufferedNewMap = null;
        }
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.getRenderLayers().addToLayer(RenderLayers.TOP_LAYER, this);

        ope.setCamera(loadedMap.getCamera().clone());

        if(loadedMap != null){
            loadedMap.setRenderLayer(ope);
        }
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
        this.bufferedNewMap = newMap;
    }

    public Camera getCamera() {
        return this.loadedMap.getCamera();
    }

    public Stack<Container> getGameContainers() {
        return this.gameContainers;
    }

    public void setGameContainers(Stack<Container> gameContainers) {
        this.gameContainers = gameContainers;
    }

    public Window getParentWindow() {
        return this.parentWindow;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }
}