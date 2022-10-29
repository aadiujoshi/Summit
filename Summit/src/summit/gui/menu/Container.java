package summit.gui.menu;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import summit.game.GameMap;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.Clickable;
import summit.util.Region;

public class Container implements Paintable, Clickable{

    private List<Container> components;
    private Region region;
    private Container parent;

    private float REL_WIDTH;
    private float REL_HEIGHT;
    private float REL_X;
    private float REL_Y;

    public Container(Container parent, float relX, float relY, float relWidth, float relHeight){
        components = new ArrayList<>();
        this.parent = parent;
        if(parent == null)
            region = new Region(Renderer.WIDTH*relX, Renderer.HEIGHT*relY, Renderer.WIDTH*relWidth, Renderer.HEIGHT*relHeight);
        else {
            
            region = new Region(Renderer.WIDTH*relX, Renderer.HEIGHT*relY, Renderer.WIDTH*relWidth, Renderer.HEIGHT*relHeight);
        }

        REL_WIDTH = relWidth;
        REL_HEIGHT = relHeight;
        REL_X = relX;
        REL_Y = relY;
    }

    protected Container(){
        //use for subclasses
    }

    @Override
    public void click(GameMap map, MouseEvent e) {
        for(int i = 0; i < components.size(); i++){
            if(components.get(i).getRegion().contains(e.getX(), e.getY())){
                components.get(i).click(map, e);
            }
        }
    }

    @Override
    public void paint(PaintEvent e) {

        Renderer ren = e.getRenderer();

        int rWidth = ((int)(region.getWidth()/16))*16;
        int rHeight = ((int)(region.getHeight()/16))*16;


        int startX = (int)(region.getX()-(rWidth/2));
        int startY = (int)(region.getY()-(rHeight/2));

        int endX = (int)(region.getX()+(rWidth/2));
        int endY = (int)(region.getY()+(rHeight/2));

        //traversed in pixel coordinates
        for (int x = startX; x <= endX; x+=16) {
            for (int y = startY; y <= endY; y+=16) {
                // System.out.println(px + "  " + py + "\n\n");
                
                if(y == startY && x == startX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.FLIP_NONE);
                    continue;
                }
                if(y == startY && x == endX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.FLIP_X);
                    continue;
                }
                if(y == endY && x == startX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.FLIP_Y);
                    continue;
                }
                if(y == endY && x == endX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.FLIP_X | Renderer.FLIP_Y);
                    continue;
                }
                //------------

                //check borders
                if(y == startY){
                    ren.render(Sprite.MENU_BORDER, x, y, Renderer.FLIP_NONE);
                    continue;
                }
                if(y == endY){
                    ren.render(Sprite.MENU_BORDER, x, y, Renderer.FLIP_Y);
                    continue;
                }
                if(x == startX){
                    ren.render(Sprite.MENU_BORDER, x, y,  Renderer.ROTATE_90 | Renderer.FLIP_X);
                    continue;
                }
                if(x == endX){
                    ren.render(Sprite.MENU_BORDER, x, y, Renderer.ROTATE_90);
                    continue;
                }
                //------------

                //render inside
                ren.render(Sprite.MENU_FILL, x, y, Renderer.FLIP_NONE);
            }
        }

        //draw components

        for (int i = 0; i < components.size(); i++) {
            components.get(i).paint(e);
        }
    }

    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------

    public void addComponent(Container m){
        components.add(m);
    }
    
    public List<Container> getComponents() {
        return this.components;
    }

    public void setComponents(List<Container> components) {
        this.components = components;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
