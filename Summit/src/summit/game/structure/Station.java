/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.structure;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gui.Container;

public class Station extends Structure{

    private Container gui;

    public Station(float x, float y, float width, float height, GameMap parentMap) {
        super(x, y, width, height, parentMap);
    }

    public Station(float x, float y, GameMap parentMap){
        super(x, y, 1, 1, parentMap);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        e.getWindow().pushGameContainer(gui);
    }
}
