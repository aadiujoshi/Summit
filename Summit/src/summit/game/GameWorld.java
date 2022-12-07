/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import summit.game.animation.ForegroundAnimation;
import summit.game.entity.mob.Player;
import summit.game.mapgenerator.GameMapGenerator;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
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
        stage1.setAnimation(new ForegroundAnimation(4, 3, Renderer.toIntRGB(200, 200, 250)));
        stage1.setFilter(new ColorFilter(-60, -50, 0));

        player.setCamera(stage1.getCamera());
        
        initUpdateThread();
    }

    // must be called after loading from save
    public void reinit(Window w){
        for (Map.Entry<String, GameMap> m : maps.entrySet()) {
            if(m.getValue() != null)
                m.getValue().reinit();
        }
        this.parentWindow = w;
        initUpdateThread();
    }

    public void initUpdateThread(){
        gameUpdateThread = new Thread(new Runnable(){

            @Override
            public void run(){
                int prevDelay = 1;
                while(!parentWindow.isClosed()){
                    long startTime = Time.timeNs();
                    
                    invokeGameUpdates(prevDelay);
                    
                    prevDelay = (int)(Time.timeNs()-startTime);
                }

                System.out.println("Game Update Thread Terminated");
            }
        });

        gameUpdateThread.start();
    }

    private void invokeGameUpdates(int deltaTime){
        GameUpdateEvent e = new GameUpdateEvent(this, deltaTime, parentWindow.mouseX(), parentWindow.mouseY(), false);
        
        if(loadedMap != null)
            loadedMap.update(e);
        
        //change gamemaps
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

    public Window getParentWindow() {
        return this.parentWindow;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }
}