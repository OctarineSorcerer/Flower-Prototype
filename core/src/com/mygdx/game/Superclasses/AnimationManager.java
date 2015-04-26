package com.mygdx.game.Superclasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class to create an animation from a given texture
 */
public class AnimationManager {
    protected Animation animation; //Internal animation class
    private float stateTime;

    /**
     * Animation constructor
     * @param sheet Texture/animationsheet. Contains all frames of animation
     * @param sheetRows Rows in the animation sheet
     * @param sheetCols Columns in the animation sheet
     */
    protected AnimationManager(Texture sheet, int sheetRows, int sheetCols) {
        TextureRegion[][] temp = TextureRegion.split(sheet, sheet.getWidth()/sheetCols, sheet.getHeight()/sheetRows);
        //Get each cell of the animation matrix

        TextureRegion[] frames = new TextureRegion[sheetRows*sheetCols]; //All cells flattened into an array
        int index = 0;
        for (int i = 0; i < sheetRows; i++) { //Fill the 1D array
            for (int j = 0; j < sheetCols; j++) {
                frames[index++] = temp[i][j];
            }
        }
        animation = new Animation((sheetCols/sheetRows),frames); //Create an animation from this
        //sheet.dispose();
    }

    protected float getStateTime() {
        return stateTime;
    }

    /**
     * Get the current frame of the animation, given a change in time
     * @param deltaTime Change in time since this was last called
     * @return A textureRegion corresponding to the current time
     */
    protected TextureRegion GetFrame(float deltaTime) {
        stateTime += deltaTime;
        return animation.getKeyFrame(stateTime, true);
    }
}
