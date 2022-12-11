package summit.game.tile;

import summit.game.GameUpdateEvent;

public class EmptyTile extends Tile{

    public EmptyTile(float x, float y) {
        super(x, y);
        super.setBoundary(true);
    }

    @Override
    public void update(GameUpdateEvent e) {
    }
}
