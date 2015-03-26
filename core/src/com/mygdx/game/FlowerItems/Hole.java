package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by User on 12/03/2015.
 */
public class Hole {
    Sprite endSprite = new Sprite(new Texture(Gdx.files.internal("textures/hole/duggyduggyhole.png")));
    Animation HoleAnimation;
    float stateTime;
    public Hole() {
        Texture animSheet = new Texture(Gdx.files.internal("textures/hole/diggydiggyhole.png"));
        TextureRegion[][] temp = TextureRegion.split(animSheet, animSheet.getWidth()/6, animSheet.getHeight());

        TextureRegion[] frames = new TextureRegion[5];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 6; j++) {
                frames[index++] = temp[i][j];
            }
        } //Fill the 1D array
        HoleAnimation = new Animation((1f/6f),frames);
    }
    public TextureRegion GetFrame(float deltaTime) {
        stateTime += deltaTime;
        TextureRegion currentFrame = HoleAnimation.getKeyFrame(stateTime, true);
        return currentFrame;
    }
}
