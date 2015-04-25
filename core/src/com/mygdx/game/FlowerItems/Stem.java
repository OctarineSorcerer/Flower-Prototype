package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BezierInstructions;
import com.sun.javafx.geom.Point2D;

import java.awt.*;
import java.util.Random;

public class Stem {
        public int thickness = 20;
        public Color colour = Color.GREEN;
        public int curves = 4; //default 200, same as in BezierInstructions
        Vector2 stemTip = new Vector2();

        public BezierInstructions curveInfo;

        public Stem(long seed, int curves) {
            this.curves = curves;
            CreateStemCurve(seed);
        }

        public void CreateStemCurve(long seed) {
            BezierInstructions bez = new BezierInstructions(seed, curves);
            stemTip.x = bez.tipX; stemTip.y = bez.tipY;
            curveInfo = bez;
        }
    }
