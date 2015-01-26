package com.mygdx.game.FlowerItems;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dan on 26/01/2015.
 */
public class GrowthHandling {

    public float Growth; //How much the flower has grown so far
    public float BloomStart; //When the (Orlando) bloom starts
    public int BloomLength;

    public float GrowthRate; //growth per irl day

    Calendar lastCheck;
    Calendar thisCheck;

    public void CheckTime() {
        Calendar calendar = Calendar.getInstance();
        lastCheck = thisCheck;
        thisCheck = calendar;


    }
}
