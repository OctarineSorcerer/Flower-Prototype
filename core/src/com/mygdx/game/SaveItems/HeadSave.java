package com.mygdx.game.SaveItems;

import com.badlogic.gdx.graphics.Color;

/**
* Created by Dan on 16/03/2015.
*/
public class HeadSave {
    String monochromePath;
    Color tintColour;
    public HeadSave(Color tintColour, String monochromePath) {
        this.tintColour = tintColour;
        this.monochromePath = monochromePath;
    }
    public HeadSave() {}
}
