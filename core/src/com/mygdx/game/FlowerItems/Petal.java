package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.TintableElement;

public class Petal extends TintableElement {
    float endScale = 2; //Scale of fully mature petal

    /**
     * Constructor for a petal
     *
     * @param monochromeIndex The index of the monochrome image in the textures/petals/monochrome folder
     * @param tintColour      The colour you wish to tint the petal
     */
    public Petal(int monochromeIndex, Color tintColour) {
        super("textures/petals/monochrome/", monochromeIndex, tintColour);
        sprite.setOriginCenter();
        //sprite.translateX(sprite.getWidth()/2);
    }
}
