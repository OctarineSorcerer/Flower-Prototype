package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.TintableElement;

public class Stem extends TintableElement {
        int thickness;
        int height;
        int snake; //Amount the stem can wibble

        public Stem(int monochromeIndex, Color tintColour) {
            super("textures/stems/monochrome/", monochromeIndex, tintColour);
        }
    }
