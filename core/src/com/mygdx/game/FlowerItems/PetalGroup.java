package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TintableElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing a sprite and a list of locations/rotations for it to go
 */
public class PetalGroup extends TintableElement {
    float bloomGrowthRate = 1f; //Y amount it grows per time unit while blooming
    float xGrowthAfter = 0f; //X amount it grows per time unit while blooming

    private List<LocRotPair> places = new ArrayList<LocRotPair>(); //Location/rotation pairs

    /**
     * Constructor for a petal
     * @param monochromeName The index of the monochrome image in the "textures/petals/monochrome" folder
     * @param tintColour The colour you wish to tint the petal
     */
    public PetalGroup(String monochromeName, Color tintColour) {
        super("textures/petals/monochrome/", monochromeName, tintColour);
        sprite.setOrigin(sprite.getWidth()/2, 0);
    }

    /**
     * Add a location/rotation pair to the places list
     * @param location Location of the petal
     * @param rotation Rotation of the petal
     */
    public void Add(Vector2 location, float rotation) {
        places.add(new LocRotPair(location, rotation));
    }

    /**
     * Remove all locations and rotations from places
     */
    public void Clear() {
        places = new ArrayList<LocRotPair>();
    }

    /**
     * Draw the petals
     * @param batch Spritebatch to draw with
     */
    public void Draw(SpriteBatch batch) {
        for (LocRotPair place : places) { //For each location/rotation pair
            SetPosWithRotationalOrigin(place.location); //Set position of sprite
            sprite.setRotation(-(place.rotation)); //Set rotation of sprite
            sprite.draw(batch); //Draw sprite
        }
    }

    public float GetBloomGrowthRate() {
        return bloomGrowthRate;
    }
    public float GetXGrowthAfter() {return xGrowthAfter; }

    /**
     * Set bloom properties of this petal type
     * @param bloomGrowthRate Y growth per unit time when blooming
     * @param xGrowthAfter X growth per unit time when blooming
     */
    public void SetBlooms(float bloomGrowthRate, float xGrowthAfter) {
        this.bloomGrowthRate = bloomGrowthRate;
        this.xGrowthAfter = xGrowthAfter;
    }

    /***
     * Both a 2D vector and a rotation
     */
    class LocRotPair {
        Vector2 location;
        float rotation;

        /**
         * Constructor for location/rotation pair
         * @param location Vector representing location
         * @param rotation Float representing rotation
         */
        public LocRotPair(Vector2 location, float rotation) {
            this.location = location;
            this.rotation = rotation;
        }
    }
}
