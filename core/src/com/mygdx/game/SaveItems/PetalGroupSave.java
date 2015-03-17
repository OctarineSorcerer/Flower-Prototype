package com.mygdx.game.SaveItems;

import com.badlogic.gdx.graphics.Color;

/**
* Created by Dan on 16/03/2015.
*/
public class PetalGroupSave {
    public float bloomGrowthRate;
    public float xGrowthAfter;
    Color tintColour;
    String monochromePath;
    public PetalGroupSave(Color tintColour, String monochromePath, float bloomGrowth, float xGrowth) {
        this.tintColour = tintColour;
        this.monochromePath = monochromePath;
        this.bloomGrowthRate = bloomGrowth;
        this.xGrowthAfter = xGrowth;
    }
    public PetalGroupSave() {}
}
