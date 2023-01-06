package summit.game.entity.mob;

/**
 * 
 * The Trader class represents a non-playable character that the player can
 * interact with to buy and sell items.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class Trader extends HumanoidEntity {

    /**
     * 
     * Constructs a new Trader at the specified x and y position and with the
     * specified width and height.
     * 
     * @param x      the x position of the Trader
     * @param y      the y position of the Trader
     * @param width  the width of the Trader
     * @param height the height of the Trader
     */
    public Trader(float x, float y, float width, float height) {
        super(x, y);
    }
}
