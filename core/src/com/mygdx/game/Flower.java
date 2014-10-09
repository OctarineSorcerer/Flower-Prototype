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
        petals = new ArrayList<PetalFlyweight>();
    }
    void ArrangePetals(PetalStyle arrangement)
    {

    }

    public static class Petal extends TintableElement {
        /**
         * Constructor for a petal
         * @param monochromeIndex The index of the monochrome image in the textures/petals/monochrome folder
         * @param tintColour The colour you wish to tint the petal
         */
        Petal(int monochromeIndex, Color tintColour) {
            super("textures/petals/monochrome/", monochromeIndex, tintColour);
        }
    }
    public static class Head extends TintableElement {
        Head(int monochromeIndex, Color tintColour) {
            super("textures/heads/monochrome/", monochromeIndex, tintColour);
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
        PetalFlyweight(Petal petal, Point location)
        {
            this.petal = petal;
            locations = new ArrayList<Point>();
            AddPetal(location);
        }
        void AddPetal(Point location)
        {
            locations.add(location);
        }
        public int Amount()
        {
            return locations.size();
        }
    }
}
