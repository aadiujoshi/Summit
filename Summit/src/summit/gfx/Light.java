package summit.gfx;

public class Light implements Paintable{
    private int rLayer = RenderLayers.STRUCTURE_ENTITY_LAYER+1; 

    public static final Light NO_LIGHT = new Light(0, 0, 0, 0, 0, 0);

    private float radius;

    private float x;
    private float y;
    
    private int red;
    private int green;
    private int blue;

    public Light(float x, float y, float radius, int r, int g, int b){
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public Light(float x, float y, float radius, ColorFilter filter){
        this(x, y, radius, filter.getRed(), filter.getGreen(), filter.getBlue());
    }

    @Override
    public void renderLayer(OrderPaintEvent r) {
        r.getRenderLayers().addToLayer(rLayer, this);
    }

    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().renderLight(this, e.getCamera());
    }    

    //-------------  getters and setters -------------------
    
    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
    
    public void setRenderLayer(int layer){
        this.rLayer = layer;
    }

    public ColorFilter getColorFilter(){
        return new ColorFilter(red, green, blue);
    }
}
