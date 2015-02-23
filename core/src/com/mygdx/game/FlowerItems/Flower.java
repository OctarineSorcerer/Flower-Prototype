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

    public GrowthHandling growth;

    private int count;
    private boolean petalsOutside = false;
    private PetalStyle style;

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

    void ArrangePetals(PetalStyle arrangement, int count, int petalIndex) {
        float sepAngle = 360f / (float) count;
        switch (arrangement) {
            case Touching:
                PetalFlyweight thisFlyweight = petals.get(petalIndex);
                Petal relevantPetal = thisFlyweight.petal;
                relevantPetal.sprite.setOrigin(relevantPetal.sprite.getWidth() / 2, 0); //origin at bottom thingy
                //relevantPetal.Scale(petalWidth / relevantPetal.sprite.getWidth()); //scales petal

                float ratio = relevantPetal.sprite.getHeight() / head.radius;
                growth = new GrowthHandling(1f, 6f, 1 + ratio);

                System.out.println("Original X scale: " + relevantPetal.sprite.getScaleX() +
                                    " | Y: " + relevantPetal.sprite.getScaleY());

                relevantPetal.Scale(-ratio); //Sets top of petal to the middle of the head

                System.out.println("Second X scale: " + relevantPetal.sprite.getScaleX() +
                        " | Y: " + relevantPetal.sprite.getScaleY());

                SetPetalPositions(sepAngle, thisFlyweight);
                break;
        }
    }

    public void SetPetalPositions(float sepAngle, PetalFlyweight petalGroup) {
        Petal relevantPetal = petalGroup.petal;
        float sagitta = (float)(head.radius - Math.sqrt(Math.pow(head.radius, 2)
                - Math.pow(0.5*relevantPetal.sprite.getWidth(), 2)));
        //^That bit is the height of the arc. Yeah. Go maths. http://www.mathopenref.com/sagitta.html
        petalGroup.ClearLocs();
        for (float i = 0; i < sepAngle * count; i += sepAngle) {
            Point2D location = FlowerMaths.AddPoints(head.GetCenter()
                    , FlowerMaths.GetPetalPos(head.radius - sagitta, i));
            petalGroup.AddPetal(location, i);
        }
    }

    public void ApplyGrowth() {
        float grown = growth.CheckTime();

        //petals/blooming
        float bloomAmount = growth.GetAmountLastBloomed();
        if(bloomAmount > 0) {
            for(PetalFlyweight fly : petals) {
                float scaleByX = fly.petal.sprite.getScaleX() - 1 + bloomAmount * fly.petal.bloomGrowthRate;
                float scaleByY = fly.petal.sprite.getScaleY() - 1 + bloomAmount * fly.petal.bloomGrowthRate;
                fly.petal.sprite.setScale(scaleByX,
                        scaleByY);
                if(fly.petal.sprite.getScaleY() > 0) petalsOutside = true;
                System.out.println("Grown X:" + fly.petal.sprite.getScaleX() + " | Y: " + fly.petal.sprite.getScaleY());
            }
            System.out.println("Bloomed: " + bloomAmount);
        }
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
        if(petalsOutside) {
            for (Flower.PetalFlyweight petalType : petals) {
                petalType.DrawCentered(batch);
            }
            head.sprite.draw(batch);
        }
        else {
            head.sprite.draw(batch);
            for (Flower.PetalFlyweight petalType : petals) {
                petalType.DrawCentered(batch);
            }
        }
    }

    public void DrawShapes(ShapeRenderer shapeRenderer) {
        stem.curveInfo.DrawSome(shapeRenderer, new Vector2(rootLoc.x, rootLoc.y), growth.Growth);
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
         * @param location Location of the new petal to add
         * @param rotation Rotation of the new petal to add
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
