package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.TintableElement;
import com.sun.javafx.geom.Point2D;

/**
 * Class representing the head of a flower
 */
public class Head extends TintableElement {
    float radius;

    public Head(int monochromeIndex, Color tintColour, Point2D loc) {
        super("textures/heads/monochrome/", monochromeIndex, tintColour);
        sprite.setOriginCenter();
        sprite.setCenter(loc.x, loc.y);
        //sprite.scale(1.5f);
        radius = sprite.getWidth() / 2;
    }
}
