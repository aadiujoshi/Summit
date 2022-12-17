/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import summit.game.GameUpdateEvent;
import summit.game.GameWorld;
import summit.game.gamemap.GameMap;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Controls;
import summit.util.GameLoader;
import summit.util.Scheduler;
import summit.util.Settings;
import summit.util.Time;

public class Window implements MouseListener, KeyListener{
    
    private String title;

    private JFrame frame;
    private Canvas canvas;
    private Renderer renderer;

    private boolean fullscreen = false;

    //-----------------------
    public float fps;
    private long lastFrame;
    private boolean availableClick;
    //-----------------------

    // public static final int SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    // public static final int SCREEN_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private volatile boolean closed = false;
    private static boolean mouseDown = false;

    private int width;
    private int height;
    
    private BufferedImage finalFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

    private Stack<Container> guiContainersHome;
    private Stack<Container> guiContainersGame;
    private WindowState state;

    private GameWorld world;

    private BufferStrategy bufferStrategy;

    private Thread graphicsThread;
    private Thread schedulerThread;

    //------------------------------------------
    private MainSelectionMenu mainMenu;
    private VideoSettings settings;
    //------------------------------------------

    public Window(String title){

        this.title = title;

        width = SCREEN_WIDTH;
        height = SCREEN_HEIGHT;
        
        guiContainersHome = new Stack<>();
        guiContainersGame = new Stack<>();

        int _t = ((int)Settings.getSetting("threads") == 0) ? 1 : (int)Settings.getSetting("threads");

        renderer = new Renderer(_t, SCREEN_WIDTH, SCREEN_HEIGHT);

        mainMenu = new MainSelectionMenu(this);
        settings = new VideoSettings(this);

        schedulerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!closed)
                    Scheduler.checkEvents();
                
                System.out.println("Scheduler Thread Terminated");
            }
        });

        graphicsThread = new Thread(new Runnable() {

            long lastFpsUpdate = Time.timeMs();
            
            @Override
            public void run(){
                lastFrame = Time.timeMs();
                while(!closed){
                    Graphics2D g = null;
                    do {
                        try{
                            long startFrame = Time.timeNs();
                            
                            g = (Graphics2D)bufferStrategy.getDrawGraphics();

                            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                            
                            {
                                renderFrame(g);
                            }

                            if(Time.timeMs()-lastFpsUpdate > 500){
                                float fps_ = (Time.MS_IN_S/((Time.timeNs()-startFrame)/1000000f));

                                frame.setTitle(title + " | FPS: " + fps_);

                                // fCount++;
                                lastFpsUpdate = Time.timeMs();
                            }
                            lastFrame = startFrame;

                        } finally { g.dispose(); } 
                        try { bufferStrategy.show(); } 
                        catch(java.lang.IllegalStateException e) { }
                    } while (bufferStrategy.contentsLost());
                }

                System.out.println("Graphics Thread Terminated");
            }
        });

        //init window frame, canvas, and buffer strategy
        {
            frame = new JFrame(title + "|");
            frame.getContentPane().setPreferredSize(new Dimension(width, height));
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setResizable(true);
            frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            // frame.setMaximumSize(new java.awt.Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            // frame.revalidate();

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);

                    closed = true;

                    onQuit();
                }

                @Override
                public void windowGainedFocus(WindowEvent e) {
                    super.windowGainedFocus(e);
                }

                @Override
                public void windowLostFocus(WindowEvent e) {
                    super.windowLostFocus(e);
                }
            });

            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    frame.validate();
                    width = canvas.getWidth();
                    height = canvas.getHeight();
                }
            });

            GraphicsConfiguration graphicsConfiguration =
                    GraphicsEnvironment
                            .getLocalGraphicsEnvironment()
                            .getDefaultScreenDevice()
                            .getDefaultConfiguration();

            canvas = new Canvas(graphicsConfiguration);

            canvas.setIgnoreRepaint(true);
            canvas.setPreferredSize(new Dimension(width, height));
            canvas.setSize(width, height);
            canvas.setFocusable(true);

            canvas.addKeyListener(this);
            canvas.addMouseListener(this);

            frame.add(canvas);

            frame.pack();
            frame.setLocationRelativeTo(null);

            canvas.createBufferStrategy(2);
            bufferStrategy = canvas.getBufferStrategy();

            frame.revalidate();

            frame.setVisible(true);

        }
        
        graphicsThread.start();
        schedulerThread.start();

        System.out.println("Threads in use: " + (Thread.activeCount()+1) + "\n");

        Time.nanoDelay(Time.NS_IN_MS*100);

        this.setState(WindowState.SELECTIONMENUS);
    }

    private void renderFrame(Graphics2D g){
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        PaintEvent pe = new PaintEvent(world, this, renderer, lastFrame, mouseX(), mouseY());
        
        OrderPaintEvent ope = new OrderPaintEvent(new RenderLayers(10), null);
        
        if(state != WindowState.GAME){
            renderer.render(Sprite.SUMMIT_BACKGROUND, Renderer.WIDTH/2, Renderer.HEIGHT/2, Renderer.NO_OP, null);
            
            if (!guiContainersHome.isEmpty())
                guiContainersHome.peek().paint(pe);
        }
        else if(state == WindowState.GAME){
            if(world != null)
                world.setRenderLayer(ope);
                
            try{
                ope.getRenderLayers().renderLayers(pe);
            } catch (NullPointerException e) {
                System.out.println("Null Camera");
            }

            if(!guiContainersGame.isEmpty())
                guiContainersGame.peek().paint(pe);
        }
        
        //----------------------------------------------------------------------------------
        // draw final frame to screen
        //----------------------------------------------------------------------------------
        
        renderer.upscaleToImage(finalFrame);
        
        g.drawImage(finalFrame, null, 0, 0);
        
        renderer.resetFrame();
    }

    public void setState(WindowState newState){
        if(state == newState) return;

        if(newState == WindowState.SELECTIONMENUS){
            clearHomeContainers();
            clearGameContainers();
            pushHomeContainer(mainMenu);
            state = newState;
            return;
        }

        if(newState == WindowState.SETTINGS){
            pushHomeContainer(settings);
            state = WindowState.SETTINGS;
            return;
        }

        if(newState == WindowState.NEWGAME){
            world = new GameWorld("ihatetiktok", this, 3L);
            state = WindowState.GAME;
            return;
        }

        if(newState == WindowState.SAVEDGAME){
            world = GameLoader.loadWorld("Summit/gamesaves/testsave1.txt");
            world.reinit(this, "Summit/gamesaves/testsave1.txt");
            world.pause();

            state = WindowState.GAME;
            return;
        }

        if(newState == WindowState.BACK){
            switch(state){
                case SETTINGS:
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case GAME:
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case NEWGAME:
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case SAVEDGAME:
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case SELECTIONMENUS:
                    //do nothing
                    break;
            }
        }
    }

    private void onQuit(){
        //terminate upscaling threads
        renderer.terminate();

        if(world != null){
            GameLoader.saveWorld(world, "Summit/gamesaves/testsave1.txt");
        }
    } 


    /**
     * Closes the game, and disposes of all active threads. 
     * If a game is in session, it is automatically saved.
     */
    public void quit(){
        closed = true;
        onQuit();
        canvas.setVisible(false);
        frame.setVisible(false);
        frame.dispose();
    }
    
    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------

    /**
     * @return If this Window has been closed
     */
    public synchronized boolean isClosed() {
        return this.closed;
    }

    /**
     * This method is called by GameUpdateEvent to simulate a mouse click for 
     * GameUpdateRecievers
     * 
     * @return If the mouse has been clicked
     */
    public boolean availableClick(){
        boolean tmp = availableClick;
        availableClick = false;
        return tmp;
    }

    public void pushHomeContainer(Container cont){
        if(cont.isPushed())
            return;
        
        guiContainersHome.push(cont);
        cont.setPushed(true);
        cont.setParentWindow(this);
    }

    public void popHomeContainer(){
        guiContainersHome.peek().close();
        guiContainersHome.pop().setPushed(false);
    }

    public void clearHomeContainers(){
        for (Container container : guiContainersHome) {
            guiContainersHome.peek().close();
            container.setPushed(false);
        }
        guiContainersHome.clear();
    }

    public void pushGameContainer(Container cont){
        if(cont.isPushed())
            return;
        guiContainersGame.push(cont);
        cont.setPushed(true);
        cont.setParentWindow(this);
    }

    public void popGameContainer(){
        if(!guiContainersGame.isEmpty()){
            guiContainersGame.peek().close();
            guiContainersGame.pop().setPushed(false);
        }
    }

    public void clearGameContainers(){
        for(Container container : guiContainersHome) {
            guiContainersGame.peek().close();
            container.setPushed(false);
        }
        guiContainersGame.clear();
    }

    public static boolean mouseDown(){
        return mouseDown;
    }

    public int mouseX(){
        return (java.awt.MouseInfo.getPointerInfo().getLocation().x-frame.getLocation().x) / 
                (SCREEN_WIDTH/Renderer.WIDTH);
    }

    public int mouseY(){
        return (java.awt.MouseInfo.getPointerInfo().getLocation().y-frame.getLocation().y) / 
                (SCREEN_HEIGHT/Renderer.HEIGHT);
    }

    public void setFullscreen(boolean full) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                fullscreen = full;
                if(fullscreen){
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
                }
                else if(!fullscreen){
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
                    frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                }
            }
        });
    }

    //-------------------------------------------------------------------
    // Key Events
    //-------------------------------------------------------------------

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_F11){
            setFullscreen(!fullscreen);
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            if(state == WindowState.GAME){
                if(!this.guiContainersGame.isEmpty())
                    this.popGameContainer();
            } else {
                this.setState(WindowState.BACK);
            }
        }

        Controls.setPress(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Controls.setRelease(e);
    }

    //-------------------------------------------------------------------
    // Mouse Events
    //-------------------------------------------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        this.availableClick = true;

        int rx = e.getX()/(SCREEN_WIDTH/Renderer.WIDTH);
        int ry = (e.getY())/(SCREEN_HEIGHT/Renderer.HEIGHT);
        
        e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiersEx(), rx, ry, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        
        if(state == WindowState.GAME){
            if(world != null){
                GameMap loadedmap = world.getLoadedMap();
                loadedmap.gameClick(new GameUpdateEvent(world));
                this.availableClick = true;
            }
            
            if(!guiContainersGame.isEmpty()){
                if(guiContainersGame.peek().contains(rx, ry)){
                    guiContainersGame.peek().guiClick(e);
                    this.availableClick = false;
                }
            }
        }
        else {
            if(!guiContainersHome.isEmpty()){
                if(guiContainersHome.peek().contains(rx, ry)){
                    guiContainersHome.peek().guiClick(e);
                    this.availableClick = false;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}

