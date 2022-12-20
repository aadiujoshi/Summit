package summit.game.gamemap;

import summit.game.entity.mob.Player;

public class BossRoom extends GameMap{

    public BossRoom(Player player, long seed) {
        super(player, seed, 30, 30);
    }
}
