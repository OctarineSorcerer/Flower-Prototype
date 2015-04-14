package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Superclasses.AnimationManager;

/**
 * Class for diggin' of holes
 */
public class Hole extends AnimationManager{
    public boolean beingDug = false;
    public boolean dug = false;
    private float x,y;

    public Hole(float x, float y) {
        super(new Texture(Gdx.files.internal("textures/hole/diggydiggyhole.png")), 1, 6);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        animation.setFrameDuration(1f / 6f);

        this.x = x;
        this.y = y;
    }
    public void draw(SpriteBatch batch, float delta) {
        if(animation.isAnimationFinished(getStateTime())) {
            dug = true;
            TextureRegion[] keyframes = animation.getKeyFrames();
            batch.draw(keyframes[keyframes.length - 1], x - 6, y - 6);
        }
        else {
            TextureRegion region = GetFrame(delta);
            batch.draw(region, x - 6, y - 6);
        }
    }
}
