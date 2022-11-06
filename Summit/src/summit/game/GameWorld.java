package summit.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import summit.game.entity.PlayerEntity;
import summit.game.mapgenerator.GameMapGenerator;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gui.Window;
import summit.util.Time;

public class GameWorld implements Paintable, Serializable{

    private List<GameMap> maps;
    private GameMap loadedMap;
    private final long SEED;

    private Camera camera;

    private PlayerEntity player;

    private transient Window parentWindow;

    private transient Thread gameUpdateThread;

    /**
     * Use this constructor to create a new game
     * @param parentWindow
     */
    public GameWorld(Window parentWindow, long seed){
        this.parentWindow = parentWindow;
        SEED = seed;
        maps = new ArrayList<>();

        camera = new Camera(0, 0);
        player = new PlayerEntity(0, 0);
        player.setCamera(camera);

        maps.add(GameMapGenerator.generateStage1(seed));
        loadedMap = maps.get(0);

        gameUpdateThread = new Thread(new Runnable(){

            @Override
            public void run(){
                while(true){
                    Time.nanoDelay(Time.NS_IN_MS);
                    invokeGameUpdates();
                }
            }
        });

        gameUpdateThread.start();
    }

    private void invokeGameUpdates(){
        GameUpdateEvent gue = new GameUpdateEvent(loadedMap, false);

        if(loadedMap != null)
            loadedMap.update(gue);
        
        player.update(gue);
    }

    @Override
    public void paint(PaintEvent e){
        PaintEvent pe = new PaintEvent(e.getRenderer(), e.getLastFrame(), camera);

        if(loadedMap != null){
            loadedMap.paint(pe);
        }

        player.paint(pe);

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

}