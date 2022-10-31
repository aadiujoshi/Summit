package summit.gui;

import summit.game.GameMap;
import java.awt.event.MouseEvent;

public interface GameClickListener{
    public void gameClick(GameMap map, MouseEvent e);
}
