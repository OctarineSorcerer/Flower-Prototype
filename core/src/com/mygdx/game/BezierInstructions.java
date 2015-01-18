package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.sun.javafx.geom.Point2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dan on 21/12/2014.
 */
public class BezierInstructions {

    public int tipX, tipY;
    public Vector2[] points; //Format should be E, p, p, E, p, p, E, p, p, E etc
    ArrayList<Bezier<Vector2>> curvesOnScreen;

    Vector2 point0 = new Vector2(), point1 = new Vector2();
    int segments;
    float t = 0;

    public BezierInstructions() {
        LoadPoints(GenerateCrazyPoints(new Random().nextLong(), 4, 200));
    }

    /**
     *
     * @param points Control points, try for 4 of these right now
     */
    public void LoadPoints(Vector2[] points) {
        this.points = points;
        tipX = (int)(points[points.length - 1].x);
        tipY = (int)(points[points.length - 1].y);
    }

    /**
     * Draws a cubic bezier curve
     * @param pixmap Pixmap for use of drawing
     * @return A texture containing drawn bezier curve
     */
    public Texture DrawToTexture(Pixmap pixmap, int radius) {
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
            beginning += 3; end += 3;
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
        Vector2 point = new Vector2();
        for(int i = 0; i < points.length; i++) {
            point.set(points[i].x, points[i].y);
            pixmap.drawLine((int)point.x + 5, height - (int)point.y + 5, (int)point. x - 5, height - (int)point.y - 5);
            pixmap.drawLine((int)point.x - 5, height - (int)point.y + 5, (int)point.x + 5, height - (int)point.y - 5);
            if(i % 3 == 0 && i != 0) { //endpoint
                pixmap.drawLine((int)points[i-1].x, height - (int)points[i-1].y, (int)points[i].x, height - (int)points[i].y);
                if(i < points.length - 1) { //not the last
                    pixmap.drawLine((int)points[i+1].x, height - (int)points[i+1].y, (int)points[i].x, height - (int)points[i].y);
                }
            }
        }

        Pixmap alteredMap = new Pixmap(greatestX, height - smallestYFromTop, Pixmap.Format.RGBA4444);
        alteredMap.drawPixmap(pixmap, 0, 0, 0, 0, greatestX, height - smallestYFromTop);
        pixmap.dispose();
        Texture output = new Texture(alteredMap);
        alteredMap.dispose();
        return output;
    } //Frickin' maximums :(

    /**
     * Draw without using a texture. Perhaps more CPU-intensive, but by gods will save on memory and other problems
     * @param shapeRenderer
     * @param rootOrigin NOT where the root is. Where the bottom-left of the 'sprite' should be ie RootLoc - width/2
     */
    public void Draw(ShapeRenderer shapeRenderer, Vector2 rootOrigin) {
        //No maximums needed here... just... need to find the curves being used here
        //First, get all curves that may be on screen
        //For each, do the segment-draw thing
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        for(Bezier<Vector2> curve : curvesOnScreen) {
            curve.valueAt(point0, 0f);
            for(int i = 1; i <= segments; i++) {
                t = i/(float)segments;
                curve.valueAt(point1, t);
                Vector2 firstDrawPoint = point0.cpy().add(rootOrigin);
                Vector2 secondDrawPoint = point1.cpy().add(rootOrigin);
                shapeRenderer.rectLine(firstDrawPoint, secondDrawPoint, 15); //default WIDTH OF 15
                point0 = point1.cpy();
            }
        }
        shapeRenderer.end();
    }

    public void GetCurvesOnScreen(int cameraYPos, int cameraXPos, Vector2 root) {
        int yPos = cameraYPos - FlowerPrototype.HEIGHT/2;
        int xPos = cameraXPos - FlowerPrototype.WIDTH/2;
        curvesOnScreen = new ArrayList<Bezier<Vector2>>();
        int curve = 0;
        for(int i = 0; i < points.length; i++) {
            if(i != points.length - 1) curve = i/3; //save it from going above hopefully?
            float pointY = points[i].y + root.y;
            float pointX = points[i].x + root.x;
            if((pointX > yPos && pointY < yPos + FlowerPrototype.HEIGHT) && //if point is within limits of the screen I guess
                    pointX > xPos && pointX < xPos + FlowerPrototype.WIDTH ) {
                ArrayList<Vector2> tempCurve = new ArrayList<Vector2>();
                for(int i2 = curve * 3; i2 <= curve*3 + 3; i2++) { //go through that curve's points
                    if(i2 < points.length) {
                        tempCurve.add(points[i2]);
                    }
                    else {
                        System.out.println("A thing, a thing is breaking!");
                    }
                }
                curvesOnScreen.add(new Bezier<Vector2>(tempCurve.toArray(new Vector2[tempCurve.size()])));
                i += 3 - (i % 3); //jump to next curve I think
            }
        }
        segments = curvesOnScreen.size() * 40;
    }

    /***
     *
     * @param seed just some random seed thing
     * @param cubicCount amount of cubic curves to generate as a path
     * @param width width of the stem's sprite
     * @return
     */
    public static Vector2[] GenerateCrazyPoints(long seed, int cubicCount, int width) {
        List<Vector2> points = new ArrayList<Vector2>();
        Vector2 lastEndPoint, lastHandle;
        Random rand = new Random(seed);
        double yLast = 0;
        int yRangeMin = 60, yRangeMax = 100;
        for(int i = 0; i < cubicCount; i++)
        {
            for(int i2 = 0; i2 <= 3; i2++) {
                if(i == 0 && i2 == 0) { //First point
                    points.add(new Vector2(0, 0)); //control point
                    continue;
                }
                if(i > 0 && i2 == 0) { //past first curve, on first point
                    continue; //skippy, it's already there
                }
                else if(i2 == 1 && i != 0) //first handle of new curve
                {
                    lastEndPoint = points.get(points.size() - 1);
                    lastHandle = points.get(points.size() - 2);
                    Vector2 newPoint = new Vector2();
                    newPoint.x = lastEndPoint.x + (lastEndPoint.x - lastHandle.x);
                    newPoint.y = lastEndPoint.y + (lastEndPoint.y - lastHandle.y);
                    //Temporary scale of 1, if needed it can probably be calculated
                    points.add(newPoint);
                    continue;
                }
                else if(i2 > 1 || i == 0) //First curve or past first handle (no tangents to worry about)
                {   //Basically just a random point in range
                    boolean isNegative = rand.nextBoolean();
                    double x = rand.nextDouble() * width;

                    if(isNegative) { x = -x; }

                    double yAddition = rand.nextInt(yRangeMax - yRangeMin) + yRangeMin;
                    double y = yLast + yAddition;
                    points.add(new Vector2((float) x, (float) y));
                    yLast = y;
                    continue;
                }
            }
        }

        return points.toArray(new Vector2[points.size()]);
    }
}
