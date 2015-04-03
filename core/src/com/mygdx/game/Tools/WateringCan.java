package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Superclasses.AnimationManager;

/**
 * Created by Dan on 02/04/2015.
 */
public class WateringCan extends AnimationManager implements ITool {
    public WateringCan() {
        super(new Texture(Gdx.files.internal("textures/tools/anims/WateringCanAnim.png")), 1, 7);
        animation.setFrameDuration(0.5f / 7f);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y, float delta) {
        TextureRegion region = GetFrame(delta);
        batch.draw(region, x - region.getRegionWidth(), y - (region.getRegionHeight()/2));
    }

    @Override
    public void apply(float x, float y) {

    }
}
