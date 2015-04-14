package com.mygdx.game.Superclasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class to handle any animation given
 */
public class AnimationManager {
    public Animation animation;
    float stateTime;

    public AnimationManager(Texture sheet, int sheetRows, int sheetCols) {;
        TextureRegion[][] temp = TextureRegion.split(sheet, sheet.getWidth()/sheetCols, sheet.getHeight()/sheetRows);

        TextureRegion[] frames = new TextureRegion[sheetRows*sheetCols];
        int index = 0;
        for (int i = 0; i < sheetRows; i++) {
            for (int j = 0; j < sheetCols; j++) {
                frames[index++] = temp[i][j];
            }
        } //Fill the 1D array
        animation = new Animation((sheetCols/sheetRows),frames);
        //sheet.dispose();
    }

    public float getStateTime() {
        return stateTime;
    }

    /**
     * Get the current frame of the animation, given a change in time
     * @param deltaTime Change in time since this was last called
     * @return A textureRegion corresponding to the current time
     */
    public TextureRegion GetFrame(float deltaTime) {
        stateTime += deltaTime;
        return animation.getKeyFrame(stateTime, true);
    }
}
