package com.mygdx.game.SaveItems;

import com.badlogic.gdx.graphics.Color;
import com.sun.javafx.geom.Point2D;

/**
* Created by Dan on 16/03/2015.
*/
public class StemSave {
    long seed;
    Point2D root;
    Color colour;
    int thickness;
    public StemSave(long seed, Color colour, int thickness, Point2D root) {
        this.seed = seed;
        this.colour = colour;
        this.thickness = thickness;
        this.root = root;
    }
    public StemSave() {}
}
