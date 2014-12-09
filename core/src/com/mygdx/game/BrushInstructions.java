package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;

import java.util.List;

/**
 * Created by Dan on 08/12/2014.
 */
public class BrushInstructions {
    List<BrushForce> forces;
    private class BrushForce {
        Vector2 force;
        int duration;
        int start;
    }
    public void Execute(int width, int height) {
        Pixmap canvas = new Pixmap(width, height, Pixmap.Format.RGBA4444);
        Point2D brushPos = new Point2D(width/2, height/2);
        for (BrushForce force : forces) {

        }
    }
}
