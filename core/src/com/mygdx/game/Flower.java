package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.javafx.geom.Point2D;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Not an interface - the flower should be procedural
 */
public class Flower { //These are their own classes as they may need unique functionality later
    static DebugUtils.DebugCross debugCross = new DebugUtils.DebugCross(0, Color.GREEN);
    public List<PetalFlyweight> petals;
    public Head head;
    public Stem stem;

    Flower(Petal mainPetal, Head flowerHead, Stem flowerStem, int petalCount, PetalStyle petalArrangement)
    {
        head = flowerHead;
        petals = new ArrayList<PetalFlyweight>();
        petals.add(new PetalFlyweight(new Petal(0, Color.CYAN)));
        ArrangePetals(petalArrangement, petalCount, 0);
    }
    void ArrangePetals(PetalStyle arrangement, int count, int petalIndex)
    {
        float sepAngle = 360f/(float)count;
        switch (arrangement)
        {
            case Touching:
                PetalFlyweight thisFlyweight = petals.get(petalIndex);
                float petalWidth = FlowerMaths.GetPetalWidth(sepAngle, 0.5f, head.radius);
                Petal relevantPetal = thisFlyweight.petal;
                relevantPetal.sprite.setOrigin(relevantPetal.sprite.getWidth()/2, 0); //origin at bottom thingy
                relevantPetal.Scale(petalWidth / relevantPetal.sprite.getWidth()); //scales petal
                for(float i = 0; i < sepAngle*count; i+= sepAngle)
                {
                    Point2D location = FlowerMaths.AddPoints(head.GetCenter()
                            , FlowerMaths.GetPetalPos(head.radius, i));
                    //location.y += relevantPetal.sprite.getHeight();
                    //location.x += relevantPetal.sprite.getWidth();
                    thisFlyweight.AddPetal(location, i);
                }
                break;
        }
    }

    public static class Petal extends TintableElement {
        /**
         * Constructor for a petal
         * @param monochromeIndex The index of the monochrome image in the textures/petals/monochrome folder
         * @param tintColour The colour you wish to tint the petal
         */
        Petal(int monochromeIndex, Color tintColour) {
            super("textures/petals/monochrome/", monochromeIndex, tintColour);
            sprite.setOriginCenter();
            //sprite.translateX(sprite.getWidth()/2);
        }
    }
    public static class Head extends TintableElement {
        float radius;
        Head(int monochromeIndex, Color tintColour, Point2D loc) {
            super("textures/heads/monochrome/", monochromeIndex, tintColour);
            sprite.setOriginCenter();
            sprite.setCenter(loc.x, loc.y);
            //sprite.scale(1.5f);
            radius = sprite.getWidth()/2;
        }
    }
    public static class Stem extends TintableElement {
        int thickness;
        int height;
        int snake; //Amount the stem can wibble

        Stem(int monochromeIndex, Color tintColour) {
            super("textures/stems/monochrome/", monochromeIndex, tintColour);
        }
    }

    public static enum PetalStyle
    {
        Overlapping,
        Touching,
    }

    static class PetalFlyweight {
        Petal petal;
        List<Point2D> locations;
        List<Float> rotations;
        PetalFlyweight(Petal petal) {
            this.petal = petal;
            this.petal.sprite.setOrigin(petal.sprite.getWidth()/2, 0);
            locations = new ArrayList<Point2D>();
            rotations = new ArrayList<Float>();
        }
        PetalFlyweight(Petal petal, Point2D location) {
            this.petal = petal;
            this.petal.sprite.setOrigin(petal.sprite.getWidth()/2, 0);
            locations = new ArrayList<Point2D>();
            rotations = new ArrayList<Float>();
            AddPetal(location, 0);
        }

        /**
         *
         * @param location
         * @param rotation
         */
        void AddPetal(Point2D location, float rotation) {
            locations.add(location);
            rotations.add(rotation);
        }
        public int Amount()
        {
            return locations.size();
        }
        public void DrawCentered(SpriteBatch batch) {
            for (int i = 0; i < locations.size(); i++) {
                Point2D loc = locations.get(i);
                petal.SetPosWithRotationalOrigin(loc);
                petal.sprite.setRotation(-(rotations.get(i)));
                petal.sprite.draw(batch);
            }
        }
    }
}
