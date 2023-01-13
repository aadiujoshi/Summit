package summit.game.structure;

import java.awt.Point;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 * 
 * The RaisedStone class is a Structure that represents a raised stone in the
 * game.
 * 
 * This class inherits properties from the Structure class such as position,
 * size, and the ability to be rendered on the game map.
 * 
 * The RaisedStone class also overrides the paint method to allow for custom
 * rendering of the object and the gameClick method to handle player
 * interaction.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class RaisedStone extends Structure {

    /**
     * 
     * Creates a new instance of the RaisedStone class.
     * 
     * @param x         the x position of the RaisedStone on the parent map
     * @param y         the y position of the RaisedStone on the parent map
     * @param parentMap the GameMap that the RaisedStone is currently on
     */
    public RaisedStone(float x, float y, GameMap parentMap) {
        super(x, y, 1, 1, parentMap);
        super.setSpriteOffsetY(0.25f);
        super.setSprite(Sprite.RAISED_STONE);
        super.setShadow(ColorFilter.NOFILTER);
        super.setEnabled(false);
        super.situate(parentMap);
    }

    /**
     * 
     * The paint method is responsible for rendering the RaisedStone object on the
     * game screen.
     * This implementation allows for custom rendering of the object.
     * 
     * @param e PaintEvent object that contains information about the current game
     *          state and renderer.
     */
    @Override
    public void paint(PaintEvent e) {
        super.paint(e);

        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().fillRect((int)(1+p.x-getWidth()/2*16),
        // (int)(p.y-getHeight()/2*16), (int)(getWidth()*16), (int)(getHeight()*16),
        // Renderer.toIntRGB(0, 255, 0));
    }

    /**
     * The gameClick method is responsible for handling player interaction with the
     * RaisedStone object.
     * This implementation does not contain any logic for player interaction, but
     * could be overridden to handle events such as opening a menu or triggering a
     * specific action.
     * 
     * @param e GameUpdateEvent object that contains information about the current
     *          game state.
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
    }
}
