package summit.util;

public class Region {

    private float width;
    private float height;

    private float x;
    private float y;

    public Region(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean overlap(Region other){
        return false;
    }

    public boolean contains(float ox, float oy){
        if(ox < x+(width/2) && ox > x-(width/2)){
            if(oy < y+(height/2) && oy > y-(height/2)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "x: " + x + ", y: " + y + ", width: " + width + ", height: " + height;
    }

    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
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
