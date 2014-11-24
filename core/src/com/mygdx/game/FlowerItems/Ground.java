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
    int iterations;
    public Ground(Texture groundTexture)
    {
        sprite = new Sprite(groundTexture);
        sprite.setPosition(0, 0);
        iterations = (int)(FlowerPrototype.WIDTH/sprite.getWidth() + 1);
    }
    public void Draw(SpriteBatch batch) {
        for (int i = 0; i < iterations; i++) {
            sprite.draw(batch);
            sprite.translateX(sprite.getWidth());
        }
        sprite.setX(0);
    }
}
