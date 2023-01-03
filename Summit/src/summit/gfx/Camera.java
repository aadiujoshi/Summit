package summit.gfx;

import java.io.Serializable;

/**
 * The Camera class represents the camera in a 2D game, with x and y position
 * coordinates. It implements the
 * Serializable and Cloneable interfaces to allow for saving and copying of
 * camera objects.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Camera implements Serializable, Cloneable {

    /** The x position coordinate of the camera. */
    private float x;

    /** The y position coordinate of the camera. */
    private float y;

    /**
     * Constructs a Camera object with the specified x and y position coordinates.
     * 
     * @param x the x position coordinate of the camera
     * @param y the y position coordinate of the camera
     */
    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x position coordinate of the camera.
     * 
     * @return the x position coordinate of the camera
     */
    public float getX() {
        return this.x;
    }

    /**
     * Sets the x position coordinate of the camera.
     * 
     * @param x the new x position coordinate of the camera
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Returns the y position coordinate of the camera.
     * 
     * @return the y position coordinate of the camera
     */
    public float getY() {
        return this.y;
    }

    /**
     * Sets the y position coordinate of the camera.
     * 
     * @param y the new y position coordinate of the camera
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns a string representation of the Camera object, in the format "x y".
     * 
     * @return a string representation of the Camera object
     */
    @Override
    public String toString() {
        return this.x + "  " + this.y;
    }

    /**
     * Returns a deep copy of the Camera object.
     * 
     * @return a deep copy of the Camera object
     */
    @Override
    public Camera clone() {
        return new Camera(getX(), getY());
    }
}