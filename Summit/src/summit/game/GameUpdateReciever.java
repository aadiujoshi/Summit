package summit.game;

public interface GameUpdateReciever{

    /**
    * ticking + game physics + situational checking
    */
    public void update(GameUpdateEvent e);
}