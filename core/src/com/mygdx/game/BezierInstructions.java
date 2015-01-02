package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Dan on 21/12/2014.
 */
public class BezierInstructions {

    public int tipX, tipY;
    public Vector2[] points;

    /**
     *
     * @param points Control points, try for 4 of these right now
     */
    public BezierInstructions(Vector2[] points) {
        this.points = points;
    }

    /**
     * Draws a cubic bezier curve
     * @param pixmap Pixmap for use of drawing
     * @return A texture containing drawn bezier curve
     */
    public Texture Draw(Pixmap pixmap, int radius) {
        int height = pixmap.getHeight();
        pixmap.setColor(Color.GREEN);
        int segments = height; //might be enough

        int greatestX = 0, smallestYFromTop = 0, topX = 0;

        float t = 0;

        Bezier<Vector2> bezier = new Bezier<Vector2>(points);
        Vector2 pixel = new Vector2(); //Thing we will use to draw

        bezier.valueAt(pixel, t); //.add(pixmap.getWidth()/2, 0); For centering
        pixmap.fillCircle((int)pixel.x, height - (int)pixel.y, radius);

        //As the intended use for this is creating sprites and saving them for later use, optimisation should be the
        //last worry here. Therefore, ALL THE RESOLUTION
        for(int i = 1; i <= segments; i++) {
            t = i / (float) segments;
            bezier.valueAt(pixel, t); //.add(pixmap.getWidth()/2, 0);
            if(i == segments) { //Last one

                tipX = Math.round(pixel.x);
                tipY = Math.round(pixel.y);
            }
            pixmap.fillCircle((int)pixel.x, height - (int)pixel.y, radius);
            if(pixel.y + radius < smallestYFromTop) {
                smallestYFromTop = Math.round(pixel.y + radius);
            }
            if(pixel.x + radius > greatestX) {
                greatestX = Math.round(pixel.x + radius);
            }
        }
        Pixmap alteredMap = new Pixmap(greatestX, height - smallestYFromTop, Pixmap.Format.RGBA4444);
        alteredMap.drawPixmap(pixmap, 0, 0, 0, 0, greatestX, height - smallestYFromTop);
        pixmap.dispose();
        Texture output = new Texture(alteredMap);
        alteredMap.dispose();
        return output;
    }

    /***
     *
     * @param seed just some random seed thing
     * @param pointCount amount of points in the curve! seeing as we only have a 4-point drawing thing so far use 4
     * @param width width of the stem's sprite
     * @return
     */
    public static Vector2[] GeneratePoints(long seed, int pointCount, int width) {
        List<Vector2> points = new ArrayList<Vector2>();
        Random rand = new Random(seed);
        double yLast = 0;
        int yRangeMin = 60, yRangeMax = 100;
        points.add(new Vector2(width/2, 0));
        for(int i = 1; i < pointCount; i++)
        {
            double x = rand.nextDouble() * width;
            double yAddition = rand.nextInt(yRangeMax - yRangeMin) + yRangeMin;
            double y = yLast + yAddition;
            points.add(new Vector2((float) x, (float) y));
            //System.out.println(x + ", " + y + ". yAddition = " + yAddition);
            yLast = y;
        }
        //Collections.reverse(points);
        return points.toArray(new Vector2[points.size()]);
    }
}
