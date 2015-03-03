package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a series of bezier curves, contains methods to use and manipulate them
 */
public class BezierInstructions {

    public int tipX, tipY;
    public Vector2 beginning;
    private Vector2[] points; //Format should be E, p, p, E, p, p, E, p, p, E etc
    private ArrayList<Bezier<Vector2>> curvesOnScreen; //for now, holds all the curves
    private long seed;

    private Vector2 point0 = new Vector2(), point1 = new Vector2();
    private int segments;

    public BezierInstructions(long seed) {
        LoadPoints(GeneratePoints(seed, 4, 200));
        beginning = new Vector2();
    }
    public BezierInstructions(long seed, Vector2 root) {
        LoadPoints(GeneratePoints(seed, 4, 200));
        beginning = root;
    }

    /**
     *
     * @param points Control points, try for 4 of these right now
     */
    void LoadPoints(Vector2[] points) {
        this.points = points;
        tipX = (int)(points[points.length - 1].x);
        tipY = (int)(points[points.length - 1].y);
    }

    /**
     * DrawAll without using a texture. Perhaps more CPU-intensive, but by gods will save on memory and other problems
     * @param shapeRenderer The shape renderer to render the lines with
     * @param rootOrigin NOT where the root is. Where the bottom-left of the 'sprite' should be ie RootLoc - width/2
     */
    public void DrawAll(ShapeRenderer shapeRenderer, Vector2 rootOrigin) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN); //Yes. Green stem for now

        float t;
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

    /**
     * Draws only he curves up to the maximum T specified
     * @param shapeRenderer Shape renderer to render the curves
     * @param rootOrigin Origin of the root of the stem
     * @param maximumT The maximum t to render to. 1 per curve, where the decimal is the fraction of the curve to render, ie 0.5 for ~ a half
     */
    public void DrawSome(ShapeRenderer shapeRenderer, Vector2 rootOrigin, float maximumT) {
        //Only currently works as curvesOnScreen holds all
        float t;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN); //Yes. Green stem for now
        int floored = (int)Math.floor(maximumT);

        for(int c = 0; c < maximumT && c < curvesOnScreen.size(); c++) {
            Bezier<Vector2> curve = curvesOnScreen.get(c);
            curve.valueAt(point0, 0f);
            for(int i = 1; i <= segments; i++) {
                t = i/(float)segments;
                if(c == floored && t > maximumT - floored) {
                    break; //If we've gone beyond the scope of what's already here
                }
                curve.valueAt(point1, t);
                Vector2 firstDrawPoint = point0.cpy().add(rootOrigin);
                Vector2 secondDrawPoint = point1.cpy().add(rootOrigin);
                shapeRenderer.rectLine(firstDrawPoint, secondDrawPoint, 15); //default WIDTH OF 15
                point0 = point1.cpy();
            }
        }
        shapeRenderer.end();
    }

    /**
     * For now just sorts out all the curves. commented-out condition didn't work 100% of time
     * @param cameraYPos Y position of camera object being used
     * @param cameraXPos X position of camera object being used
     * @param root Root (ie origin) of the stem
     */
    public void GetCurvesOnScreen(int cameraYPos, int cameraXPos, Vector2 root) {
        int yPos = cameraYPos - FlowerPrototype.HEIGHT/2;
        int xPos = cameraXPos - FlowerPrototype.WIDTH/2;
        curvesOnScreen = new ArrayList<Bezier<Vector2>>();
        int curve = 0;
        for(int i = 0; i < points.length; i++) {
            if(i != points.length - 1) curve = i/3; //save it from going above hopefully?
            float pointY = points[i].y + root.y;
            float pointX = points[i].x + root.x;
            //if((pointX > yPos && pointY < yPos + FlowerPrototype.HEIGHT) && //if point is within limits of the screen I guess
                    //pointX > xPos && pointX < xPos + FlowerPrototype.WIDTH ) {
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
            //}
        }
        segments = curvesOnScreen.size() * 40;
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
     * @param cubicCount amount of cubic curves to generate as a path
     * @param width width of the stem's sprite
     * @return An array of vectors representing the points, in format (End, control, control, End, control, etc..)
     */
    public Vector2[] GeneratePoints(long seed, int cubicCount, int width) {
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
                    //skip it, it's already there
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
                }
            }
        }
        this.seed = seed;
        return points.toArray(new Vector2[points.size()]);
    }
}
