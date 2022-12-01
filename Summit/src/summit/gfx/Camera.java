package summit.gfx;

import java.io.Serializable;

public class Camera implements Serializable, Cloneable{

    private float x;
    private float y;

    public Camera(float x, float y){
        this.x = x;
        this.y = y;
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

    
    @Override
    public String toString(){
        return this.x + "  " + this.y;
    }

    @Override 
    public Camera clone(){
        return new Camera(getX(), getY());
    }
}
