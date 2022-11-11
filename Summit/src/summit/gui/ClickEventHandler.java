package summit.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import summit.game.GameMap;
import summit.gfx.Renderer;

public class ClickEventHandler extends java.awt.Component implements MouseListener {

    private GameMap map;

    public ClickEventHandler(GameMap map){
        this.map = map;
        super.addMouseListener(this);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // float nx = Renderer.toTile(e.getX(), map.getCamera());
        // float ny = Renderer.toTile(e.getY(), map.getCamera());

        // ((Clickable)map.getRegionAt(nx, ny)).click(map, e);
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
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
}
