package com.mygdx.game.FlowerItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.*;
import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Not an interface - the flower should be procedural
 */
public class Flower {
    public ArrayList<PetalGroup> petals; //These are the petals that make up the flower - they are grouped if they are clones
    public ArrayList<Integer> petalIndices; //Indexes of each petal - ie which petalgroup each petal is
    public Head head;
    public Stem stem;
    public Vector2 rootLoc; //Location of the root(the bottom of the stem)
    public Hole hole; //Every flower has to have a hole, right?

    public GrowthHandling growth; //Class to handle how much the flower has grown

    private boolean petalsOutside = false; //Stores whether the petals are outside the head yet

    /**
     * Constructor that sets all the relevant properties
     * @param petals Petalgroups this flower should have
     * @param petalIndices Indices of the petalgroups each petal should be in. One entry per petal
     * @param flowerHead Head of the flower
     * @param flowerStem Stem of the flower
     * @param root Location of the flower's root - ie its ground location
     * @param growth Details of this flower's growth
     */
    public Flower(ArrayList<PetalGroup> petals, ArrayList<Integer> petalIndices, Head flowerHead, Stem flowerStem,
                  Point2D root, GrowthHandling growth) {
        rootLoc = new Vector2(root.x, root.y);
        hole = new Hole(rootLoc.x, rootLoc.y);
        head = flowerHead;
        stem = flowerStem;

        this.petals = petals;
        this.petalIndices = petalIndices;

        head.SetCenter(rootLoc);
        this.growth = growth;
        ArrangePetals();
    }

    /**
     * Initial arrangement of petals, sets their scaling for later in their lives
     */
    void ArrangePetals() {
        for(PetalGroup petalGroup : petals) {
            float ratio = petalGroup.sprite.getHeight() / head.radius;
            petalGroup.xGrowthAfter = petalGroup.sprite.getScaleY()/growth.bloomInfo.GetBloomLength();
            petalGroup.bloomGrowthRate = ratio / growth.bloomInfo.GetBloomLength(); //Bloomlength not right (It's 2.5 instead of 4)
            petalGroup.sprite.setOrigin(petalGroup.sprite.getWidth() / 2, 0); //origin at bottom thingy
            petalGroup.sprite.scale(-ratio); //Sets top of petal to the middle of the head
            petalGroup.sprite.setScale(1f, petalGroup.sprite.getScaleY());
        }

        SetPetalPositions();
    }

    /**
     * Puts all the petals in the correct places around the head
     */
    private void SetPetalPositions() {
        float sepAngle = 360f / petalIndices.size();
        for(PetalGroup petalGroup : petals) {
            petalGroup.Clear(); //Clears these, so it can set them later
        }
        for(int i = 0; i < petalIndices.size(); i++) { //Adds a petal to the correct place
            int petalIndex = petalIndices.get(i);
            AddPetalPosition(i*sepAngle, petals.get(petalIndex));
        }
    }

    /**
     * Add a petal to the flower at a specified angle around the center of the head. Location will be calculated
     * @param angle Circular angle on the head where the petal should reside
     * @param petalGroup The group that this petal is
     */
    private void AddPetalPosition(float angle, PetalGroup petalGroup) {
        float sagitta = (float)(head.radius - Math.sqrt(Math.pow(head.radius, 2)
                - Math.pow(0.5* petalGroup.sprite.getWidth(), 2)));
        //^That bit is the height of the arc. http://www.mathopenref.com/sagitta.html
        //The sagitta is used to determine how much the petal should be "sunk into" the head to avoid just touching it
        Vector2 location = head.GetCenter().add(FlowerMaths.GetPetalPos(head.radius - sagitta, angle)); //Gets location the petal should be at
        petalGroup.Add(location, angle);
    }

    /**
     * Generates a sequence of integers. Sequence is of a specified length, with a ceiling on the numbers that may occur
     * @param maxNumber The maximum a number in the sequence can be
     * @param amount How many numbers should be in the sequence
     * @return A sequence of length amount, containing numbers between 0-maxNumber
     */
    public static ArrayList<Integer> GetIndexMix (int maxNumber, int amount) {
        ArrayList<Integer> output = new ArrayList<Integer>();
        for(int i = 0; i < amount; i++) {
            output.add(FlowerPrototype.rand.nextInt(maxNumber + 1));
        }
        return output;
    }

    /**
     * Checks the time difference between last growth and this, applying it
     */
    public void ApplyGrowth() {
        growth.CheckTime();
        stem.stemTip = stem.curveInfo.GetPositionAtT(growth.Growth, rootLoc); //Grow the stem
        head.SetCenter(stem.stemTip); //Place head after stem growth
        SetPetalPositions();
        //petals/blooming
        float bloomAmount = growth.GetAmountLastBloomed(); //How much the flower has bloomed since last time
        if(bloomAmount > 0) { //This part scales the petals appropriately
            for(PetalGroup petalGroup : petals) {
                float xScale = petalGroup.sprite.getScaleX();
                float scaleY = petalGroup.sprite.getScaleY() + bloomAmount * petalGroup.bloomGrowthRate;
                petalGroup.sprite.setScale(xScale,
                        scaleY);
                if(petalGroup.sprite.getScaleY() > 0) {
                    petalsOutside = true;
                }
            }
        }
    }

    /**
     * This sub basically does the same as above, but is for use on loading. Is a different subroutine to save a little
     * logic when checking growth
     */
    public void ApplyLoadGrowth() {
        growth.CheckTime();
        stem.stemTip = stem.curveInfo.GetPositionAtT(growth.Growth, rootLoc);
        head.SetCenter(stem.stemTip);
        SetPetalPositions();
        //petals/blooming
        float bloomAmount = growth.bloomInfo.BloomBetweenGrowths(0, growth.Growth);
        if(bloomAmount > 0) {
            for(PetalGroup petalGroup : petals) {
                float xScale = petalGroup.sprite.getScaleX();
                float scaleY = petalGroup.sprite.getScaleY() + bloomAmount * petalGroup.bloomGrowthRate;
                petalGroup.sprite.setScale(xScale,
                        scaleY);
                if(petalGroup.sprite.getScaleY() > 0) {
                    petalsOutside = true;
                }
            }
        }
    }

    /**
     * Grows the stem by one curve
     */
    public void LengthenStem() {
        //If this occurred while blooming, as things are I'd have to choose between curves possibly instantly growing,
        //or the petals growing extremely out of proportion
        if(!growth.bloomInfo.Blooming) {
            stem.curveInfo.AddCurve();
            stem.curves = stem.curveInfo.GetCurveCount();
            if (growth.Growth > stem.curveInfo.GetCurveCount()) {
                growth.Growth = stem.curveInfo.GetCurveCount();
                //Stop the new curve from instantly appearing if enough time has already elapsed.
            }
        }
    }

    /**
     * Handles the drawing of the flower
     * @param batch Spritebatch to use for drawing
     */
    public void DrawSprites(SpriteBatch batch) {
        if(hole.dug) {
            if (petalsOutside) {
                for (PetalGroup petalGroup : petals) {
                    petalGroup.Draw(batch);
                }
                head.sprite.draw(batch);
            } else {
                head.sprite.draw(batch);
                for (PetalGroup petalGroup : petals) {
                    petalGroup.Draw(batch);
                }
            }
        }
    }

    public void DrawHole(SpriteBatch batch, float delta) {
        if(hole.beingDug) {
            hole.draw(batch, delta);
        }
    }

    public void DrawShapes(ShapeRenderer shapeRenderer) {
        stem.curveInfo.DrawSome(shapeRenderer, new Vector2(rootLoc.x, rootLoc.y), stem.colour, growth.Growth, stem.thickness);
    }
}
