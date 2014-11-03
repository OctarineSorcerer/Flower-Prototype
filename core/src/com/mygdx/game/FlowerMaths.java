package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.sun.javafx.geom.Point2D;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.awt.*;

/**
 * Created by Dan on 09/10/2014.
 */
public class FlowerMaths {

    /**
     * Returns the closest axial angle (0, 90, 180, 270, 360) to the given angle
     * @param angle A positive angle. Does not have to be below 360
     * @return Either 0, 90, 180, 270, 360)
     */
    public static int GetClosestQuarterAngle(float angle) {
        if(angle > 360) //Reduces angle to its equivalent, below-360 form
        {
            angle = angle - (int)(angle/360);
        }
        if(angle >= 0 && angle < 45)
        {
            return 0;
        }
        else if (angle >= 45 && angle < 135)
        {
            return 90;
        }
        else if(angle >= 135 && angle < 225)
        {
            return 180;
        }
        else if(angle >= 225 && angle < 315)
        {
            return 270;
        }
        else return 0;
    }

    /**
     * Returns the width a petal should have, given a radius and the separation between petals
     * @param petalSepAngle Degrees a petal is separated from another by
     * @param sectorPortion The amount per sector a petal should take up
     * @param radius Radius of the flowerhead/circle
     * @return The width a petal should be in this situation
     */
    public static float GetPetalWidth(float petalSepAngle, float sectorPortion, float radius) {
        float theta = petalSepAngle * MathUtils.degreesToRadians;
        float arcLength = radius * theta;
        float chordLength = 2f*radius*(MathUtils.sin(arcLength/2f*radius));
        return Math.abs(chordLength*sectorPortion);
    }

    /**
     * Take my own word for this, the maths is in one or two of your books. Position of petal relative to circle center
     * @param radius Radius of the flowerhead/circle
     * @param angle Angle around the circle that the petal will reside at
     * @return
     */
    public static Point2D GetPetalPos(float radius, float angle)
    {
        if(angle == 360) angle = 0;
        float closestAxial = GetClosestQuarterAngle(angle);
        float sepFromAxial = Math.abs(angle - closestAxial);
        if(angle == 0)
                return new Point2D(0, (int)radius);
        else if (angle == 90)
                return new Point2D((int)radius, 0);
        else if (angle == 180)
                return new Point2D(0, (int)(-radius));
        else if(angle == 270)
                return new Point2D((int)(-radius), 0);
        else { //firstly operate under assumption of near horizontal - we can then switch if not
            float vertical = Math.abs(radius * (MathUtils.sinDeg(sepFromAxial)));
            float horizontal = Math.abs(radius * (MathUtils.sinDeg(90 - sepFromAxial)));

            if(closestAxial == 0 || closestAxial == 180) { //closest to a vertical axis. Switch vertical and horizontal
                float vBuffer = vertical;
                vertical = horizontal;
                horizontal = vBuffer; //preserve signs
            }
            if (angle > 180 && angle < 360) //negative horizontal
            {
                horizontal = Math.abs(horizontal) * -1;
            }
            if(angle > 90 && angle < 270) { //downwards vertical (negative)
                vertical = Math.abs(vertical) * -1;
            }
            return new Point2D((int)horizontal, (int)vertical);
        }
    }

    /**
     * Adds two points together (ie where you'd end up)
     * @param initial Initial point
     * @param relative Travel instructions? point
     * @return
     */
    public static Point2D AddPoints(Point2D initial, Point2D relative)
    {
        Point2D output = new Point2D(initial.x + relative.x, initial.y + relative.y);
        return output;
    }
}
