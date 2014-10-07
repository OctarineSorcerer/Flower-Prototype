package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.*;
/**
 * Not an interface - the flower should be procedural
 */
public class Flower {

    public class Petal extends TintableElement {
        /**
         * Constructor for a petal
         * @param monochromeIndex The index of the monochrome image in the textures/petals/monochrome folder
         * @param tintColour The colour you wish to tint the petal
         */
        Petal(int monochromeIndex, Color tintColour) {
            super("textures/petals/monochrome/", monochromeIndex, tintColour);
        }
    }
    public class Head extends TintableElement {
        Head(int monochromeIndex, Color tintColour) {
            super("textures/heads/monochrome/", monochromeIndex, tintColour);
        }
    }
    class Stem extends TintableElement {
        int thickness;
        Stem(int monochromeIndex, Color tintColour) {
            super("textures/stems/monochrome/", monochromeIndex, tintColour);
        }
    }
}
