package summit.gui;

import summit.game.GameMap;
import java.awt.event.MouseEvent;

public interface GameClickReciever{
    public void gameClick(GameMap map, MouseEvent e);
}
