package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Tool interface! All tools must draw and have an effect when they are applied
 */
public interface ITool {
    void draw(SpriteBatch batch, float x, float y, float delta);
    void apply(float x, float y);
}
