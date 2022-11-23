package summit.game.structure;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import summit.game.GameClickEvent;
import summit.game.GameClickReciever;
import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.game.mapgenerator.GameMapGenerator;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Region;

public class Structure extends Region implements Paintable, GameClickReciever, GameUpdateReciever {

    private String sprite;

    private float spriteOffsetX;
    private float spriteOffsetY;

    private int renderOp;
    private ColorFilter filter = ColorFilter.NOFILTER;
    private ColorFilter shadow = new ColorFilter(-100, -100, -100);

    private GameMap innerMap;

    public Structure(float x, float y, float width, float height, GameMap map) {
        super(x, y, width, height);
        this.innerMap = map;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.getRenderLayers().addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().renderGame(sprite, 
                                    getX()+spriteOffsetX, getY()+spriteOffsetY, renderOp,
                                    filter,
                                    e.getCamera());
    }

    @Override
    public void gameClick(GameClickEvent e) {
        // System.out.println("you have clicked on a structure congratulations");

        // System.out.println(innerMap);

        Player p = e.getMap().getPlayer();

        if(getY()-getHeight()/2 > p.getY() && p.getX() < getX()+getWidth()/2 && p.getX() > getX()-getWidth()/2){
            innerMap.setPlayer(p);
            p.setX(0);
            p.setY(0);
            e.setLoadedMap(innerMap);
        }
    }

    @Override
    public void update(GameUpdateEvent e) {
        Point2D.Float mt = Renderer.toTile(e.mouseX(), e.mouseY(), e.getMap().getPlayer().getCamera());

        if(this.contains(mt.x, mt.y))
            setRenderOp(getRenderOp() | Renderer.OUTLINE_BLUE | Renderer.OUTLINE_GREEN | Renderer.OUTLINE_RED);
        else
            setRenderOp(getRenderOp() & ~Renderer.OUTLINE_BLUE & ~Renderer.OUTLINE_GREEN & ~Renderer.OUTLINE_RED);

        TileStack[][] tiles = e.getMap().getTiles();
        
        int startX = Math.round(getX()-getWidth()/2);
        int startY = Math.round(getX()-getHeight()/2);

        int endX = Math.round(getX()+getWidth()/2);
        int endY = Math.round(getX()+getHeight()/2);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile t = tiles[y][x].peekTile();
                t.setBoundary(true);

                Light l = new Light(x, y, 1.2f, shadow);

                l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
                // t.setRenderOp(t.getRenderOp() | Renderer.OUTLINE_BLUE);
                t.setLight(l);
            }
        }
    }

    public void setSprite(String sprite){
        this.sprite = sprite;
    }

     public void setInnerMap(GameMap map){
        this.innerMap = map;
    }

    public GameMap getInnerMap(){
        return this.innerMap;
    }

    public void setRenderOp(int r){
        renderOp = r;
    }

    public int getRenderOp(){
        return renderOp;
    }

    public void setColorFilter(ColorFilter cf){
        this.filter = cf;
    }

    public ColorFilter getColorFilter(){
        return this.filter;
    }

    public void setShadow(ColorFilter shadow){
        this.shadow = shadow;
    }

    public float getSpriteOffsetX() {
        return this.spriteOffsetX;
    }

    public void setSpriteOffsetX(float spriteOffsetX) {
        this.spriteOffsetX = spriteOffsetX;
    }

    public float getSpriteOffsetY() {
        return this.spriteOffsetY;
    }

    public void setSpriteOffsetY(float spriteOffsetY) {
        this.spriteOffsetY = spriteOffsetY;
    }
}
