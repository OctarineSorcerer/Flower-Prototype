package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import javax.xml.bind.ValidationException;
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

        ArrayList<Bezier<Vector2>> beziers = new ArrayList<Bezier<Vector2>>();
        int beginning = 0, end = 3;
        while(end <= points.length)
        {
            ArrayList<Vector2> bezPoints = new ArrayList<Vector2>();
            for(int i = beginning; i <= end; i++) {
                bezPoints.add(points[i]);
            }
            beziers.add(new Bezier<Vector2>(bezPoints.toArray(new Vector2[bezPoints.size()])));
            beginning += 2; end += 2;
        }

        Vector2 pixel = new Vector2(); //Thing we will use to draw

        beziers.get(0).valueAt(pixel, t); //.add(pixmap.getWidth()/2, 0); For centering
        pixmap.fillCircle((int)pixel.x, height - (int)pixel.y, radius);

        //As the intended use for this is creating sprites and saving them for later use, optimisation should be the
        //last worry here. Therefore, ALL THE RESOLUTION
        for(Bezier<Vector2> bezier : beziers) {
            for (int i = 1; i <= segments; i++) {
                t = i / (float) segments;
                bezier.valueAt(pixel, t); //.add(pixmap.getWidth()/2, 0);
                if (i == segments) { //Last one

                    tipX = Math.round(pixel.x);
                    tipY = Math.round(pixel.y);
                }
                pixmap.fillCircle((int) pixel.x, height - (int) pixel.y, radius);
                if (pixel.y + radius < smallestYFromTop) {
                    smallestYFromTop = Math.round(pixel.y + radius);
                }
                if (pixel.x + radius > greatestX) {
                    greatestX = Math.round(pixel.x + radius);
                }
            }
        }

        pixmap.setColor(Color.RED); //cross dem debug points
        for(Vector2 point : points) {
            pixmap.drawLine((int)point.x + 5, (int)point.y + 5, (int)point. x - 5, (int)point.y - 5);
            pixmap.drawLine((int)point.x - 5, (int)point.y + 5, (int)point.x + 5, (int)point.y - 5);
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
     * @param cubicCount amount of cubic curves to generate as a path
     * @param width width of the stem's sprite
     * @return
     */
    public static Vector2[] GeneratePoints(long seed, int cubicCount, int width) {
        List<Vector2> points = new ArrayList<Vector2>();
        Vector2 lastEndPoint, lastHandle;
        Random rand = new Random(seed);
        double yLast = 0;
        int yRangeMin = 60, yRangeMax = 100;
        for(int i = 0; i < cubicCount; i++)
        {
            for(int i2 = 0; i2 < 4; i2++) {
                if(i == 0 && i2 == 0) { //First point
                    points.add(new Vector2(width/2, 0)); //control point
                }
                if(i > 1 && i2 == 0) { //past first curve, on first point
                    continue; //skippy, it's already there
                }
                else if(i == 0 || i2 > 1) //First curve or past first handle
                {
                    double x = rand.nextDouble() * width;
                    double yAddition = rand.nextInt(yRangeMax - yRangeMin) + yRangeMin;
                    double y = yLast + yAddition;
                    points.add(new Vector2((float) x, (float) y));
                    //System.out.println(x + ", " + y + ". yAddition = " + yAddition);
                    yLast = y;
                }
                else if(i > 0 && i2 == 1) //maybe just "Else". This ensures that the new first handle of the line is tangential
                {
                    lastEndPoint = points.get(points.size() - 1);
                    lastHandle = points.get(points.size() - 2);
                    Vector2 newPoint = new Vector2();
                    newPoint.x = lastEndPoint.x + (lastEndPoint.x - lastHandle.x);
                    newPoint.y = lastEndPoint.y + (lastEndPoint.y - lastHandle.y);
                    //Temporary scale of 1, if needed it can probably be calculated
                    points.add(newPoint);
                }
            }
        }
        //Collections.reverse(points);
        return points.toArray(new Vector2[points.size()]);
    }
}
