package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FlowerPrototype;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Superclasses.AnimationManager;

/**
 *
 */
public class Shovel extends AnimationManager implements ITool {
    public Shovel() {
        super(new Texture(Gdx.files.internal("textures/tools/anims/SpadeAnim.png")), 1, 8);
        animation.setFrameDuration(0.5f / 8f);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
     * Will draw the current frame of shovel. Centered too. Make sure the spritebatch has started
     * @param batch Spritebatch
     * @param x X position of where to draw the frame
     * @param y Y position of where to draw the frame
     * @param delta Change in time since last drawn
     */
    @Override
    public void draw(SpriteBatch batch, float x, float y, float delta) {
        TextureRegion region = GetFrame(delta);
        batch.draw(region, x - (region.getRegionWidth()/2), y/* - (region.getRegionHeight()/2)*/);
    }

    @Override
    public void apply(float x, float y) {
        Vector2 applyPoint = new Vector2(x, y);
        Vector2 rootPoint = new Vector2(GameScreen.testFlower.rootLoc.x,
                FlowerPrototype.HEIGHT - GameScreen.testFlower.rootLoc.y); //translate into the OTHER kind of y
        float distance = rootPoint.dst(applyPoint);
        if (distance < 30) {
            GameScreen.testFlower.hole.beingDug = true;
        }
    }
}
