package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BezierInstructions;
import com.mygdx.game.TintableElement;
import com.sun.javafx.geom.Point2D;

import java.util.List;
import java.util.Random;

public class Stem extends TintableElement {
        int thickness = 20;
        Point2D stemTip = new Point2D();

        public Stem(int monochromeIndex, Color tintColour) {
            super("textures/stems/monochrome/", monochromeIndex, tintColour);
        }

        public Stem() {
            super();
            sprite = CreateStemSprite(new Random().nextLong());
        }

        public Sprite CreateStemSprite(long seed) {
            int width = 200;
            BezierInstructions bez = new BezierInstructions(BezierInstructions.GeneratePoints(seed, 4, width));
            Vector2 endVector = bez.points[bez.points.length - 1];
            Texture stemTex = bez.Draw(new Pixmap((int) (width * 1.5), (int) (endVector.y * 1.5), Pixmap.Format.RGBA4444), thickness);
            stemTip.x = bez.tipX; stemTip.y = bez.tipY;
            Sprite output = new Sprite(stemTex);
            return output;
            //Shift that later
        }
    }
