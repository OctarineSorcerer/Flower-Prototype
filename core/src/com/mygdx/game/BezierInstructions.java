package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Dan on 21/12/2014.
 */
public class BezierInstructions {

    /***
     * Woo, binomial expansion! This using a cubic bezier curve, the equation for a point at a time is as follows:
     * (1-t)^3 * P0 + 3(1-t)^2 * t * P1 + 3(1-t) * t^2 * P2 + t^3 * P3
     * @param t Elapsed time. Between 0 and 1, 0 being start of curve, 1 being end
     * @param p0 I believe... first control point?
     * @param p1 Second control point
     * @param p2 Third control point
     * @param p3 End control point I think
     * @return A vector containing the point on a bezier curve at the specified time
     */
    static Vector2 CalculateBezierPoint(float t, Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3) {
        float u = 1 - t;

        Vector2 p = p0.cpy().scl(((float) Math.pow(u, 3)));
        p.add(p1.cpy().scl(3 * u * u * t));
        p.add(p2.cpy().scl(3 * u * t * t));
        p.add(p3.cpy().scl(t * t * t));

        return p;
    }

    /***
     * Draws a cubic bezier curve
     * @param points The control points. CONTROL POINTS. 4 of these please.
     * @param pixmap Pixmap for use of drawing
     * @return A texture containing drawn bezier curve
     */
    public static Texture DrawBezier(Vector2[] points, Pixmap pixmap)
    {
        int radius = 20; //hypothetical for now, we'll pass in an actual radius later
        int height = pixmap.getHeight();
        pixmap.setColor(Color.GREEN);
        int segments = height; //might be enough
        float t = 0;

        Bezier<Vector2> bezier = new Bezier<Vector2>(points);
        Vector2 pixel = new Vector2(); //Thing we will use to draw

        bezier.valueAt(pixel, t).add(pixmap.getWidth()/2, 0);
        pixmap.fillCircle((int)pixel.x, (int)pixel.y, radius);

        //As the intended use for this is creating sprites and saving them for later use, optimisation should be the
        //last worry here. Therefore, ALL THE RESOLUTION
        for(int i = 1; i <= segments; i++) {
            t = i / (float) segments;
            bezier.valueAt(pixel, t).add(pixmap.getWidth()/2, 0);
            pixmap.fillCircle(((int) pixel.x), ((int) pixel.y), radius);
            System.out.println(pixel.x + ", " + pixel.y);
        }
        Texture output = new Texture(pixmap);
        pixmap.dispose();
        return output;
    }


}
