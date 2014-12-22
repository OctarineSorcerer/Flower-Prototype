package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.FlowerPrototype;

/**
 * Created by Dan on 03/11/2014.
 */
public class Ground {
    public Sprite sprite;
    float groundStart = 0, progress = 0;
    int iterations;

    public Ground(Texture groundTexture) {
        sprite = new Sprite(groundTexture);
        sprite.setPosition(0, 0);
        iterations = (int)(FlowerPrototype.WIDTH/sprite.getWidth() + 2); //one extra for each side of screen
    }
    public void Draw(SpriteBatch batch) {
        for (int i = 0; i < iterations; i++) {
            sprite.draw(batch);
            sprite.translateX(sprite.getWidth());
        }
        sprite.setX(groundStart);
    }
    public void IncrementStart(float amount) {
        if(progress + amount >= sprite.getWidth())
        {
            groundStart += sprite.getWidth()* Math.floor((amount + progress)/sprite.getWidth());
            progress = (progress + amount) - sprite.getWidth();
            //This will only work correctly if this is incremented less than 1 sprite width at a time
            //However, this is not likely to happen, and so the modulo technique will not be used
        }
        else progress += amount;
    }
    public void DecrementStart(float amount) {
        if(progress - amount < 0)
        {
            progress = (sprite.getWidth() - (amount - progress));
            groundStart -= sprite.getWidth() * Math.floor((amount + progress)/sprite.getWidth());
        } else progress -= amount;
    }
}
