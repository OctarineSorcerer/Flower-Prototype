package com.mygdx.game.SaveItems;

import java.util.Calendar;

/**
* Just holds a few pieces of information about growth in one place
*/
public class GrowthSave {
    public float latestGrowth, growthRate, previousGrowth;
    public float bloomStart, bloomLength;
    public long lastMilliCheck;

    public GrowthSave(float currentGrowth, float previousGrowth, float bloomStart, float bloomLength, float growthRate) {
        this.latestGrowth = currentGrowth;
        this.previousGrowth = previousGrowth;
        this.bloomStart = bloomStart;
        this.bloomLength = bloomLength;
        this.growthRate = growthRate;
        lastMilliCheck = Calendar.getInstance().getTimeInMillis();
    }
    public GrowthSave() {} //No-arg constructor for the JSON serializer
}
