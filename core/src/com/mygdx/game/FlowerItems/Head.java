package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.TintableElement;
import com.sun.javafx.geom.Point2D;

/**
 * Class representing the head of a flower
 */
public class Head extends TintableElement {
    float radius;

    public Head(String monochromeName, Color tintColour) {
        super("textures/heads/monochrome/", monochromeName, tintColour);
        radius = sprite.getWidth() / 2;
    }

    public void SetCenter(Point2D position) {
        SetPosWithOrigin(position, new Point2D(sprite.getWidth()/2, sprite.getHeight()/2));
    }
}
