package summit.game;

public interface GameUpdate{

    /**
    * ticking + game physics + situational checking
    */
    public void update(GameUpdateEvent e);
}