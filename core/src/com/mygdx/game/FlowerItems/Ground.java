package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.FlowerPrototype;

/**
 * Class to represent the ground and to simplify its use as a tiled sprite
 */
public class Ground {
    private Sprite sprite;
    private float groundStart = 0, progress = 0;
    private int iterations, paddingLeft = 2, paddingRight = 3; //Higher amount of padding sprites for more vicious scrollers

    public Ground(Texture groundTexture) {
        sprite = new Sprite(groundTexture);
        sprite.setPosition(0, 0);
        iterations = (int)(FlowerPrototype.WIDTH/sprite.getWidth()); //1 extra for each side of screen?
    }
    public void Draw(SpriteBatch batch) {
        sprite.setX(groundStart - (paddingLeft * sprite.getWidth()));
        for (int i = 0; i < iterations + paddingRight*2; i++) {
            sprite.draw(batch);
            sprite.translateX(sprite.getWidth());
        }
    }
    public void IncrementStart(float amount) {
        float resultant = progress + amount;
        if(resultant >= sprite.getWidth() || resultant <= 0)
        {
            int wholeSpritesCovered = (int)Math.floor(resultant/sprite.getWidth());
            if (resultant <= 0) wholeSpritesCovered -= 1; //As this can easily go below 0 (ie onto previous sprite) yet not be above sprite width
            groundStart += wholeSpritesCovered * sprite.getWidth();

            progress = resultant - (sprite.getWidth() * wholeSpritesCovered);
        }
        else progress += amount;
    }
}
