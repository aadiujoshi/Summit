package summit.gui;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.awt.event.KeyAdapter;
import java.awt.image.DataBufferInt;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import summit.game.GameMap;
import summit.game.GameWorld;
import summit.game.tile.TileStack;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.menu.Container;
import summit.gui.menu.MainSelectionMenu;
import summit.util.Controls;
import summit.util.Time;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Window implements MouseListener, KeyListener{
    
    private JFrame frame;
    private Canvas canvas;
    private Renderer renderer;

    private boolean fullscreen = false;

    //-----------------------
    public float fps;
    private long lastFrame;

    //-----------------------

    public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    // public static final int SCREEN_WIDTH = 1920;
    // public static final int SCREEN_HEIGHT = 1080;

    private boolean closed = false;
    private boolean mouseDown = false;

    private int width;
    private int height;

    private BufferedImage bg;

    private BufferedImage finalFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

    private Stack<Container> guiContainersHome;
    private Stack<Container> guiContainersGame;
    private WindowState state;

    private GameWorld world;

    private BufferStrategy bufferStrategy;

    private Thread graphicsThread;
    
    //------------------------------------------
    private MainSelectionMenu mainMenu;

    
    //------------------------------------------

    public Window(String title, int w, int h){

        width = w;
        height = h;
        
        guiContainersHome = new Stack<>();
        guiContainersGame = new Stack<>();

        renderer = new Renderer();

        mainMenu = new MainSelectionMenu();


        // world = new GameWorld(this);

        try {
            bg = ImageIO.read(new File("Summit/Summit/src/summit/deprecated/extra/gradient-background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphicsThread = new Thread(new Runnable() {
            
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
                            // g.setRenderingHint(RenderingHints.);

                            {
                                renderFrame(g);
                            }

                            // System.out.println(Time.MS_IN_S/((Time.timeNs()-startFrame)/1000000));
                            frame.setTitle(Long.toString(Time.MS_IN_S/((Time.timeNs()-startFrame)/1000000)));
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
            frame = new JFrame(title);
            frame.getContentPane().setPreferredSize(new Dimension(width, height));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);

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

            frame.setVisible(true);

            graphicsThread.start();
        }
        
        this.setState(WindowState.SELECTIONMENUS);
    }
    
    private void renderFrame(Graphics2D g){
        
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        if(state == WindowState.SELECTIONMENUS){
            PaintEvent pe = new PaintEvent(renderer, lastFrame, null);
            renderer.renderImage(bg, Renderer.WIDTH/2, Renderer.HEIGHT/2);
            if (!guiContainersHome.isEmpty())
                guiContainersHome.peek().paint(pe);
        }
        else if(state == WindowState.GAME){
            //camera is set by gameworld
            PaintEvent pe = new PaintEvent(renderer, lastFrame, null);
            if(world != null)
                world.paint(pe);
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
            pushHomeContainer(mainMenu);
            state = newState;
            return;
        }

        if(newState == WindowState.NEWGAME){
            world = new GameWorld(this, 69);
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
                    frame.setSize(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
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
        
        float rx = e.getX()/(SCREEN_WIDTH/Renderer.WIDTH);
        float ry = e.getY()/(SCREEN_HEIGHT/Renderer.HEIGHT);

        e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)rx, (int)ry, e.getClickCount(), e.isPopupTrigger(), e.getButton());

        if(state == WindowState.GAME){
            // if(world != null){
            //     GameMap loadedmap = world.getLoadedMap();
            //     if(loadedmap != null){
            //         TileStack[][] map = loadedmap.getMap();
            //         for (int i = 0; i < map.length; i++) {
            //             for (int j = 0; j < map[0].length; j++) {
            //                 // if(){

            //                 // }
            //             }
            //         }
            //     }
            // }
            
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
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}

