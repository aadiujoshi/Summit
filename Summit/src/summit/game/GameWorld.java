package summit.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import summit.game.animation.SnowfallAnimation;
import summit.game.entity.PlayerEntity;
import summit.game.mapgenerator.GameMapGenerator;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gui.Window;
import summit.gui.menu.Container;
import summit.util.Time;

public class GameWorld implements Paintable, Serializable{

    private HashMap<String, GameMap> maps;
    private GameMap loadedMap;
    private final long SEED;

    private Camera camera;

    private PlayerEntity player;

    private transient Window parentWindow;

    private transient Stack<Container> gameContainers;

    private transient Thread gameUpdateThread;

    private transient SnowfallAnimation snowAnim;

    /**
     * Use this constructor to create a new game
     * @param parentWindow
     */
    public GameWorld(Window parentWindow, long seed){
        this.parentWindow = parentWindow;
        SEED = seed;
        maps = new HashMap<>();

        snowAnim = new SnowfallAnimation(1, 3);

        GameMap stage1 = GameMapGenerator.generateStage1(seed);
        GameMap stage2 = GameMapGenerator.generateStage2(seed);
        GameMap stage3 = GameMapGenerator.generateStage3(seed);

        maps.put(stage1.getName(), stage1);
        maps.put(null, stage2);
        maps.put(null, stage3);
        
        loadedMap = maps.get("stage1");
        
        player = new PlayerEntity(loadedMap.getWidth()/2, loadedMap.getHeight()/2);
        camera = new Camera(player.getX(), player.getY());
        player.setCamera(camera);

        initUpdateThread();
    }

    public void initUpdateThread(){
        gameUpdateThread = new Thread(new Runnable(){

            @Override
            public void run(){
                //in milliseconds
                int prevDelay = 1;
                while(true){
                    long startTime = Time.timeMs();
                    
                    invokeGameUpdates(prevDelay);
                    
                    prevDelay = (int)(Time.timeMs()-startTime);
                }
            }
        });

        gameUpdateThread.start();
    }

    private void invokeGameUpdates(int deltaTime){
        GameUpdateEvent gue = new GameUpdateEvent(loadedMap, deltaTime, false);

        if(loadedMap != null)
            loadedMap.update(gue);
        
        player.update(gue);
    }

    @Override
    public void paint(PaintEvent e){

        e.setCamera(camera.clone());

        if(loadedMap != null){
            loadedMap.paint(e);
        }

        player.paint(e);
        snowAnim.paint(e);
        e.getRenderer().brightenFrame(33);
    }

    public GameMap getLoadedMap() {
        return this.loadedMap;
    }

    public void setLoadedMap(GameMap loadedMap) {
        this.loadedMap = loadedMap;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
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