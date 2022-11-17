package summit.game;

public class GameClickEvent{
    private GameWorld world;
    private GameMap map;
    private MouseEvent me;

    public GameClickEvent(GameWorld world, GameMap map, MouseEvent e){
        this.world = world;
        this.map = map;
        this.me = e;
    }

    public MouseEvent mouseInfo(){
        return me;
    }

    public GameWorld getWorld(){
        return world
    }

    public GameMap getMap(){
        return map;
    }
}