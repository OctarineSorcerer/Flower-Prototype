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
public class Hole extends AnimationManager{ //Extends animation manager as it displays an animation
    public boolean beingDug = false; //Is the hole currently being dug?
    public boolean dug = false; //Has the hole been completely dug. This controls whether animation is playing
    private float x,y; //Location of hole

    /**
     * Constructor for the hole
     * @param x X location of hole
     * @param y Y location of hole
     */
    public Hole(float x, float y) {
        super(new Texture(Gdx.files.internal("textures/hole/diggydiggyhole.png")), 1, 6); //Loads the hole animation
        animation.setPlayMode(Animation.PlayMode.NORMAL); //Makes sure it doesn't loop
        animation.setFrameDuration(1f / 6f); //Sets the speed of the animation really

        this.x = x;
        this.y = y;
    }

    /**
     * Draws the current frame of the hole
     * @param batch Spritebatch to draw with
     * @param delta Change in time since last frame
     */
    public void draw(SpriteBatch batch, float delta) {
        if(animation.isAnimationFinished(getStateTime())) {
            dug = true;
            TextureRegion[] keyframes = animation.getKeyFrames();
            batch.draw(keyframes[keyframes.length - 1], x - 6, y - 6); //Draws the last frame only if animation is finished
        }
        else {
            TextureRegion region = GetFrame(delta); //Get current frame
            batch.draw(region, x - 6, y - 6); //Draw it
        }
    }
}
