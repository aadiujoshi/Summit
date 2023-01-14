package summit.gui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.awt.event.MouseEvent;

import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.DBConnection;
import summit.util.GameLoader;
import summit.util.GraphicsScheduler;
import summit.util.ScheduledEvent;

public class SavedGamesSelectionMenu extends Container implements MouseWheelListener{

    //-------------------------------------------------------------------------------------
    private class WorldSaveBox extends TextContainer{
        private String saveName;
        private String saveKey;

        public WorldSaveBox(Container parent, Window window, 
                                float relX, float relY, 
                                String saveName, String saveKey) {

            super(saveName, parent, window, relX, relY, Sprite.TEXTBOX1);
            super.setSplit(false);
            this.saveName = saveName;
            this.saveKey = saveKey;
        }
        
        @Override
        public void guiClick(MouseEvent e){
            getParentWindow().loadWorld(saveKey, saveName);
        }
    }

    private class DeleteWorldBox extends TextContainer{

        //goes back to 0 after 2 second of unsuccessive clicks
        private int sClicks = 0;

        private String saveName;
        private String saveKey;

        public DeleteWorldBox(Container parent, Window window, 
                                float relX, float relY, 
                                String saveName, String saveKey) {

            super("X", parent, window, relX, relY, Sprite.MENUBOX5);
            super.setOutline(true);
            super.setFilter(new ColorFilter(255,-255,-255));
            super.setTextFilter(new ColorFilter(0xffffff));
            this.saveName = saveName;
            this.saveKey = saveKey;
        }
        
        @Override
        public void guiClick(MouseEvent e){
            sClicks++;
            
            if(sClicks == 1){
                popup = "Press again to delete " + saveName;

                GraphicsScheduler.registerEvent(new ScheduledEvent(2000, 1) {
                    @Override
                    public void run() {
                        popup = "";
                        sClicks = 0;
                    }
                });
            }

            if(sClicks == 2){
                DBConnection.removeSave(saveKey);

                popup = "Deleted " + saveName;

                createSaveBoxes();

                GraphicsScheduler.registerEvent(new ScheduledEvent(2000, 1) {
                    @Override
                    public void run() {
                        popup = "";
                        sClicks = 0xbeef;
                    }
                });
            }
        }
    }
    //-------------------------------------------------------------------------------------

    private int offsetY;

    private boolean connected = DBConnection.connect();

    private String popup = "";

    // private int offsetYmax;

    public SavedGamesSelectionMenu(Window window) {
        super(null, window, 0.5f, 0.5f, Sprite.SUMMIT_BACKGROUND2);
        super.setOutline(false);

        createSaveBoxes();
    }

    private void createSaveBoxes(){
        getComponents().clear();

        //RESET CONNECTION BUTTON -------------------------------------------------
        TextContainer retry = new TextContainer("reload", this, getParentWindow(), 0.13f, 0.85f, Sprite.MENUBOX4){
            @Override
            public synchronized void guiClick(MouseEvent e){
                GameLoader.logger.log("Retrying database connection...");
                setTextFilter(new ColorFilter(0xff0000));
                connected = DBConnection.connect();

                createSaveBoxes();
                setTextFilter(new ColorFilter(0x00ff00));
            }
        };
        retry.setTextFilter(new ColorFilter(0x00ff00));
        addComponent(retry);
        //------------------------------------------------------------------

        //LOAD FROM CACHE BUTTON -------------------------------------------------
        TextContainer cache = new TextContainer("cache", this, getParentWindow(), 0.13f, 0.75f, Sprite.MENUBOX4){
            @Override
            public synchronized void guiClick(MouseEvent e){
                GameLoader.logger.log("Loading world save from cache...");
                setTextFilter(new ColorFilter(0xff0000));

                getParentWindow().loadCache();

                setTextFilter(new ColorFilter(0x00ff00));
            }
        };
        cache.setTextFilter(new ColorFilter(0x00ff00));
        addComponent(cache);
        //------------------------------------------------------------------

        HashMap<String, String> saves = GameLoader.getSaves();

        float ry = 0.2f;

        for (var save : saves.entrySet()) {
            WorldSaveBox w = new WorldSaveBox(this, getParentWindow(), 0.5f, ry, save.getValue(), save.getKey());
            DeleteWorldBox dw = new DeleteWorldBox(this, getParentWindow(), 0.8f, ry, save.getValue(), save.getKey());

            addComponent(w);
            addComponent(dw);

            ry+=0.2f;
        }

        //send to children components
        setParentWindow(getParentWindow());
    }

    
    /** 
     * @param e
     */
    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        if(!connected){
            e.getRenderer().renderText("Database connecion Error!", 
                    128, 60, Renderer.NO_OP, new ColorFilter(0xffffff));
            e.getRenderer().renderText("Check server logs", 
                    128, 72, Renderer.NO_OP, new ColorFilter(0xffffff));
            e.getRenderer().renderText("for more information", 
                    128, 84, Renderer.NO_OP, new ColorFilter(0xffffff));
        }

        if(getComponents().size()-2 == 0 && connected){
            e.getRenderer().renderText("No saved worlds found...", 
                    128, 65, Renderer.NO_OP, new ColorFilter(0xffffff));
            e.getRenderer().renderText(" create new world", 
                    128, 78, Renderer.NO_OP, new ColorFilter(0xffffff));
        }

        e.getRenderer().renderText(popup, 128, 8, Renderer.NO_OP, new ColorFilter(0));
    }

    
    /** 
     * @param e
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float m = (float)e.getPreciseWheelRotation();

        // System.out.println(offsetYmax + "  " + offsetY);

        if(offsetY - 5*m <= 0)// && offsetY - 5*m <= offsetYmax)
            offsetY-=m*5;
        else 
            return;
            
        var comp = getComponents();

        for (int i = 0; i < getComponents().size(); i++) {
            Container c = comp.get(i);

            if(c instanceof WorldSaveBox || c instanceof DeleteWorldBox){
                c.setY(c.getY() - 5*m);
            }
        }
    }
}
