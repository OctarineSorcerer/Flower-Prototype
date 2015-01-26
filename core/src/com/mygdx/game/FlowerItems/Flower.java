package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.*;
import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Not an interface - the flower should be procedural
 */
public class Flower { //These are their own classes as they may need unique functionality later
    public List<PetalFlyweight> petals;
    public Head head;
    public Stem stem;
    public Vector2 rootLoc;

    int count;
    PetalStyle style;

    public Flower(Petal mainPetal, Head flowerHead, Stem flowerStem, int petalCount, PetalStyle petalArrangement, Point2D root) {
        rootLoc = new Vector2(root.x, root.y);
        head = flowerHead;
        stem = flowerStem;

        petals = new ArrayList<PetalFlyweight>();
        petals.add(new PetalFlyweight(mainPetal));
        count = petalCount;
        style = petalArrangement;
        head.SetPosWithOrigin(
                new Point2D(stem.stemTip.x + rootLoc.x, stem.stemTip.y + rootLoc.y),
                new Point2D(head.sprite.getWidth()/2, head.sprite.getHeight()/2));
        ArrangePetals(style, count, 0);
    }

    public void DebugChangeStem() {
        stem = new Stem();
        Point2D headCenterOld = head.GetCenter();
        head.SetPosWithOrigin(
                new Point2D(stem.stemTip.x + rootLoc.x, stem.stemTip.y + rootLoc.y),
                new Point2D(head.sprite.getWidth() / 2, head.sprite.getHeight() / 2));
        for(PetalFlyweight fly : petals) {
            fly.ShiftLocs(head.GetCenter().x - headCenterOld.x,
                    head.GetCenter().y - headCenterOld.y);
        }
    }
    public void DrawSprites(SpriteBatch batch) {
        for (Flower.PetalFlyweight petalType : petals) {
            petalType.DrawCentered(batch);
        }
        head.sprite.draw(batch);
    }
    public void DrawShapes(ShapeRenderer shapeRenderer) {
        stem.curveInfo.Draw(shapeRenderer, new Vector2(rootLoc.x, rootLoc.y));
    }

    void ArrangePetals(PetalStyle arrangement, int count, int petalIndex) {
        float sepAngle = 360f / (float) count;
        switch (arrangement) {
            case Touching:
                PetalFlyweight thisFlyweight = petals.get(petalIndex);
                float petalWidth = FlowerMaths.GetPetalWidth(sepAngle, 0.5f, head.radius);
                Petal relevantPetal = thisFlyweight.petal;
                relevantPetal.sprite.setOrigin(relevantPetal.sprite.getWidth() / 2, 0); //origin at bottom thingy
                //relevantPetal.Scale(petalWidth / relevantPetal.sprite.getWidth()); //scales petal
                float sagitta = (float)(head.radius - Math.sqrt(Math.pow(head.radius, 2)
                        - Math.pow(0.5*relevantPetal.sprite.getWidth()*relevantPetal.sprite.getScaleX(), 2)));
                //^That bit is the height of the arc. Yeah. Go maths. http://www.mathopenref.com/sagitta.html
                thisFlyweight.ClearLocs();
                for (float i = 0; i < sepAngle * count; i += sepAngle) {
                    Point2D location = FlowerMaths.AddPoints(head.GetCenter()
                            , FlowerMaths.GetPetalPos(head.radius - sagitta, i));
                    thisFlyweight.AddPetal(location, i);
                }
                break;
        }
    }

    public enum PetalStyle {
        Overlapping,
        Touching,
    }

    public class PetalFlyweight {
        Petal petal;
        public List<Point2D> locations;
        public List<Float> rotations;

        PetalFlyweight(Petal petal) {
            this.petal = petal;
            this.petal.sprite.setOrigin(petal.sprite.getWidth() / 2, 0);
            locations = new ArrayList<Point2D>();
            rotations = new ArrayList<Float>();
        }

        PetalFlyweight(Petal petal, Point2D location) {
            this.petal = petal;
            this.petal.sprite.setOrigin(petal.sprite.getWidth() / 2, 0);
            locations = new ArrayList<Point2D>();
            rotations = new ArrayList<Float>();
            AddPetal(location, 0);
        }

        /**
         * @param location
         * @param rotation
         */
        void AddPetal(Point2D location, float rotation) {
            locations.add(location);
            rotations.add(rotation);
        }

        public void DrawCentered(SpriteBatch batch) {
            for (int i = 0; i < locations.size(); i++) {
                Point2D loc = locations.get(i);
                petal.SetPosWithRotationalOrigin(loc);
                petal.sprite.setRotation(-(rotations.get(i)));
                petal.sprite.draw(batch);
            }
        }

        public void ClearLocs() {
            locations = new ArrayList<Point2D>();
            rotations = new ArrayList<Float>();
        }

        public void ShiftLocs(float xShift, float yShift) {
            for(Point2D loc : locations) {
                loc.setLocation(loc.x + xShift, loc.y + yShift);
            }
        }
    }
}
