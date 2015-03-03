package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Dan on 27/02/2015.
 */
public class SaveInfo {
    public class HeadSave {

    }
    public class PetalsSave {

    }
    public class StemSave {
        long seed;
        Color colour;
        float width;
    }
    public class GrowthInfo {
        float latestGrowth;
        float bloomStart, bloomLength;

    }
}
