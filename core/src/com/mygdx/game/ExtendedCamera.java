package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.sun.javafx.geom.Point2D;

/**
 * Camera class to facilitate easier camera use
 */
public class ExtendedCamera extends OrthographicCamera {
    private Integer minX, minY, maxX, maxY; //bounds which the camera can remain in

    /**
     * Construct the camera without bounds - ie no movement restrictions
     */
    public ExtendedCamera() {
        minX = null;
        maxX = null;
        minY = null;
        maxY = null;
    }

    /**
     * Construct camera with bounds - ie the co-ordinates it cannot travel outside of
     * @param minX Minimum X bound
     * @param minY Minimum Y bound
     * @param maxX Maximum X bound
     * @param maxY Maximum Y bound
     */
    public ExtendedCamera(Integer minX, Integer minY, Integer maxX, Integer maxY) {
        SetBounds(minX, minY, maxX, maxY);
    }

    /**
     * Sets the bounds of the camera
     * @param minX Minimum X boundary
     * @param minY Minimum Y boundary
     * @param maxX Maximum X boundary
     * @param maxY Maximum Y boundary
     */
    public void SetBounds(Integer minX, Integer minY, Integer maxX, Integer maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Translates the camera without going outside of bounds
     * @param x Amount to shift x of camera
     * @param y Amount to shift y of camera
     * @return A vector indicating the amount translated
     */
    public Vector2 SafeTranslate(float x, float y) {
        Vector2 output = new Vector2(position.x, position.y); //store current position
        translate(x, y); //Initial translate

        if(maxX != null && position.x > maxX) {
            position.x = maxX;
        }
        else if(minX != null && position.x < minX) {
            position. x = minX; //limit on x axis
        }

        if(maxY != null && position.y > maxY) {
            position.y = maxY;
        }
        else if(minY != null && position.y < minY) {
            position.y = minY; //limit on y axis
        }

        update();
        output.x = position.x - output.x;
        output.y = position.y - output.y;
        return  output;
    }
}
