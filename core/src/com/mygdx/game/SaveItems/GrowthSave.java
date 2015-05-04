package com.mygdx.game.SaveItems;

import com.mygdx.game.FlowerItems.GrowthHandling;
import java.util.Calendar;

/**
* Just holds a few pieces of information about growth in one place
*/
public class GrowthSave {
    public float growthRate;
    public float latestStemGrowth, previousStemGrowth;
    public float latestBloomGrowth, previousBloomGrowth;
    public float bloomStart, bloomLength;
    public long lastMilliCheck;

    public GrowthSave(float stemGrowth, float previousStemGrowth,
                      float bloomGrowth, float previousBloomGrowth,
                      float bloomStart, float bloomLength, float growthRate) {
        this.latestStemGrowth = stemGrowth;
        this.previousStemGrowth = previousStemGrowth;

        this.latestBloomGrowth = bloomGrowth;
        this.previousBloomGrowth = previousBloomGrowth;

        this.bloomStart = bloomStart;
        this.bloomLength = bloomLength;
        this.growthRate = growthRate;
        lastMilliCheck = Calendar.getInstance().getTimeInMillis();
    }
    public GrowthSave(GrowthHandling growth) {
        this.latestStemGrowth = growth.Growth();
        this.previousStemGrowth = growth.PreviousGrowth();

        this.latestBloomGrowth = growth.BloomGrowth();
        this.previousBloomGrowth = growth.PreviousBloom();

        this.bloomStart = growth.bloomInfo.GetBloomStart();
        this.bloomLength = growth.bloomInfo.GetBloomLength();
        this.growthRate = growth.GrowthRate;
        lastMilliCheck = Calendar.getInstance().getTimeInMillis();
    }
    public GrowthSave() {} //No-arg constructor for the JSON serializer
}
