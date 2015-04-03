package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Tool interface! At the moment they only really use apply, but that may change and it's still some nice functionality
 */
public interface ITool {
    void draw(SpriteBatch batch, float x, float y, float delta);
    void apply(float x, float y);
}
