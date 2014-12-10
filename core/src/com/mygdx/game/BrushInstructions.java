package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Dan on 08/12/2014.
 */
public class BrushInstructions {
    ArrayList<BrushForce> forces;

    private class BrushForce {
        Vector2 magnitude;
        int start;
        int duration;
        int activeTime;
        boolean active;
    }
    private class Brush {
        Point2D position;
        Vector2 velocity;
        Color colour;
        int radius;

        /**
         * Constructor for a brush
         * @param brushPosition Initial position of the brush
         * @param brushColour Colour the brush should paint
         * @param brushRadius Radius the brush should paint ('tis a circle)
         */
        public Brush(Point2D brushPosition, Color brushColour, int brushRadius) {
            velocity = new Vector2();
            position = brushPosition;
            colour = brushColour;
            radius = brushRadius;
        }
    }

    public Texture Execute(int width, int targetHeight) {
        Pixmap canvas = new Pixmap(width, targetHeight, Pixmap.Format.RGBA4444);
        Brush brush = new Brush(new Point2D(width/2, 0), Color.GREEN, 20);
        canvas.setColor(brush.colour);

        int time = 0;
        int nextForceIndex = 0;

        while(brush.position.getY() < targetHeight) { //It DOES have to be step-by-step cos you have to paint a circle each time
            canvas.fillCircle((int)brush.position.getX(), (int)brush.position.getY(), brush.radius);
            BrushForce nearestForce = forces.get(nextForceIndex);
            if(nearestForce.start == time) { //If a magnitude starts now
                nearestForce.active = true;
                if(nextForceIndex < (forces.size() - 1)) nextForceIndex += 1; //yeah, don't increase if you're the last
            }
            for (BrushForce force : forces) { //DAMMIT I WANNA USE STREAMS BUT NO LAMBDA
                if(force.active) {
                    brush.velocity.add(force.magnitude);
                    force.activeTime += 1;
                    if(force.activeTime > force.duration) {
                        force.active = false;
                    }
                }
            }
            brush.position.add(brush.velocity.x, brush.velocity.y);
        }
        Texture output = new Texture(canvas);
        canvas.dispose();
        return output;
    }

    /**
     * Sets forces list, ensuring that they are ordered by the time they start
     * @param forcesInput Forces you wish to give the brush. Does not need to be sorted
     */
    public void SetForces(ArrayList<BrushForce> forcesInput) {
        Comparator<BrushForce> brushForceComparator = new Comparator<BrushForce>() {
            @Override
            public int compare(BrushForce o1, BrushForce o2) {
                return Integer.compare(o1.start, o2.start);
            }
        };
        Collections.sort(forcesInput, brushForceComparator);
        forces = forcesInput;
    }
}
