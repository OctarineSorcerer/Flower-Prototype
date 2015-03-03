package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TintableElement;

/**
 * Class representing the head of a flower
 */
public class Head extends TintableElement {
    float radius;

    public Head(String monochromeName, Color tintColour) {
        super("textures/heads/monochrome/", monochromeName, tintColour);
        radius = sprite.getWidth() / 2;
    }

    public void SetCenter(Vector2 position) {
        SetPosWithOrigin(position, new Vector2(sprite.getWidth()/2, sprite.getHeight()/2));
    }
}
