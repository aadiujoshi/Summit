/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.item.SnowballItem;
import summit.gfx.Light;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class SnowTile extends Tile{
    
    public SnowTile(float x, float y){
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
        super.setBreakable(true);
        Light l = new Light(x, y, 1f, 100, 100, 100);
        l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
        
        super.particleAnimation(true);
        super.setColor(Renderer.toIntRGB(170, 214, 230));
    }

    @Override
    public void gameClick(GameUpdateEvent e){
        super.gameClick(e);

        e.getMap().getPlayer().addItems(
                new SnowballItem(
                    e.getMap().getPlayer()), 
                    "snowballs", 
                    1);
    }
}