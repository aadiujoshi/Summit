/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import summit.gfx.BufferedSprites;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Region;

//should extend Region class!!!!!!!!!
public class Container extends Region implements Paintable, GUIClickReciever{

    private List<Container> components;
    // private Region region;
    private Container parent;
    private transient Window window;

    private String guiSprite;

    private boolean pushed;

    private boolean visible = true;

    public Container(Container parent, Window window, float relX, float relY, String guiSprite){
        super( (parent == null) ?
                new Region(((int)(Renderer.WIDTH*relX)/16)*16, 
                            ((int)(Renderer.HEIGHT*relY)/16)*16, 
                            BufferedSprites.getSprite(guiSprite)[0].length, 
                            BufferedSprites.getSprite(guiSprite).length) :
                new Region(
                            (parent.getWidth()*relX)  +  (parent.getX()-(parent.getWidth()/2)), 
                            (parent.getHeight()*relY)  +  (parent.getY()-(parent.getHeight()/2)), 
                            BufferedSprites.getSprite(guiSprite)[0].length, 
                            BufferedSprites.getSprite(guiSprite).length) );

        components = new ArrayList<>();
        this.parent = parent;
        this.guiSprite = guiSprite;
    }

    @Override
    public void guiClick(MouseEvent e) {
        for(int i = 0; i < components.size(); i++){
            if(components.get(i).contains(e.getX(), e.getY())){
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
        e.getRenderer().render(guiSprite, (int)getX(), (int)getY(), Renderer.NO_OP, ColorFilter.NOFILTER);
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
    
    public String getGuiSprite() {
        return this.guiSprite;
    }

    public void setGuiSprite(String guiSprite) {
        this.guiSprite = guiSprite;
    }
}
