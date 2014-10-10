package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Not an interface - the flower should be procedural
 */
public class Flower { //These are their own classes as they may need unique functionality later
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
        float sepAngle = 360/count;
        switch (arrangement)
        {
            case Touching:
                PetalFlyweight thisFlyweight = petals.get(petalIndex);
                float petalWidth = FlowerMaths.GetPetalWidth(sepAngle, (float) 1, head.radius);
                Petal relevantPetal = thisFlyweight.petal;
                relevantPetal.Scale(petalWidth / relevantPetal.sprite.getWidth()); //scales petal
                for(int i = 0; i < sepAngle*count; i+= sepAngle)
                {
                    Point location = FlowerMaths.AddPoints(new Point((int) head.sprite.getX(), (int) head.sprite.getY())
                            , FlowerMaths.GetPetalPos(head.radius, i));
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
            sprite.setOrigin(sprite.getWidth()/2, 0);
        }
    }
    public static class Head extends TintableElement {
        float radius;
        Head(int monochromeIndex, Color tintColour, Point loc) {
            super("textures/heads/monochrome/", monochromeIndex, tintColour);
            sprite.setX(loc.x);
            sprite.setY(loc.y);
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
        List<Point> locations;
        List<Float> rotations;
        PetalFlyweight(Petal petal) {
            this.petal = petal;
            locations = new ArrayList<Point>();
            rotations = new ArrayList<Float>();
        }
        PetalFlyweight(Petal petal, Point location) {
            this.petal = petal;
            locations = new ArrayList<Point>();
            rotations = new ArrayList<Float>();
            AddPetal(location, 0);
        }
        void AddPetal(Point location, float rotation) {
            locations.add(location);
            rotations.add(rotation);
        }
        public int Amount()
        {
            return locations.size();
        }
    }
}
