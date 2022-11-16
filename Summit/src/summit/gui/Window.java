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

import summit.game.GameMap;
import summit.game.GameWorld;
import summit.game.animation.Scheduler;
import summit.game.tile.TileStack;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.menu.Container;
import summit.gui.menu.MainSelectionMenu;
import summit.util.Controls;
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

    //-----------------------

    // public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    // public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private boolean closed = false;
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

    
    //------------------------------------------

    public Window(String title){

        this.title = title;

        width = SCREEN_WIDTH;
        height = SCREEN_HEIGHT;
        
        guiContainersHome = new Stack<>();
        guiContainersGame = new Stack<>();

        renderer = new Renderer();

        mainMenu = new MainSelectionMenu();

        schedulerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                    Scheduler.checkEvents();
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
                            // Time.nanoDelay((long)(Time.NS_IN_MS*(1000/144)));
                            long startFrame = Time.timeNs();
                            
                            g = (Graphics2D)bufferStrategy.getDrawGraphics();

                            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                            
                            {
                                renderFrame(g);
                            }

                            // System.out.println(Time.MS_IN_S/((Time.timeNs()-startFrame)/1000000));
                            if(Time.timeMs()-lastFpsUpdate > 500){
                                frame.setTitle(title + " | FPS: " + (Float.toString(Time.MS_IN_S/((Time.timeNs()-startFrame)/1000000f))));
                                lastFpsUpdate = Time.timeMs();
                            }
                            lastFrame = startFrame;

                        } finally { g.dispose(); } 
                        try { bufferStrategy.show(); } 
                        catch(java.lang.IllegalStateException e) { }
                    } while (bufferStrategy.contentsLost());
                }
            }
        });

        //init window frame, canvas, and buffer strategy
        {
            frame = new JFrame(title + "|");
            frame.getContentPane().setPreferredSize(new Dimension(width, height));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            // frame.setMaximumSize(new java.awt.Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            // frame.revalidate();

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    closed = true;
                    super.windowClosing(e);
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
            // canvas.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            // canvas.setMaximumSize(new java.awt.Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            // canvas.revalidate();

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

        this.setState(WindowState.SELECTIONMENUS);
    }

    private void renderFrame(Graphics2D g){
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // System.out.println(state);

        PaintEvent pe = new PaintEvent(renderer, lastFrame, null, this);

        if(state == WindowState.SELECTIONMENUS){
            renderer.render(Sprite.SUMMIT_BACKGROUND, Renderer.WIDTH/2, Renderer.HEIGHT/2, Renderer.NO_OP, null);
            
            if (!guiContainersHome.isEmpty())
                guiContainersHome.peek().paint(pe);
        }
        else if(state == WindowState.GAME){
            if(world != null)
                world.paint(pe);
        }

        pe.invokeDelayed();

        //----------------------------------------------------------------------------------
        // draw final frame to screen
        //----------------------------------------------------------------------------------
        renderer.upscaleToImage(finalFrame);
        
        g.drawImage(finalFrame, null, 0, 0);
        
        // renderer.resetFrame();
    }


    public void setState(WindowState newState){
        if(state == newState) return;

        if(newState == WindowState.SELECTIONMENUS){
            clearHomeContainers();
            pushHomeContainer(mainMenu);
            state = newState;
            return;
        }

        if(newState == WindowState.NEWGAME){
            world = new GameWorld(this, 2L);
            world.setGameContainers(guiContainersGame);
            state = WindowState.GAME;
            return;
        }

        if(newState == WindowState.SAVEDGAME){
            //idk yet
            return;
        }

        if(newState == WindowState.BACK){
            switch(state){
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

    public void quit(){
        closed = true;
        canvas.setVisible(false);
        frame.setVisible(false);
        frame.dispose();
    }
    
    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------

    /**
     * Used for anonymous classes
     * @return This window instance
     */
    private Window getThis(){
        return this;
    }

    public void pushHomeContainer(Container cont){
        guiContainersHome.push(cont);
        cont.setParentWindow(this);
    }

    public void popHomeContainer(){
        guiContainersHome.pop();
    }

    public void clearHomeContainers(){
        guiContainersHome.clear();
    }

    public void pushGameContainer(Container cont){
        guiContainersGame.push(cont);
        cont.setParentWindow(this);
    }

    public void popGameContainer(){
        guiContainersGame.pop();
    }

    public void clearGameContainers(){
        guiContainersGame.clear();
    }

    public static boolean mouseDown(){
        return mouseDown;
    }

    public void setFullscreen(boolean full) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                fullscreen = full;
                if(fullscreen){
                    frame.setResizable(true);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
                }
                else if(!fullscreen){
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
                    frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                    frame.setResizable(false);
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

        int rx = e.getX()/(SCREEN_WIDTH/Renderer.WIDTH);
        int ry = e.getY()/(SCREEN_HEIGHT/Renderer.HEIGHT);

        e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiersEx(), rx, ry, e.getClickCount(), e.isPopupTrigger(), e.getButton());

        if(state == WindowState.GAME){
            if(world != null){
                GameMap loadedmap = world.getLoadedMap();
                // Renderer.toTile();
                System.out.println(Renderer.toTile(rx, ry, world.getCamera()));
                // loadedmap.getTileAt(rx, ry);
            }
            
            for(Container container : guiContainersGame) {
                if(container.getRegion().contains(rx, ry)){
                    container.guiClick(e);
                }
            }
        }
        else {
            if(!guiContainersHome.isEmpty()){
                if(guiContainersHome.peek().getRegion().contains(rx, ry)){
                    guiContainersHome.peek().guiClick(e);
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

