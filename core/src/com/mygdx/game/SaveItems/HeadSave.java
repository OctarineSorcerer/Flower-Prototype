package com.mygdx.game.SaveItems;

import com.badlogic.gdx.graphics.Color;

/**
* Save information about the flower's head
*/
public class HeadSave {
    String monochromePath;
    Color tintColour;
    public HeadSave(Color tintColour, String monochromePath) {
        this.tintColour = tintColour;
        this.monochromePath = monochromePath;
    }
    public HeadSave() {} //No-arg constructor for the JSON serializer
}
