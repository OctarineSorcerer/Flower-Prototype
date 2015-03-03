package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BezierInstructions;
import com.sun.javafx.geom.Point2D;

import java.util.Random;

public class Stem {
        int thickness = 20;
        int width = 200; //default 200, same as in BezierInstructions
        Vector2 stemTip = new Vector2();

        public BezierInstructions curveInfo;

        public Stem(long seed) {
            CreateStemCurve(seed);
        }

        public void CreateStemCurve(long seed) {
            BezierInstructions bez = new BezierInstructions(seed);
            stemTip.x = bez.tipX; stemTip.y = bez.tipY;
            curveInfo = bez;
        }
    }
