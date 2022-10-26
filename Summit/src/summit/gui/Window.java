package summit.gui;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Stack;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import summit.game.GameWorld;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gui.menu.Menu;
import summit.util.Time;

public class Window {
    
    private JFrame frame;
    private Canvas canvas;
    private Renderer renderer;

    private boolean closed = false;

    private int width;
    private int height;

    private Color bg;

    private Stack<Menu> menus;
    private WindowState state;

    private GameWorld world;

    private BufferStrategy buffer;

    private Thread graphicsThread;
    private Thread mouseThread;
    
    public Window(String title, int w, int h){

        width = w;
        height = h;
        
        menus = new Stack<>();

        renderer = new Renderer();

        world = new GameWorld(this);

        bg = Color.BLUE;

        graphicsThread = new Thread(new Runnable() {
            @Override
            public void run(){
                while(true){
                    Graphics2D g = null;
                    do {
                        try{
                            g = (Graphics2D)buffer.getDrawGraphics();

                            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                            // g.setRenderingHint(RenderingHints.);

                            {
                                renderFrame(g);
                            }

                        } finally {
                            g.dispose();
                        }
                        buffer.show();
                    } while (buffer.contentsLost());
                }
            }
        });

        mouseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                
            }
        });

        //init window frame, canvas, and buffer strategy
        {
            frame = new JFrame(title);
            frame.getContentPane().setPreferredSize(new Dimension(width, height));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);

            GraphicsConfiguration graphicsConfiguration = 
                    GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration();

            canvas = new Canvas(graphicsConfiguration);

            canvas.setIgnoreRepaint(true);
            canvas.setPreferredSize(new Dimension(width, height));
            canvas.setSize(width, height);
            frame.add(canvas);

            frame.pack();
            frame.setLocationRelativeTo(null);

            canvas.createBufferStrategy(2);
            buffer = canvas.getBufferStrategy();

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

            frame.setVisible(true);
        }

        graphicsThread.start();
        mouseThread.start();
    }
    
    private void renderFrame(Graphics2D g){
        g.setColor(bg);
        g.fillRect(0, 0, width, height);

        PaintEvent pe = new PaintEvent(renderer, Time.timeMs());

        world.paint(pe);

        for(Menu menu: menus){
            menu.paint(pe);
        }
    }

    public void setState(WindowState newState){
        if(state == newState) return;

        if(state == null && newState == WindowState.SELECTIONMENUS){
            
        }

        if(state == WindowState.SELECTIONMENUS && newState == WindowState.GAME){
            
        }

        if(state == WindowState.GAME && newState == WindowState.SELECTIONMENUS){
            
        }

        this.state = newState;
    }

    
    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------
}