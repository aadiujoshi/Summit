package summit.game;

import java.net.Socket;

import summit.game.tile.TileStack;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;

public class GameMap implements Paintable, GameUpdate{
    
    // List<Entity> entities;
    private TileStack[][] map;

    private String name;

    private Camera camera;
    // private Player player;

    public GameMap(String name) {
        map = new TileStack[256][256];
        this.name = name;
    }

    @Override
    public void update(GameUpdateEvent e) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j].update(e);
            }
        }
    }

    @Override
    public void paint(PaintEvent e) {
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                map[i][j].paint(e);
            }
        }
    }

    //--------------------------------------------------------------------
    // getters and setters
    //--------------------------------------------------------------------

    public Camera getCamera(){
        return camera;
    }

    public void setCamera(Camera cam){
        this.camera = cam;
    }
    
    public TileStack[][] getMap() {
        return this.map;
    }

    // public Player getPlayer() {
    //     return this.player;
    // }

    // public void setPlayer(Player player) {
    //     this.player = player;
    // }
}
