
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
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import summit.Main;
import summit.game.GameUpdateEvent;
import summit.game.GameWorld;
import summit.game.gamemap.GameMap;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Controls;
import summit.util.DBConnection;
import summit.util.GameLoader;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;
import summit.util.Settings;
import summit.util.Time;

/**
 * A class representing a window that displays graphics and handles mouse and
 * keyboard events.
 * The window implements the MouseListener and KeyListener interfaces to receive
 * mouse and
 * keyboard events.
 *
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Window implements MouseListener, KeyListener {
    /** The title of the window displayed in the title bar. */
    private String title;

    /** The JFrame object representing the window.*/
    private JFrame frame;

    /** The Canvas object for drawing graphics on the window.*/
    private Canvas canvas;

    /** The Renderer object for rendering graphics to the Canvas.*/
    private Renderer renderer;

    /** A boolean flag indicating whether the window is in fullscreen mode.*/
    private boolean fullscreen;

    /** The frames per second (fps) of the window. */
    public float fps;

    /** The timestamp of the last frame.*/
    private long lastFrame;

    /**A boolean flag indicating whether a click is available.*/
    private boolean availableClick;

    /** The width of the window.*/
    public static final int SCREEN_WIDTH = 1280;

    /** The height of the window.*/
    public static final int SCREEN_HEIGHT = 720;

    /**A boolean flag indicating whether the window is closed.*/
    private volatile boolean closed;

    /**Prevents the {@code onQuit()} method from being called several times by multiple "close window" requests*/
    private volatile boolean validateClosed;

    /** A boolean flag indicating whether the mouse is down.*/
    private static boolean mouseDown;

    /**
     * The width of the window.
     */
    private int width;

    /**
     * The height of the window.
     */
    private int height;

    /**
     * The final frame as a BufferedImage object.
     */
    private BufferedImage finalFrame;

    /**
     * A stack of Container objects representing the home GUI containers.
     */
    private Stack<Container> guiContainersHome;

    /**
     * A stack of Container objects representing the game GUI containers.
     */
    private Stack<Container> guiContainersGame;

    /**
     * The state of the window.
     */
    private WindowState state;

    /**
     * The GameWorld object representing the game world.
     */
    private GameWorld world;

    /**
     * The BufferStrategy object for the window.
     */
    private BufferStrategy bufferStrategy;

    /**
     * The graphics thread for the window.
     */
    private Thread graphicsThread;

    /**
     * The game scheduler thread for the window.
     */
    private Thread gameSchedulerThread;

    /**
     * The graphics scheduler thread for the window.
     */
    private Thread graphicsSchedulerThread;

    /**
     * The MainSelectionMenu object for the main menu.
     */
    private MainSelectionMenu mainMenu;

    /**
     * The Saved Games Selection Menu to load a previous game from the database
     */
    private SavedGamesSelectionMenu selMenu;

    /**
     * The Saved Games Selection Menu to load a previous game from the database
     */
    private GameCreationMenu createMenu;

    /**
     * The SettingsGUI object for the video and game settings.
     */
    private SettingsGUI settings;

    /**
     * Used for transtioning between Window States
     * 
     * @see Window#transition(TransitionScreen)
     * @see Window#endTransition(WindowState)
     */
    private volatile TransitionScreen transition;

    /**
     * Constructs a new Window with the given title. Initializes the frame,
     * canvas, and buffer strategy for the window. Sets up event listeners
     * for the frame and canvas. Creates a new main menu and starts the
     * scheduler and graphics threads.
     * 
     * @param title The title of the window.
     */
    public Window(String title) {

        this.title = title;

        width = SCREEN_WIDTH;
        height = SCREEN_HEIGHT;

        finalFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        guiContainersHome = new Stack<>();
        guiContainersGame = new Stack<>();

        //miniumin 1 thread
        int _t = ((int) Settings.getSetting("threads") == 0) ? 1 : (int) Settings.getSetting("threads");
        renderer = new Renderer(_t, SCREEN_WIDTH, SCREEN_HEIGHT);

        mainMenu = new MainSelectionMenu(this);
        selMenu = new SavedGamesSelectionMenu(this);
        settings = new SettingsGUI(this);
        createMenu = new GameCreationMenu(this);
        transition = null;

        gameSchedulerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!closed)
                    GameScheduler.checkEvents();

                System.out.println("Scheduler1 Thread Terminated");
            }
        });

        graphicsSchedulerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!closed)
                    GraphicsScheduler.checkEvents();

                System.out.println("Scheduler2 Thread Terminated");
            }
        });

        graphicsThread = new Thread(new Runnable() {

            long lastFpsUpdate = Time.timeMs();

            @Override
            public void run() {
                lastFrame = Time.timeMs();
                while (!closed) {
                    Graphics2D g = null;
                    do {
                        try {
                            long startFrame = Time.timeNs();

                            g = (Graphics2D) bufferStrategy.getDrawGraphics();

                            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                            {
                                // try{
                                renderFrame(g);
                                // } catch(Exception e){
                                // System.out.println("Render error: " + e);
                                // }
                            }

                            if (Time.timeMs() - lastFpsUpdate > 500) {
                                float fps_ = (Time.MS_IN_S / ((Time.timeNs() - startFrame) / 1000000f));

                                frame.setTitle(title + " | FPS: " + fps_);

                                // fCount++;
                                lastFpsUpdate = Time.timeMs();
                            }
                            lastFrame = startFrame;

                        } finally {
                            g.dispose();
                        }
                        try {
                            bufferStrategy.show();
                        } catch (java.lang.IllegalStateException e) {
                        }
                    } while (bufferStrategy.contentsLost());
                }

                System.out.println("Graphics Thread Terminated");
            }
        });

        // init window frame, canvas, and buffer strategy
        {
            frame = new JFrame(title + "|");
            frame.getContentPane().setPreferredSize(new Dimension(width, height));
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setResizable(true);
            frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

            try {
                frame.setIconImage(ImageIO.read(new File(Main.path + "resources/sprites/ice-staff.png")));
            } catch (IOException e1) {
                System.out.println("Game Icon not found");
            }

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);

                    onQuit();
                    
                    closed = true;
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

            GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment
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

        frame.addMouseWheelListener(selMenu);

        graphicsThread.start();
        gameSchedulerThread.start();
        graphicsSchedulerThread.start();

        System.out.println("Threads in use: " + (Thread.activeCount() + 1) + "\n");

        Time.nanoDelay(Time.NS_IN_MS * 100);

        this.setState(WindowState.SELECTIONMENUS);
    }
    
    /**
     * Renders the current frame to the window.
     * 
     * @param g the Graphics2D object for rendering to the window
     */
    private void renderFrame(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        PaintEvent pe = new PaintEvent(world, this, renderer, lastFrame, mouseX(), mouseY());

        OrderPaintEvent ope = new OrderPaintEvent(new RenderLayers(10), null);

        if (state != WindowState.GAME) {
            renderer.render(Sprite.SUMMIT_BACKGROUND1, Renderer.WIDTH / 2, Renderer.HEIGHT / 2, Renderer.NO_OP, null);

            if (!guiContainersHome.isEmpty())
                guiContainersHome.peek().paint(pe);
        } else if (state == WindowState.GAME) {

            if (world != null)
                world.setRenderLayer(ope);

            ope.getRenderLayers().renderLayers(pe);
            
            if (!guiContainersGame.isEmpty())
                guiContainersGame.peek().paint(pe);
        }

        //superimpose transition screen
        if(transition != null)
            transition.paint(pe);

        // ----------------------------------------------------------------------------------
        // draw final frame to screen
        // ----------------------------------------------------------------------------------

        renderer.upscaleToImage(finalFrame);

        g.drawImage(finalFrame, null, 0, 0);

        renderer.resetFrame();
    }

    public void loadCache(){
        this.transition(new TransitionScreen(this, "Loading world from cache..."));

        world = GameLoader.loadCache();

        if(world == null){
            System.out.println("Could not load cached save... check server_logs");
            this.endTransition();
            return;
        }

        System.out.println("Successfully loaded world: " + world.getName() + " [Key:" + world.getSaveKey() + "]");

        world.reinit(this);
        this.endTransition();
        state = WindowState.GAME;
    }

    /**
     * Retrieves the {@code GameWorld} associated with the {@code saveKey} from the database,
     * and sets that as the current game session, ready to play on
     * 
     * @param saveKey
     */
    public void loadWorld(String saveKey, String saveName){

        this.transition(new TransitionScreen(this, "Loading world..."));

        world = GameLoader.loadWorld(saveKey);

        if(world == null){
            System.out.println(saveName + ", " + saveKey + ", does not exist");
            this.endTransition();
            return;
        }

        System.out.println("Successfully loaded world: " + world.getName() + " [Key:" + saveKey + "]");

        world.reinit(this);
        this.endTransition();
        state = WindowState.GAME;
    }

    public void createWorld(String name){
        transition(new TransitionScreen(this, "CREATING WORLD"));

        world = new GameWorld(name, this, (long)(Math.random()*Long.MAX_VALUE));
        GameLoader.createSave(world.getSaveKey(), world.getName());
        
        endTransition();
        state = WindowState.GAME;
    }

    /**
     * Sets the state of the window.
     * 
     * @param newState the new state of the window
     */
    public void setState(WindowState newState) {
        if (state == newState)
            return;

        if (newState == WindowState.SELECTIONMENUS) {
            clearHomeContainers();
            clearGameContainers();
            pushHomeContainer(mainMenu);

            if(world != null){
                world.terminate();
            }
            state = newState;
            return;
        }

        if (newState == WindowState.SETTINGS) {
            pushHomeContainer(settings);
            state = WindowState.SETTINGS;
            return;
        }

        if(newState == WindowState.NEWGAME){
            pushHomeContainer(createMenu);
            state = WindowState.NEWGAME;
            return;
        }

        if(newState == WindowState.SAVEDGAMESELECTION){
            pushHomeContainer(selMenu);

            state = WindowState.SAVEDGAMESELECTION;
            return;
        }

        if (newState == WindowState.BACK) {
            switch (state) {
                case SETTINGS:
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case GAME:
                    world.terminate();
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case NEWGAME:
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case SAVEDGAMESELECTION:
                    setState(WindowState.SELECTIONMENUS);
                    break;
                case SELECTIONMENUS:
                    // do nothing
                    break;
            }
        }
    }

    /**
     * Performs tasks necessary when quitting the game.
     */
    private void onQuit() {
        //if true, this method has already been called
        if(validateClosed)
            return;

        //flag this method
        validateClosed = true;
        
        if(world != null && state == WindowState.GAME){
            world.terminate();
            GameLoader.saveWorld(world);
        }
        DBConnection.updateSettings();
        DBConnection.closeConnection();
        
        // terminate upscaling threads
        renderer.terminate();
    }

    /**
     * Closes the game, and disposes of all active threads.
     * If a game is in session, it is automatically saved.
     */
    public void manualQuit() {
        
        onQuit();

        closed = true;

        canvas.setVisible(false);
        frame.setVisible(false);
        frame.dispose();
    }

    // --------------------------------------------------------------------
    // getters and setters
    // --------------------------------------------------------------------

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
    public boolean availableClick() {
        boolean tmp = availableClick;
        availableClick = false;
        return tmp;
    }

    /**
     * Pushes a Container to the home GUI containers stack.
     *
     * @param cont the Container to push
     */
    public void pushHomeContainer(Container cont) {
        if (cont.isPushed())
            return;

        guiContainersHome.push(cont);
        cont.setPushed(true);
        cont.setParentWindow(this);
    }

    /**
     * Pops the top Container from the home GUI containers stack.
     */
    public void popHomeContainer() {
        if (!guiContainersHome.peek().isNavContainer())
            return;

        guiContainersHome.peek().close();
        guiContainersHome.pop().setPushed(false);
    }

    /**
     * Clears all Containers from the home GUI containers stack.
     */
    public void clearHomeContainers() {
        for (Container container : guiContainersHome) {
            guiContainersHome.peek().close();
            container.setPushed(false);
        }
        guiContainersHome.clear();
    }

    /**
     * Pushes a Container to the game GUI containers stack.
     *
     * @param cont the Container to push
     */
    public void pushGameContainer(Container cont) {
        if (cont.isPushed())
            return;
        guiContainersGame.push(cont);
        cont.setPushed(true);
        cont.setParentWindow(this);
    }

    /**
     * Pops the top Container from the game GUI containers stack.
     */
    public void popGameContainer() {
        if (!guiContainersGame.isEmpty()) {
            if (!guiContainersHome.peek().isNavContainer())
                return;

            guiContainersGame.peek().close();
            guiContainersGame.pop().setPushed(false);
        }
    }

    /**
     * Clears all Containers from the game GUI containers stack.
     */
    public void clearGameContainers() {
        for (Container container : guiContainersHome) {
            guiContainersGame.peek().close();
            container.setPushed(false);
        }
        guiContainersGame.clear();
    }
    
    /**
     * Transitions to the specified TransitionScreen.
     *
     * @param ts the TransitionScreen to transition to
     */
    public void transition(TransitionScreen ts) {
        transition = ts;
    }

    /**
     * Ends the current transition screen and sets the state to the specified new state.
     * 
     * @param newState the new state to set after the transition
     */
    public void endTransition() {
        transition = null;
    }

    /**
     * Returns a boolean indicating whether the mouse is down.
     *
     * @return true if the mouse is down, false otherwise
     */
    public static boolean mouseDown() {
        return mouseDown;
    }

    /**
     * Returns the x-coordinate of the mouse cursor relative to the window.
     *
     * @return the x-coordinate of the mouse cursor
     */
    public int mouseX() {
        return (java.awt.MouseInfo.getPointerInfo().getLocation().x - frame.getLocation().x) /
                (SCREEN_WIDTH / Renderer.WIDTH);
    }

    /**
     * Returns the y-coordinate of the mouse cursor relative to the window.
     *
     * @return the y-coordinate of the mouse cursor
     */
    public int mouseY() {
        return (java.awt.MouseInfo.getPointerInfo().getLocation().y - frame.getLocation().y) /
                (SCREEN_HEIGHT / Renderer.HEIGHT) - (fullscreen ? 0 : 4);
    }

    /**
     * Sets the window to fullscreen or windowed mode.
     *
     * @param f true to set the window to fullscreen, false to set the window to
     *          windowed mode
     */
    public void setFullscreen(boolean f) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fullscreen = f;
                if (fullscreen) {
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                            .setFullScreenWindow(frame);
                    Renderer.setFullscreen(true);
                } else if (!fullscreen) {
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                            .setFullScreenWindow(null);
                    frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                }
            }
        });
    }

    // -------------------------------------------------------------------
    // Key Events
    // -------------------------------------------------------------------

    /**
     * Handles key typing events.
     *
     * @param e the KeyEvent object for the key typing event
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Handles key press events.
     *
     * @param e the KeyEvent object for the key press event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F11) {
            setFullscreen(!fullscreen);
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (state == WindowState.GAME) {
                if (!this.guiContainersGame.isEmpty())
                    this.popGameContainer();
            } else {
                this.setState(WindowState.BACK);
            }
        }

        if(state == WindowState.NEWGAME)
            createMenu.keyPressed(e);

        if (world != null)
            Controls.setPress(e, world.instanceEvent());
    }

    /**
     * Handles key release events.
     *
     * @param e the KeyEvent object for the key release event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (world != null)
            Controls.setRelease(e, world.instanceEvent());
    }

    // -------------------------------------------------------------------
    // Mouse Events
    // -------------------------------------------------------------------

    /**
     * Handles mouse click events.
     *
     * @param e the MouseEvent object for the mouse click event
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Handles mouse press events.
     *
     * @param e the MouseEvent object for the mouse press event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(transition != null)
            return;

        mouseDown = true;
        this.availableClick = true;

        int rx = mouseX();
        int ry = mouseY();

        e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiersEx(), rx, ry,
                e.getClickCount(), e.isPopupTrigger(), e.getButton());

        if (state == WindowState.GAME) {
            if (world != null) {
                GameMap loadedmap = world.getLoadedMap();
                loadedmap.gameClick(world.instanceEvent());
                this.availableClick = true;
            }

            if (!guiContainersGame.isEmpty()) {
                if (guiContainersGame.peek().contains(rx, ry)) {
                    guiContainersGame.peek().guiClick(e);
                    this.availableClick = false;
                }
            }
        } else {
            if (!guiContainersHome.isEmpty()) {
                if (guiContainersHome.peek().contains(rx, ry)) {
                    guiContainersHome.peek().guiClick(e);
                    this.availableClick = false;
                }
            }
        }
    }

    /**
     * Handles mouse release events.
     *
     * @param e the MouseEvent object for the mouse release event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    /**
     * Handles mouse enter events.
     *
     * @param e the MouseEvent object for the mouse enter event
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Handles mouse exit events.
     *
     * @param e the MouseEvent object for the mouse exit event
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
