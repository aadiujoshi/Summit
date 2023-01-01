package summit.gui;

import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Settings;

import java.awt.Color;
import java.awt.event.MouseEvent;

/**
 * A graphical user interface (GUI) container for video settings options.
 * Allows the user to adjust ambient occlusion, vsync, and number of threads
 * used for rendering.
 *
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 * 
 */
public class VideoSettings extends Container {

    // relative positions for all the boxes
    private float[] bx = new float[] { 0.65f, 0.75f, 0.85f };
    private float[] by = new float[] { 0.3f, 0.4f, 0.5f };

    private final int initThreads = (int) Settings.getSetting("threads");
    private boolean threadsChange;

    /**
     * Constructs a new video settings menu.
     *
     * @param window the parent window
     */
    public VideoSettings(Window window) {
        super(null, window, 0.5f, 0.5f, Sprite.FILL_SCREEN);

        TextContainer ao_count = new TextContainer(
                Settings.getSetting("ambient_occlusion").toString(),
                this, window, bx[0], by[0], Sprite.MENUBOX5);

        TextContainer vsync_count = new TextContainer(
                Settings.getSetting("vsync").toString(),
                this, window, bx[0], by[1], Sprite.MENUBOX5);

        TextContainer threads_count = new TextContainer(
                Settings.getSetting("threads").toString(),
                this, window, bx[0], by[2], Sprite.MENUBOX5);

        // -----------------------------------------------------------------------
        // -----------------add---------------------------------------------------
        // -----------------------------------------------------------------------

        TextContainer ao_inc = new TextContainer("+", this, window, bx[1], by[0], Sprite.MENUBOX5) {
            public void guiClick(MouseEvent e) {
                updateCont(ao_count, "ambient_occlusion", 1);
            }
        };

        TextContainer vsync_inc = new TextContainer("+", this, window, bx[1], by[1], Sprite.MENUBOX5) {
            public void guiClick(MouseEvent e) {
                updateCont(vsync_count, "vsync", 1);
            }
        };

        TextContainer threads_inc = new TextContainer("+", this, window, bx[1], by[2], Sprite.MENUBOX5) {
            public void guiClick(MouseEvent e) {
                updateCont(threads_count, "threads", 1);
                if (initThreads != (int) Settings.getSetting("threads")) {
                    threadsChange = true;
                } else {
                    threadsChange = false;
                }
            }
        };

        // -----------------------------------------------------------------------
        // -----------------subtract----------------------------------------------
        // -----------------------------------------------------------------------

        TextContainer ao_dec = new TextContainer("-", this, window, bx[2], by[0], Sprite.MENUBOX5) {
            public void guiClick(MouseEvent e) {
                updateCont(ao_count, "ambient_occlusion", -1);
            }
        };

        TextContainer vsync_dec = new TextContainer("-", this, window, bx[2], by[1], Sprite.MENUBOX5) {
            public void guiClick(MouseEvent e) {
                updateCont(vsync_count, "vsync", -1);
            }
        };

        TextContainer threads_dec = new TextContainer("-", this, window, bx[2], by[2], Sprite.MENUBOX5) {
            public void guiClick(MouseEvent e) {
                updateCont(threads_count, "threads", -1);
                if (initThreads != (int) Settings.getSetting("threads")) {
                    threadsChange = true;
                } else {
                    threadsChange = false;
                }
            }
        };

        // -----------------------------------------------------------------------
        // -----------------------------------------------------------------------
        // -----------------------------------------------------------------------

        ColorFilter incf = new ColorFilter(Color.GREEN.getRGB());
        ColorFilter decf = new ColorFilter(Color.RED.getRGB());

        ColorFilter cf = new ColorFilter(0xffffff);

        ao_count.setTextFilter(cf);
        vsync_count.setTextFilter(cf);
        threads_count.setTextFilter(cf);

        ao_inc.setTextFilter(incf);
        vsync_inc.setTextFilter(incf);
        threads_inc.setTextFilter(incf);

        ao_dec.setTextFilter(decf);
        vsync_dec.setTextFilter(decf);
        threads_dec.setTextFilter(decf);

        addComponent(ao_count);
        addComponent(vsync_count);
        addComponent(threads_count);

        addComponent(ao_inc);
        addComponent(vsync_inc);
        addComponent(threads_inc);

        addComponent(ao_dec);
        addComponent(vsync_dec);
        addComponent(threads_dec);
    }

    /**
     * Updates the value for a particular setting and displays it in the GUI.
     *
     * @param cont    the TextContainer object displaying the setting value
     * @param setting the name of the setting to update
     * @param change  the amount to change the setting by
     */
    private void updateCont(TextContainer c, String property, int add) {
        int n = Integer.parseInt(c.getText()) + add;
        if (n >= 0 && n <= 9) {
            if (property.equals("threads") && n == 0) {
                c.setText((1) + "");
                Settings.changeSetting(property, 1);
                return;
            }
            c.setText((n) + "");
            Settings.changeSetting(property, n);
        }
    }


    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().fillRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT, 0x254aff);

        paintComponents(e);

        ColorFilter f = new ColorFilter(0x969696);

        int px = (int) (Renderer.WIDTH * 0.3);

        e.getRenderer().renderText("AMBIENT OCCLUSION",
                px,
                (int) (Renderer.HEIGHT * by[0]),
                Renderer.NO_OP, f);

        e.getRenderer().renderText("VSYNC",
                px,
                (int) (Renderer.HEIGHT * by[1]),
                Renderer.NO_OP, f);

        e.getRenderer().renderText("THREADS",
                px,
                (int) (Renderer.HEIGHT * by[2]),
                Renderer.NO_OP, f);

        // threads count change warning
        if (threadsChange) {
            e.getRenderer().renderText("!Restart game", px,
                    (int) (Renderer.HEIGHT * by[2]) + 10,
                    Renderer.NO_OP,
                    new ColorFilter(0xff0009));
        }
    }
}
