package summit.game;

import summit.gfx.Renderer;

public class GameClickEvent extends GameUpdateEvent{

    private float gameX;
    private float gameY;

    public GameClickEvent(GameWorld world, GameMap map, java.awt.event.MouseEvent e) {
        super(world, map, 0, e.getX(), 
                            e.getY(), 
                            false);
        
        java.awt.geom.Point2D.Float p = Renderer.toTile(e.getX(), e.getY(), map.getPlayer().getCamera());

        this.gameX = p.x;
        this.gameY = p.y;
    }

    public float gameX(){
        return gameX;
    }

    public float gameY(){
        return gameY;
    }
}