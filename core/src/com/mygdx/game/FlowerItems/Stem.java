package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BezierManager;

public class Stem {
    public int thickness = 20; //Default thickness of stem
    public Color colour = Color.GREEN; //Default colour of stem
    public int curveNumber = 4; //Amount of bezier curveNumber present in the stem by default
    Vector2 stemTip = new Vector2();

    public BezierManager curveInfo; //Info about the curveNumber that make up the stem

    /**
     * Constructor for the stem
     * @param seed Seed for random number generation that this stem uses
     * @param curveNumber The amount of curveNumber this stem should have
     */
    public Stem(long seed, int curveNumber) {
        this.curveNumber = curveNumber;
        CreateStemCurve(seed);
    }

    /**
     * Creates this stem's curves, given a random seed
     * @param seed Seed to generate random numbers with
     */
    private void CreateStemCurve(long seed) {
        BezierManager bez = new BezierManager(seed, curveNumber);
        stemTip.x = bez.tipX; stemTip.y = bez.tipY;
        curveInfo = bez;
    }
}
