/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.item.ItemStorage;
import summit.game.item.SnowballItem;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class SnowTile extends Tile{
    
    private ItemStorage snowballs;

    public SnowTile(float x, float y){
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
        super.setBreakable(1);
        Light l = new Light(x, y, 1f, 100, 100, 100);
        l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
        
        super.setIceFilter(new ColorFilter(-10,-20,0));
        
        super.particleAnimation(true);
        super.setColor(Renderer.toIntRGB(170, 214, 230));

        this.snowballs = new ItemStorage(null);

        snowballs.addItems(new SnowballItem(null), (int)(Math.random()*2));
    }

    @Override
    public void gameClick(GameUpdateEvent e){
        super.gameClick(e);

        if(isBreakable()){
            e.getMap().getPlayer().pickupItems(snowballs);
        }
    }

    @Override
    public void reinit(){
        super.reinit();
        snowballs.reinit();
    }
}