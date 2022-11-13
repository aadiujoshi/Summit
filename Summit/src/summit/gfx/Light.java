package summit.gfx;

public class Light implements Paintable{
    private float radius;
    private int intensity;

    private float x;
    private float y;
    
    public Light(float x, float y, float radius, int intensity){
        this.radius = radius;
        this.intensity = intensity;
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().renderLight(this, x, y, e.getCamera());
    }    

    //-------------  getters and setters -------------------
    
    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getIntensity() {
        return this.intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
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
}
