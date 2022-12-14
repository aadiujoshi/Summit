/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.Player;
import summit.game.gamemap.GameMap;
import summit.util.Direction;

public class MapEntrance extends Structure{

    private GameMap exMap;

    private Direction enterOrientation = Direction.SOUTH;

    public MapEntrance(float x, float y, float width, float height, GameMap exMap, GameMap parentMap) {
        super(x, y, width, height, parentMap);
        this.exMap = exMap;
    }
    
    @Override
    public void gameClick(GameUpdateEvent e) {
        Player p = e.getMap().getPlayer();

        if((enterOrientation == Direction.SOUTH &&
                getY()-getHeight()/2 > p.getY() && p.getX() < getX()+getWidth()/2 && p.getX() > getX()-getWidth()/2) ||

            (enterOrientation == Direction.NORTH && 
                getY()-getHeight()/2 < p.getY() && p.getX() < getX()+getWidth()/2 && p.getX() > getX()-getWidth()/2)){

            e.setLoadedMap(exMap);
        }
    }
    
    public Direction getEnterOrientation(){
        return enterOrientation;
    }

    public void setExMap(GameMap map){
        this.exMap = map;
    }

    public GameMap getExMap(){
        return this.exMap;
    }

    public void setEnterOrientation(Direction d){
        this.enterOrientation = d;
    }
}
