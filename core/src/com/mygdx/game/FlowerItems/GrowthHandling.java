package com.mygdx.game.FlowerItems;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class to handle times and growing of things, stores in Growth
 */
public class GrowthHandling {

    public float Growth; //How much the item has grown so far

    public float BloomStart; //When the (Orlando) bloom starts
    public int BloomLength;
    public boolean Blooming = false;

    public float GrowthRate; //growth per irl day

    private Calendar lastCheck;
    private Calendar thisCheck;

    /**
     * Checks the current time and applies the appropriate growth
     */
    public void CheckTime() {
        Calendar calendar = Calendar.getInstance();
        lastCheck = thisCheck;
        thisCheck = calendar;

        long lastMilli = lastCheck.getTimeInMillis();
        long thisMilli = lastCheck.getTimeInMillis();
        long difference = TimeUnit.MILLISECONDS.toDays(thisMilli - lastMilli);

        Growth += GrowthRate*difference;

        Blooming = (Growth >= BloomStart && Growth <= BloomStart + BloomLength); //Ascertains whether it is blooming
    }
}
