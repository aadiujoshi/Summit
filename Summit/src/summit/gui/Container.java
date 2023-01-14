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
import summit.util.Region;

public class Container extends Region implements Paintable, GUIClickReciever{

    private List<Container> components;
    private Container parent;
    private transient Window window;

    private ColorFilter filter = ColorFilter.NOFILTER;

    private String guiSprite;

    private boolean pushed;

    private boolean outline;

    //if can be removed by hitting esc key
    private boolean navContainer;
    
    public Container(Container parent, Window window, float relX, float relY, String guiSprite){
        super( (parent == null) ?
                new Region(((int)(Renderer.WIDTH*relX)), 
                            ((int)(Renderer.HEIGHT*relY)), 
                            BufferedSprites.getSprite(guiSprite)[0].length, 
                            BufferedSprites.getSprite(guiSprite).length) :
                new Region(
                            (parent.getWidth()*relX)  +  (parent.getX()-(parent.getWidth()/2)), 
                            (parent.getHeight()*relY)  +  (parent.getY()-(parent.getHeight()/2)), 
                            BufferedSprites.getSprite(guiSprite)[0].length, 
                            BufferedSprites.getSprite(guiSprite).length) );

        components = new ArrayList<>();
        this.outline = true;
        this.navContainer = true;
        this.parent = parent;
        this.guiSprite = guiSprite;
    }

    
    /** 
     * @param e
     */
    @Override
    public void guiClick(MouseEvent e) {
        for(int i = 0; i < components.size(); i++){
            if(components.get(i).contains(e.getX(), e.getY())){
                components.get(i).guiClick(e);
            }
        }
    }

    public void close(){
    }

    
    /** 
     * @param e
     */
    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        //conainers are always rendered on the top layer, in order
    }

    
    /** 
     * @param e
     */
    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().render(guiSprite, (int)getX(), (int)getY(), 
                    (this.contains(e.mouseX(), e.mouseY()) && outline() ? Renderer.OUTLINE_BLUE | 
                                                            Renderer.OUTLINE_GREEN | 
                                                            Renderer.OUTLINE_RED : 
                                                            Renderer.NO_OP), 
                    filter);
        paintComponents(e);
    }

    
    /** 
     * @param e
     */
    protected void paintComponents(PaintEvent e){
        for (int i = 0; i < components.size(); i++) {
            components.get(i).paint(e);
        }
    }

    
    /** 
     * @param m
     */
    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------

    public void addComponent(Container m){
        components.add(m);
    }
    
    
    /** 
     * @return A List<Container> containing all the sub containers of this container
     */
    public List<Container> getComponents() {
        return this.components;
    }

    
    /** 
     * @param components
     */
    public void setComponents(List<Container> components) {
        this.components = components;
    }
    
    
    /** 
     * @param window
     */
    public void setParentWindow(Window window){
        this.window = window;
        for (Container container : components) {
            container.setParentWindow(window);
        }
    }

    
    /** 
     * @return Window
     */
    public Window getParentWindow(){
        return this.window;
    }

    
    /** 
     * @param index
     * @return Container
     */
    public Container getComponent(int index){
        return this.components.get(index);
    }
    
    
    /** 
     * @return boolean
     */
    public boolean isPushed() {
        return this.pushed;
    }

    
    /** 
     * @param pushed
     */
    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }
    
    
    /** 
     * @return String
     */
    public String getGuiSprite() {
        return this.guiSprite;
    }

    
    /** 
     * @param guiSprite
     */
    public void setGuiSprite(String guiSprite) {
        this.guiSprite = guiSprite;
    }
    
    
    /** 
     * @return ColorFilter
     */
    public ColorFilter getFilter() {
        return this.filter;
    }

    
    /** 
     * @param filter
     */
    public void setFilter(ColorFilter filter) {
        this.filter = filter;
    }
    
    
    /** 
     * @return boolean
     */
    public boolean isNavContainer() {
        return this.navContainer;
    }

    
    /** 
     * @param navContainer
     */
    public void setNavContainer(boolean navContainer) {
        this.navContainer = navContainer;
    }

    
    /** 
     * @return boolean
     */
    public boolean outline() {
        return this.outline;
    }

    
    /** 
     * @param outline
     */
    public void setOutline(boolean outline) {
        this.outline = outline;
    }
}
