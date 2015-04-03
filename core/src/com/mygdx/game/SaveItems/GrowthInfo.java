package com.mygdx.game.SaveItems;

/**
* Just holds a few pieces of information about growth in one place
*/
public class GrowthInfo {
    float latestGrowth;
    float bloomStart, bloomLength;
    public GrowthInfo(float currentGrowth, float bloomStart, float bloomLength) {
        this.latestGrowth = currentGrowth;
        this.bloomStart = bloomStart;
        this.bloomLength = bloomLength;
    }
    public GrowthInfo() {} //No-arg constructor for the JSON serializer
}
