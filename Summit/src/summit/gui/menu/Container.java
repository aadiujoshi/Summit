package summit.gui.menu;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import summit.game.GameMap;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.GUIClickListener;
import summit.gui.Window;
import summit.util.Region;

public class Container implements Paintable, GUIClickListener{

    private List<Container> components;
    private Region region;
    private Container parent;

    public Container(Container parent, float relX, float relY, float relWidth, float relHeight){
        components = new ArrayList<>();
        this.parent = parent;
        if(parent == null)
            region = new Region(((int)(Renderer.WIDTH*relX)/16)*16, ((int)(Renderer.HEIGHT*relY)/16)*16, 
                                ((int)(Renderer.WIDTH*relWidth)/16)*16, ((int)(Renderer.HEIGHT*relHeight)/16)*16);
        else {
            float w = ((int)(parent.getWidth()*relWidth)/16)*16;
            float h = ((int)(parent.getHeight()*relHeight)/16)*16;
            float x = ((int)(parent.getWidth()*relX)/16)*16;
            float y = ((int)(parent.getHeight()*relY)/16)*16;
            
            region = new Region(x+(parent.getX()-(parent.getWidth()/2)), y+(parent.getY()-(parent.getHeight()/2)), w, h);
        }
        System.out.println(region);
    }

    @Override
    public void guiClick(MouseEvent e) {
        System.out.println("container " + super.hashCode() + " clicked");
        for(int i = 0; i < components.size(); i++){
            if(components.get(i).getRegion().contains(e.getX()/(Window.SCREEN_WIDTH/Renderer.WIDTH), e.getY()/(Window.SCREEN_HEIGHT/Renderer.HEIGHT))){
                components.get(i).guiClick(e);
            }
        }
    }

    @Override
    public void paint(PaintEvent e) {

        Renderer ren = e.getRenderer();

        int rWidth = (int)region.getWidth();
        int rHeight = (int)region.getHeight();

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
    
    public int getX(){
        return (int)this.region.getX();
    }

    public int getY(){
        return (int)this.region.getY();
    }

    public int getWidth(){
        return (int)this.region.getWidth();
    }

    public int getHeight(){
        return (int)this.region.getHeight();
    }
}
