package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.TintableElement;

public class Petal extends TintableElement {
    float endScale = 2; //Scale of fully mature petal
    float bloomGrowthRate = 1f; //amount it grows per growth while blooming

    /**
     * Constructor for a petal
     *
     * @param monochromeIndex The index of the monochrome image in the textures/petals/monochrome folder
     * @param tintColour      The colour you wish to tint the petal
     */
    public Petal(String monochromeName, Color tintColour) {
        super("textures/petals/monochrome/", monochromeName, tintColour);
        sprite.setOriginCenter();
        //sprite.translateX(sprite.getWidth()/2);
    }
}
