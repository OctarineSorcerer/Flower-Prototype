package com.mygdx.game.SaveItems;

import com.mygdx.game.FlowerItems.GrowthHandling;

import java.util.Calendar;

/**
* Just holds a few pieces of information about growth in one place
*/
public class GrowthInfo {
    public float latestGrowth, growthRate, previousGrowth;
    public float bloomStart, bloomLength;
    public long lastMilliCheck;

    public GrowthInfo(float currentGrowth,float previousGrowth ,float bloomStart, float bloomLength, float growthRate) {
        this.latestGrowth = currentGrowth;
        this.previousGrowth = previousGrowth;
        this.bloomStart = bloomStart;
        this.bloomLength = bloomLength;
        this.growthRate = growthRate;
        lastMilliCheck = Calendar.getInstance().getTimeInMillis();
    }
    public GrowthInfo(GrowthHandling growth) {
        this.latestGrowth = growth.Growth;
        this.bloomStart = growth.bloomInfo.GetBloomStart();
        this.bloomLength = growth.bloomInfo.GetBloomLength();
        this.growthRate = growth.GrowthRate;
        this.lastMilliCheck = growth.GetLastMilliCheck();
    }
    public GrowthInfo() {} //No-arg constructor for the JSON serializer
}
