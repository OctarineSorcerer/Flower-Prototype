package com.mygdx.game.SaveItems;

/**
* Created by Dan on 16/03/2015.
*/
public class GrowthInfo {
    float latestGrowth;
    float bloomStart, bloomLength;
    public GrowthInfo(float currentGrowth, float bloomStart, float bloomLength) {
        this.latestGrowth = currentGrowth;
        this.bloomStart = bloomStart;
        this.bloomLength = bloomLength;
    }
    public GrowthInfo() {}
}
