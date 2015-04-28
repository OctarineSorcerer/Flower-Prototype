package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a series of CUBIC bezier curves, contains methods to use and manipulate them
 * This site used for learning concepts: http://devmag.org.za/2011/04/05/bzier-curves-a-tutorial/
 */
public class BezierManager {

    public int tipX, tipY;
    private Vector2[] points; //Format should be E, p, p, E, p, p, E, p, p, E etc
    private ArrayList<Bezier<Vector2>> curvesOnScreen; //Holds all the curves
    private long seed; //Seed of this bezier curve

    private Vector2 point0 = new Vector2(), point1 = new Vector2(); //Used for tiny segments of each curve to draw
    private int segments; //Amount of segments per curve

    public BezierManager(long seed, int curves) {
        LoadPoints(GeneratePoints(seed, curves));
    }
    public long GetSeed() {
        return seed;
    }
    public int GetCurveCount() { return curvesOnScreen.size(); }

    /**
     * Loads a set of points, setting tip position and getting all curves
     * @param points Control points, try for 4 of these right now
     */
    private void LoadPoints(Vector2[] points) {
        this.points = points;
        tipX = (int)(points[points.length - 1].x);
        tipY = (int)(points[points.length - 1].y);
        GetAllCurves();
    }

    /**
     * Draws only he curveNumber up to the maximum T specified. Does so by drawing tiny little rectangles for each segment.
     * @param shapeRenderer Shape renderer to render the curveNumber
     * @param rootOrigin Origin of the root of the stem
     * @param maximumT The maximum t to render to. 1 per curve, where the decimal is the fraction of the curve to render, ie 0.5 for ~ a half
     */
    public void DrawSome(ShapeRenderer shapeRenderer, Vector2 rootOrigin, Color colour , float maximumT, float width) {
        float t; //Current progress through curve
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //Shape renderer will draw filled shape
        shapeRenderer.setColor(colour); //Green stem for now
        int floored = (int)Math.floor(maximumT);

        for(int c = 0; c < maximumT && c < curvesOnScreen.size(); c++) { //For each curve
            Bezier<Vector2> curve = curvesOnScreen.get(c);
            curve.valueAt(point0, 0f); //Get start of first segment
            for(int i = 1; i <= segments; i++) {
                t = i/(float)segments; //point along the curve
                if(c == floored && t > maximumT - floored) {
                    break; //If we've gone beyond the scope of what's already here
                }
                curve.valueAt(point1, t); //Sets point1 to next point
                Vector2 firstDrawPoint = point0.cpy().add(rootOrigin);
                Vector2 secondDrawPoint = point1.cpy().add(rootOrigin); //Draw points now take into account root origin
                shapeRenderer.rectLine(firstDrawPoint, secondDrawPoint, width); //draw a teeny tiny rectangle
                point0 = point1.cpy(); //End of this segment is the beginning of next
            }
        }
        shapeRenderer.end(); //End current batch of shaperenderer
    }

    public void GetAllCurves() {
        curvesOnScreen = new ArrayList<Bezier<Vector2>>();
        int curve = 0;
        for(int i = 0; i < points.length; i++) {
            if(i != points.length - 1) curve = i/3; //Set which curve we are on
            ArrayList<Vector2> tempCurve = new ArrayList<Vector2>();
            for(int i2 = curve * 3; i2 <= curve*3 + 3; i2++) { //go through that curve's points
                if(i2 < points.length) {
                    tempCurve.add(points[i2]); //Add point
                }
                else {
                    System.out.println("You're probably not using cubic curves here, you silly");
                }
            }
            curvesOnScreen.add(new Bezier<Vector2>(tempCurve.toArray(new Vector2[tempCurve.size()]))); //Add the bezier curve
            i += 3 - (i % 3); //jump to next curve
        }
        segments = curvesOnScreen.size() * 40; //40 segments per curve. Makes the corners seem a little furry, but works
    }

    /***
     *
     * @param t Cumulative t format here (ie 1 per curve here, beyond decimal is progress)
     * @param root Root of the curve
     * @return A vector2 representing the position on the given curve at that time
     */
    public Vector2 GetPositionAtT(float t, Vector2 root) {
        Vector2 output = new Vector2();
        int curveNum = (int)Math.floor(t);
        float progress = t - curveNum;
        if(curveNum >= curvesOnScreen.size()) {
            curveNum = curvesOnScreen.size() - 1;
            progress = 1.0f;
        }
        Bezier<Vector2> curve = curvesOnScreen.get(curveNum);
        curve.valueAt(output, progress);
        output.add(root.x, root.y);
        return output;
    }

    /***
     * Generates a bunch of points to create a pretty ridiculous bezier path
     * @param seed just some random seed thing
     * @param cubicCount amount of cubic curveNumber to generate as a path
     * @return An array of vectors representing the points, in format (End, control, control, End, control, etc..)
     */
    private Vector2[] GeneratePoints(long seed, int cubicCount) {
        List<Vector2> points = new ArrayList<Vector2>();
        Vector2 lastEndPoint, lastHandle;
        Random rand = new Random(seed);
        double yLast = 0;
        int yRangeMin = 60, yRangeMax = 100, width = 200;
        for(int i = 0; i < cubicCount; i++)
        {
            for(int i2 = 0; i2 <= 3; i2++) {
                if(i == 0 && i2 == 0) { //First point
                    points.add(new Vector2(0, 0)); //control point
                    continue;
                }
                if(i > 0 && i2 == 0) { //past first curve, on first point
                    //skip it, it's already there (end of previous curve)
                }
                else if(i2 == 1 && i != 0) //first handle of new curve
                {   //Add a point that is a continuation of the line between the last end and handle (Tangent)
                    //This is so that the curve is smooth and doesn't sharply change direction
                    lastEndPoint = points.get(points.size() - 1);
                    lastHandle = points.get(points.size() - 2);
                    Vector2 newPoint = new Vector2();
                    newPoint.x = lastEndPoint.x + (lastEndPoint.x - lastHandle.x);
                    newPoint.y = lastEndPoint.y + (lastEndPoint.y - lastHandle.y);
                    points.add(newPoint);
                }
                else if(i2 > 1 || i == 0) //First curve or past first handle (no tangents to worry about)
                {   //Basically just a random point in range
                    boolean isNegative = rand.nextBoolean(); //Goes left?
                    double x = rand.nextDouble() * width; //shift in X

                    if(isNegative) { x = -x; } //Make negative if this was decided

                    double yAddition = rand.nextInt(yRangeMax - yRangeMin) + yRangeMin; //How far up this point will go
                    double y = yLast + yAddition;
                    points.add(new Vector2((float) x, (float) y));
                    yLast = y;
                }
            }
        }
        this.seed = seed;
        return points.toArray(new Vector2[points.size()]);
    }

    /**
     * Adds a curve to the overall curve. Kinda cheats by calculating it agains
     */
    public void AddCurve() {
        GetAllCurves();
        int currentCurveCount = curvesOnScreen.size();
        LoadPoints(GeneratePoints(seed, currentCurveCount + 1));
    }
}
