package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TintableElement;

/**
 * Class representing the head of a flower
 */
public class Head extends TintableElement {
    float radius; //Radius of the head (Please keep it circular)

    /**
     * Constructor for head
     * @param monochromeName Name of the monochrome image within "textures/heads/monochrome/"
     * @param tintColour Colour the head should be tinted
     */
    public Head(String monochromeName, Color tintColour) {
        super("textures/heads/monochrome/", monochromeName, tintColour);
        radius = sprite.getWidth() / 2;
    }

    /**
     * Set the center of the head to a position
     * @param position Vector containing x and y positions of the new center
     */
    public void SetCenter(Vector2 position) {
        SetPosWithOrigin(position, new Vector2(sprite.getWidth()/2, sprite.getHeight()/2));
    }
}
