package com.mygdx.game.SaveItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
* Class representing stem details
*/
public class StemSave {
    long seed;
    Vector2 root;
    Color colour;
    int thickness, curves;

    public StemSave(long seed, Color colour, int thickness,int curves , Vector2 root) {
        this.seed = seed;
        this.colour = colour;
        this.thickness = thickness;
        this.curves = curves;
        this.root = root;
    }
    public StemSave() {} //No argument constructor for serializer
}
