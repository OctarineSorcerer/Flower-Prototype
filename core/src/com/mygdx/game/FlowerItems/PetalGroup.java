package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TintableElement;

import java.util.ArrayList;
import java.util.List;

public class PetalGroup extends TintableElement {
    //float bottomWidth = 110f;
    float bloomGrowthRate = 1f; //amount it grows per unit while blooming
    float xGrowthAfter = 0f;

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
        //bottomWidth = GetBottomRowWidth(new Pixmap(Gdx.files.internal(MonoPath())));
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

    private int GetBottomRowWidth(Pixmap image) {
        int minX = -1, maxX = -1;
        int width = image.getWidth();
        //sint aTestPix = image.getPixel(25, 55);
        for (int x = 0; x < width; x++) {
            int pixel = image.getPixel(x, image.getHeight() - 1);
            if ((pixel & 0x000000ff) != 0) {
                if (minX == -1) {
                    minX = x;
                } else {
                    maxX = x;
                }
            }
        }
        image.dispose();
        if(minX == -1 || maxX == -1) return width;
        else return maxX - minX + 1;
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
