package summit.gui;

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import summit.game.GameMap;
import summit.gfx.Camera;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Region;

//should extend Region class!!!!!!!!!
public class Container implements Serializable, Paintable, GUIClickReciever{

    private List<Container> components;
    private Region region;
    private Container parent;
    private Window window;

    private boolean pushed;

    private boolean visible = true;

    public Container(Container parent, float relX, float relY, float relWidth, float relHeight){
        components = new ArrayList<>();
        this.parent = parent;
        if(parent == null)
            region = new Region(((int)(Renderer.WIDTH*relX)/16)*16, ((int)(Renderer.HEIGHT*relY)/16)*16, 
                                ((int)(Renderer.WIDTH*relWidth)/16)*16, ((int)(Renderer.HEIGHT*relHeight)/16)*16);
        else {
            float w = ((int)(parent.getWidth()*relWidth)/16)*16;
            float h = ((int)(parent.getHeight()*relHeight)/16)*16;
            float x = (parent.getWidth()*relX);
            float y = (parent.getHeight()*relY);
            
            region = new Region(x+(parent.getX()-(parent.getWidth()/2)), y+(parent.getY()-(parent.getHeight()/2)), w, h);
        }
    }

    @Override
    public void guiClick(MouseEvent e) {
        for(int i = 0; i < components.size(); i++){
            if(components.get(i).getRegion().contains(e.getX(), e.getY())){
                components.get(i).guiClick(e);
            }
        }
    }
    
    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        //conainers are always rendered on the top layer, in order
    }

    @Override
    public void paint(PaintEvent e) {
        if(!visible) return;

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
                
                if(y == startY && x == startX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.NO_OP, null);
                    continue;
                }
                if(y == startY && x == endX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.FLIP_X, null);
                    continue;
                }
                if(y == endY && x == startX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.FLIP_Y, null);
                    continue;
                }
                if(y == endY && x == endX){
                    ren.render(Sprite.MENU_CORNER, x, y, Renderer.FLIP_X | Renderer.FLIP_Y, null);
                    continue;
                }
                //------------

                //check borders
                if(y == startY){
                    ren.render(Sprite.MENU_BORDER, x, y, Renderer.NO_OP, null);
                    continue;
                }
                if(y == endY){
                    ren.render(Sprite.MENU_BORDER, x, y, Renderer.FLIP_Y, null);
                    continue;
                }
                if(x == startX){
                    ren.render(Sprite.MENU_BORDER, x, y,  Renderer.ROTATE_90 | Renderer.FLIP_X, null);
                    continue;
                }
                if(x == endX){
                    ren.render(Sprite.MENU_BORDER, x, y, Renderer.ROTATE_90, null);
                    continue;
                }
                //------------

                //render inside
                ren.render(Sprite.MENU_FILL, x, y, Renderer.NO_OP, null);
            }
        }

        paintComponents(e);
    }

    protected void paintComponents(PaintEvent e){
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

    public void setParentWindow(Window window){
        this.window = window;
        for (Container container : components) {
            container.setParentWindow(window);
        }
    }

    public Window getParentWindow(){
        return this.window;
    }

    public Container getComponent(int index){
        return this.components.get(index);
    }
    
    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isPushed() {
        return this.pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

}
