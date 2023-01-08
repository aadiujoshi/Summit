package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.animation.GlistenAnimation;
import summit.game.gamemap.DungeonsMap;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;

public class DungeonsEntrance extends MapEntrance{
    
    /**
     * parentmap is the main map and exmap is a dungeon map
     */
    public DungeonsEntrance(float x, float y, GameMap parentMap) {
        super(x, y, 2, 2, new DungeonsMap(parentMap.getPlayer(), parentMap.getSeed()), parentMap);
        super.setShadow(ColorFilter.NOFILTER);

        super.getExMap().addStructure(new DungeonsExit( 
                            super.getExMap().getWidth()/2-0.5f, 
                            super.getExMap().getHeight()/2+2-0.5f, 
                            parentMap, super.getExMap()));

        super.setSprite(Sprite.DUNGEON_PORTAL);
        super.situate(parentMap);
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
        super.reinit();
        getExMap().reinit();
    }
}
