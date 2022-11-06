package summit.game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.parser.Entity;

import summit.game.entity.PlayerEntity;
import summit.game.tile.TileStack;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;

public class GameMap implements Paintable, GameUpdateReciever{
    
    private List<Entity> entities;

    private PlayerEntity player;

    private TileStack[][] map;
    private final int WIDTH;
    private final int HEIGHT;
    private final String NAME;

    private final long SEED;

    private boolean loaded;

    public GameMap(String name, final long seed, final int width, final int height) {
        map = new TileStack[height][width];
        entities = new ArrayList<>();
        this.NAME = name;
        this.SEED = seed;
        this.WIDTH = width;
        this.HEIGHT = height;
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
            for(int j = 0; j < map[0].length; j++){
                map[i][j].paint(e);
            }
        }
    }

    //--------------------------------------------------------------------
    // getters and setters
    //--------------------------------------------------------------------
    
    public TileStack[][] getMap() {
        return this.map;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
