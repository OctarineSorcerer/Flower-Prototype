package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.FlowerPrototype;

/**
 * Class to represent the ground and to simplify its use as a tiled sprite
 */
public class Ground {
    private Sprite sprite;
    private float groundStart = 0, progress = 0;
    private int iterations, paddingLeft = 2, paddingRight = 3; //Higher amount of padding sprites for more vicious scrollers

    /**
     * Constructor, calculates how many times ground should be iterated
     * @param groundTexture Texture the ground will have. Should be horizontally tileable
     */
    public Ground(Texture groundTexture) {
        sprite = new Sprite(groundTexture);
        sprite.setPosition(0, 0);
        CalculateIterations();
    }

    /**
     * Calculates how many times the sprite will fit into the screen
     */
    public void CalculateIterations() {
        iterations = (int)(FlowerPrototype.WIDTH/sprite.getWidth());
    }

    /**
     * Draws the ground, tiling it
     * @param batch Spritebatch to draw the ground with
     */
    public void Draw(SpriteBatch batch) {
        sprite.setX(groundStart - (paddingLeft * sprite.getWidth())); //Sets the leftmost position of the sprite
        for (int i = 0; i < iterations + paddingRight*2; i++) { //Do the correct amount to the sides also (prevents flashes)
            sprite.draw(batch);
            sprite.translateX(sprite.getWidth()); //Shift sprite across to next render position
        }
    }

    /**
     * Increments the x position of where the ground starts (this is trickier than you'd think cos tiles)
     * @param amount Amount you want to shift the ground
     */
    public void IncrementStart(float amount) {
        float resultant = progress + amount; //progress is basically the amount the ground start has accumulated without actually being put into effect
        if(resultant >= sprite.getWidth() || resultant <= 0) //If shift results in crossing of a sprite boundary
        {
            int wholeSpritesCovered = (int)Math.floor(resultant/sprite.getWidth()); //Get how many sprites it has shifted across
            if (resultant <= 0) wholeSpritesCovered -= 1; //As this can easily go below 0 (ie onto previous sprite) yet not be above sprite width
            groundStart += wholeSpritesCovered * sprite.getWidth(); //Actually move groundStart

            progress = resultant - (sprite.getWidth() * wholeSpritesCovered); //Set partial progress that may still be left
        }
        else progress += amount; //If it doesn't add up to a sprite boundary, just accumulate it
    }
}
