package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.animation.GlistenAnimation;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;
import summit.util.GraphicsScheduler;
import summit.util.ScheduledEvent;

public class DungeonsExit extends MapEntrance {

    /**
     * exmap is the mainmap and parentmap is the dungeonmap
     */
    public DungeonsExit(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 2, 2, exMap, parentMap);
        super.setSprite(Sprite.DUNGEON_PORTAL);
        super.setShadow(ColorFilter.NOFILTER);
        super.situate(parentMap);

        // GraphicsScheduler.registerEvent(new ScheduledEvent() {
            
        // });
    }

    @Override
    public void update(GameUpdateEvent e) throws Exception{
        super.update(e);
        
        if(Math.random() >= 0.992){

            float ox = (float)(Math.random()*2-1);
            float oy = (float)(Math.random()*2-1);

            GlistenAnimation ga = new GlistenAnimation(getX()+ox, getY()+oy, 4, 0x2aa3e6);
            ga.setRLayer(RenderLayers.STRUCTURE_ENTITY_LAYER+1);

            e.getMap().addAnimation(ga);
        }
    }

    @Override
    public void reinit(){
    }
}
