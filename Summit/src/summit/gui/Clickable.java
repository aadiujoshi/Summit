package summit.gui;

import java.awt.event.MouseEvent;

import summit.game.GameMap;

public interface Clickable{
    public void click(GameMap map, MouseEvent e);
}
