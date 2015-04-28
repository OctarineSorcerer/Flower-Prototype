package com.mygdx.game.FlowerItems;

import com.mygdx.game.SaveItems.GrowthSave;

import java.util.Calendar;

/**
 * Class to handle times and growing of things, stores in Growth
 */
public class GrowthHandling {

    private float stemGrowth, bloomGrowth;
    private float previousStem, previousBloom;

    public BloomInfo bloomInfo; //Info on the bloom times

    public float GrowthRate; //growth per time unit being used

    private Calendar lastChecked = Calendar.getInstance();

    /**
     * Constructor, given a savefile
     * @param info Save information about growth
     */
    public GrowthHandling(GrowthSave info) {
        GrowthRate = info.growthRate;
        previousStem = info.previousStemGrowth;
        stemGrowth = info.latestStemGrowth;

        previousBloom = info.previousBloomGrowth;
        bloomGrowth = info.latestBloomGrowth;

        lastChecked = Calendar.getInstance();
        lastChecked.setTimeInMillis(info.lastMilliCheck);

        bloomInfo = new BloomInfo(info.bloomStart, info.bloomLength);
    }

    public float Growth() {
        return stemGrowth;
    }
    public void SetGrowth(float growth) {
        stemGrowth = growth;
    }

    public float PreviousGrowth() {
        return previousStem;
    }
    public float BloomGrowth() {
        return bloomGrowth;
    }
    public float PreviousBloom() {
        return previousBloom;
    }

    /**
     * Checks the current time and applies the appropriate growth
     * @return The amount that has been grown
     */
    public float CheckTime() {
        previousStem = stemGrowth;
        previousBloom = bloomGrowth;
        Calendar calendar = Calendar.getInstance(); //Get now
        Calendar lastCheck = lastChecked; //Get then (when last checked)
        lastChecked = calendar; //Set last checked to now for next time

        long lastMilli = lastCheck.getTimeInMillis();
        long thisMilli = lastChecked.getTimeInMillis();
        long difference = thisMilli - lastMilli; //Difference between the two times in milliseconds
        float secondsDifference = (float) difference/1000f; //Seconds difference between the two values

        float toGrow = GrowthRate*secondsDifference; //Grows y appropriate amount
        stemGrowth += toGrow;
        bloomGrowth += toGrow;

        bloomInfo.Blooming =
                (bloomGrowth >= bloomInfo.bloomStart && bloomGrowth <= bloomInfo.bloomStart + bloomInfo.bloomLength); //Ascertains whether it is blooming
        return toGrow;
    }

    /**
     * Calculate the last bloom amount
     * @return The amount that was bloomed in the last growth
     */
    public float GetAmountLastBloomed() {
        return bloomInfo.BloomBetweenGrowths(previousBloom, bloomGrowth);
    }

    /**
     * Class for holding bloom info and calculating amounts bloomed
     */
    public class BloomInfo {
        private float bloomStart;
        private float bloomLength;
        private float bloomEnd; //This one isn't supplied, but is stored so it doesn't have to be calculated every time

        public boolean Blooming = false;

        /**
         * Constructor for bloom information holding
         * @param BloomStart Growth level at which the bloom should start
         * @param BloomLength How long the bloom should last
         */
        public BloomInfo(float BloomStart, float BloomLength) {
            bloomStart = BloomStart;
            bloomLength = BloomLength;
            bloomEnd = bloomStart + bloomLength; //Calculate bloom end
        }

        /**
         * Get the amount that has been bloomed between two growth levels
         * @param before Growth level before
         * @param after Growth level after
         * @return The amount of bloom time that resides between the two supplied growth levels
         */
        float BloomBetweenGrowths(float before, float after) {
            boolean beforeWithinBloom = before > bloomStart && before < bloomEnd;
            boolean afterWithinBloom = after > bloomStart && after < bloomEnd;
            boolean afterMoreThanBefore = after > before;

            if(before > bloomEnd || after < bloomStart) {
                return 0f; //no blooming here
            }
            //If it's gotten to this point, there's some blooming going on I believe
            if(beforeWithinBloom && afterWithinBloom) {
                //Entirely within the bloom area
                return Math.abs(after - before);
            }

            if(afterWithinBloom) { //Simplified boolean here
                //Only the end within bloom area
                if(afterMoreThanBefore) return after - bloomStart;
                else return bloomEnd - after;
            }

            if(beforeWithinBloom) {
                if(afterMoreThanBefore) return bloomEnd - before;
                else return before - bloomStart;
            }

            else { //both outside
                if(afterMoreThanBefore) {
                    if(after > bloomEnd && before < bloomStart) {
                        return bloomLength;
                    }
                    else return 0f;
                }
                else {
                    if(before > bloomEnd && after < bloomStart) {
                        return bloomLength;
                    }
                    else return 0f;
                }
            }
        }
        public float GetBloomStart() {
            return bloomStart;
        }
        public float GetBloomLength() {
            return bloomLength;
        }
    }
}
