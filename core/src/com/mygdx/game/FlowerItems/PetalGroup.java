package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TintableElement;

import java.util.ArrayList;
import java.util.List;

public class PetalGroup extends TintableElement {
    float endScale = 2; //Scale of fully mature petal
    float bloomGrowthRate = 1f; //amount it grows per growth while blooming

    List<LocRotPair> places = new ArrayList<LocRotPair>();

    /**
     * Constructor for a petal
     *
     * @param monochromeName The index of the monochrome image in the textures/petals/monochrome folder
     * @param tintColour      The colour you wish to tint the petal
     */
    public PetalGroup(String monochromeName, Color tintColour) {
        super("textures/petals/monochrome/", monochromeName, tintColour);
        sprite.setOrigin(sprite.getWidth()/2, 0);
    }
    public void Add(Vector2 location, float rotation) {
        places.add(new LocRotPair(location, rotation));
    }
    public void Clear() {
        places = new ArrayList<LocRotPair>();
    }

    public void Draw(SpriteBatch batch) {
        for (int i = 0; i < places.size(); i++) {
            LocRotPair place = places.get(i);
            SetPosWithRotationalOrigin(place.location);
            sprite.setRotation(-(place.rotation));
            sprite.draw(batch);
        }
    }

    /***
     * Both a 2D vector and a rotation
     */
    class LocRotPair { //If this ends up being used elsewhere too, it can be moved
        Vector2 location;
        float rotation;
        public LocRotPair(Vector2 location, float rotation) {
            this.location = location;
            this.rotation = rotation;
        }
    }
}
