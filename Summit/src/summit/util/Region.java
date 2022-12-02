/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.io.Serializable;

public class Region implements Serializable{

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

        float bWidth = other.getWidth();
        float bHeight = other.getHeight();
        float aWidth = getWidth();
        float aHeight = getHeight();

        float bx = other.getX()-bWidth/2;
        float by = other.getY()-bHeight/2;
        float ax = getX()-aWidth/2;
        float ay = getY()-aHeight/2;

        return bx >= ax && (bx + bWidth) <= (ax + aWidth) &&
                by >= ay && (by + bHeight) <= (ay + aHeight);
    }

    public boolean contains(float ox, float oy){
        if(ox < x+(width/2f) && ox > x-(width/2f)){
            if(oy < y+(height/2f) && oy > y-(height/2f)){
                return true;
            }
        }
        return false;
    }

    public static float distance(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    @Override
    public String toString(){
        return "x: " + x + ", y: " + y + ", width: " + width + ", height: " + height;
    }

    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------

    public void setPos(float x, float y){
        setX(x);
        setY(y);
    }

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
