package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.TintableElement;
import com.sun.javafx.geom.Point2D;

/**
 * Class representing the head of a flower
 */
public class Head extends TintableElement {
    float radius;

    public Head(int monochromeIndex, Color tintColour) {
        super("textures/heads/monochrome/", monochromeIndex, tintColour);
        radius = sprite.getWidth() / 2;
    }
}
