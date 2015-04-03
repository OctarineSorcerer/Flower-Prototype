package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Superclasses.AnimationManager;

/**
 * Created by User on 12/03/2015.
 */
public class Hole extends AnimationManager{
    public boolean beingDug = false;
    private float x,y;

    public Hole(float x, float y) {
        super(new Texture(Gdx.files.internal("textures/hole/diggydiggyhole.png")), 1, 6);
        animation.setFrameDuration(1f / 6f);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        this.x = x;
        this.y = y;
    }
    public void draw(SpriteBatch batch, float delta) {
        TextureRegion region = GetFrame(delta);
        batch.draw(region, x - (region.getRegionWidth()/2), y);
    }
}
