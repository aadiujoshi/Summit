package summit.util;

import summit.game.GameUpdateEvent;

public interface ControlsReciever {
    public void keyPress(GameUpdateEvent e);
    public void keyRelease(GameUpdateEvent e);
}
