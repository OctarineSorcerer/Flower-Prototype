package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Superclasses.AnimationManager;

/**
 * Watering can tool
 */
public class WateringCan extends AnimationManager implements ITool {
    /**
     * Load the watering can animation
     */
    public WateringCan() {
        super(new Texture(Gdx.files.internal("textures/tools/anims/WateringCanAnim.png")), 1, 7);
        animation.setFrameDuration(0.5f / 7f);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
     * Draw the watering can
     * @param batch Spritebatch to draw it with
     * @param x X position of watering can
     * @param y Y position of watering can
     * @param delta Change in time since last frame
     */
    @Override
    public void draw(SpriteBatch batch, float x, float y, float delta) {
        TextureRegion region = GetFrame(delta);
        batch.draw(region, x - region.getRegionWidth(), y - (region.getRegionHeight()/2));
    }

    /**
     * Apply the watering can
     * @param x Not necessarily used
     * @param y Not necessarily used
     */
    @Override
    public void apply(float x, float y) {
        if(GameScreen.testFlower.hole.dug) {
            GameScreen.testFlower.growth.GrowthRate = 0.25f; //start the flower growing
        }

        GameScreen.testFlower.LengthenStem(); //Increase the length of the stem
    }
}
