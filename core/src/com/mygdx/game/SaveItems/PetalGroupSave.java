package com.mygdx.game.SaveItems;

import com.badlogic.gdx.graphics.Color;

/**
* Save information about a petalgroup
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
    public PetalGroupSave() {} //No-argument constructor for the JSON serializer
}
