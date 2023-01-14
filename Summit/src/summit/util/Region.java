/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.io.Serializable;

public class Region implements Serializable{

    private float width;
    private float height;

    private float x;
    private float y;
    
    public Region(Region region){
        this(region.getX(), region.getY(), region.getWidth(), region.getHeight());
    }

    public Region(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    
    /** 
     * @param other
     * @return boolean
     */
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

    
    /** 
     * @param ox
     * @param oy
     * @return boolean
     */
    public boolean contains(float ox, float oy){
        if(ox < x+(width/2f) && ox > x-(width/2f)){
            if(oy < y+(height/2f) && oy > y-(height/2f)){
                return true;
            }
        }
        return false;
    }

    
    /** 
     * @param other
     * @return float
     */
    public float distance(Region other){
        return distance(this.x, this.y, other.getX(), other.getY());
    }

    
    /** 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return float
     */
    public static float distance(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    
    /** 
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return float
     */
    public static float theta(float x1, float x2, float y1, float y2){
        return (float)(Math.atan2(y1-y2, x1-x2));
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString(){
        return "[x:" + x + " y:" + y + " width:" + width + " height:" + height + "]";
    }

    
    /** 
     * @param x
     * @param y
     */
    //--------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------

    public void setPos(float x, float y){
        setX(x);
        setY(y);
    }

    
    /** 
     * @return float
     */
    public float getWidth() {
        return this.width;
    }

    
    /** 
     * @param width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    
    /** 
     * @return float
     */
    public float getHeight() {
        return this.height;
    }

    
    /** 
     * @param height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    
    /** 
     * @return float
     */
    public float getX() {
        return this.x;
    }

    
    /** 
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    
    /** 
     * @return float
     */
    public float getY() {
        return this.y;
    }

    
    /** 
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }
}
