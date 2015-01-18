package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BezierInstructions;
import com.sun.javafx.geom.Point2D;

import java.util.Random;

public class Stem {
        int thickness = 20;
        int width = 200; //default 200, same as in BezierInstructions
        Point2D stemTip = new Point2D();

        public BezierInstructions curveInfo;

        public Stem() {
            CreateStemCurve();
        }

        public void CreateStemCurve() {
            BezierInstructions bez = new BezierInstructions();
            stemTip.x = bez.tipX; stemTip.y = bez.tipY;
            curveInfo = bez;
        }
    }
