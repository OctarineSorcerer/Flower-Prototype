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

public class Stem extends TintableElement {
        int thickness;
        Point2D spriteTip;
        int snake; //Amount the stem can wibble

        public Stem(int monochromeIndex, Color tintColour) {
            super("textures/stems/monochrome/", monochromeIndex, tintColour);
        }

        public Stem() {
            super(CreateStemSprite());
        }

        public static Sprite CreateStemSprite() {
            Vector2[] vectors = new Vector2[]
                    {
                            new Vector2(0,0),
                            new Vector2(60, 150),
                            new Vector2(-60, 300),
                            new Vector2(0, 400),
                    };
            Texture stemTex = BezierInstructions.DrawBezier(vectors, new Pixmap(200, 400, Pixmap.Format.RGBA4444));
            Sprite output = new Sprite(stemTex);
            return output;
            //Shift that later
        }
    }
