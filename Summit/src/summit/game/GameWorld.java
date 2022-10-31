package summit.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gui.Window;
import summit.util.Time;

public class GameWorld implements Paintable, Serializable{

    private List<GameMap> maps;
    private GameMap loadedMap;

    private Window parentWindow;

    private Thread gameUpdateThread;

    public GameWorld(Window parentWindow){
        this.parentWindow = parentWindow;

        maps = new ArrayList<>();

        gameUpdateThread = new Thread(new Runnable(){

            @Override
            public void run(){
                while(true){
                    Time.nanoDelay(Time.NS_IN_MS);
                    invokeGameUpdates();
                }
            }
        });
    }

    private void invokeGameUpdates(){
        GameUpdateEvent gue = new GameUpdateEvent(loadedMap, false);

        if(loadedMap != null)
            loadedMap.update(gue);
    }

    @Override
    public void paint(PaintEvent e){
        if(loadedMap != null)
            loadedMap.paint(e);
    }

    public GameMap getLoadedMap() {
        return this.loadedMap;
    }

    public void setLoadedMap(GameMap loadedMap) {
        this.loadedMap = loadedMap;
    }
}