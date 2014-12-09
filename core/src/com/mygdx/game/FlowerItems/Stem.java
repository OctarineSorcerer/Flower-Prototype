package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TintableElement;

import java.util.List;

public class Stem extends TintableElement {
        int thickness;
        int height;
        int snake; //Amount the stem can wibble

        public Stem(int monochromeIndex, Color tintColour) {
            super("textures/stems/monochrome/", monochromeIndex, tintColour);
        }
        public void CreateStemTexture() {
            //What is needed: forces, at what times, and for what duration


        }
    }
