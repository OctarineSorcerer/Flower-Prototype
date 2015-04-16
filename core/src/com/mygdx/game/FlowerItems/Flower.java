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
public class Flower { //These are their own classes as they may need unique functionality later
    public ArrayList<PetalGroup> petals;
    public ArrayList<Integer> petalIndices;
    public Head head;
    public Stem stem;
    public Vector2 rootLoc;
    public Hole hole; //Every flower has to have a hole dug, pretty much

    public GrowthHandling growth;

    private boolean petalsOutside = false;

    public Flower(ArrayList<PetalGroup> petals, ArrayList<Integer> petalIndices, Head flowerHead, Stem flowerStem,
                  Point2D root, GrowthHandling growth) {
        rootLoc = new Vector2(root.x, root.y);
        hole = new Hole(rootLoc.x, rootLoc.y);
        head = flowerHead;
        stem = flowerStem;

        this.petals = petals;
        this.petalIndices = petalIndices;

        head.SetCenter(rootLoc);
        this.growth = growth; //0, so it won't grow
        ArrangePetals();
    }

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

    private void SetPetalPositions() {
        float sepAngle = 360f / petalIndices.size();
        for(PetalGroup petalGroup : petals) {
            petalGroup.Clear();
        }
        for(int i = 0; i < petalIndices.size(); i++) {
            int petalIndex = petalIndices.get(i);
            AddPetalPosition(i*sepAngle, petals.get(petalIndex));
        }
    }
    private void AddPetalPosition(float angle, PetalGroup petalGroup) {
        float sagitta = (float)(head.radius - Math.sqrt(Math.pow(head.radius, 2)
                - Math.pow(0.5* petalGroup.sprite.getWidth(), 2)));
        //^That bit is the height of the arc. Yeah. Go maths. http://www.mathopenref.com/sagitta.html
        Vector2 location = head.GetCenter().add(FlowerMaths.GetPetalPos(head.radius - sagitta, angle));
        petalGroup.Add(location, angle);
    }
    public static ArrayList<Integer> GetIndexMix (int maxNumber, int amount) {
        ArrayList<Integer> output = new ArrayList<Integer>();
        for(int i = 0; i < amount; i++) {
            output.add(FlowerPrototype.rand.nextInt(maxNumber + 1));
        }
        return output;
    }

    public void ApplyGrowth() {
        growth.CheckTime();
        stem.stemTip = stem.curveInfo.GetPositionAtT(growth.Growth, rootLoc);
        head.SetCenter(stem.stemTip);
        SetPetalPositions();
        //petals/blooming
        float bloomAmount = growth.GetAmountLastBloomed();
        if(bloomAmount > 0) {
            for(PetalGroup petalGroup : petals) {
                float xScale = petalGroup.sprite.getScaleX(); //was 1f
                //float origYScale = petalGroup.sprite.getScaleY();
                float scaleY = petalGroup.sprite.getScaleY() + bloomAmount * petalGroup.bloomGrowthRate;
                petalGroup.sprite.setScale(xScale,
                        scaleY);
                if(petalGroup.sprite.getScaleY() > 0) {
                    petalsOutside = true;
                }
            }
        }
    }
    public void ApplyLoadGrowth() {
        growth.CheckTime();
        stem.stemTip = stem.curveInfo.GetPositionAtT(growth.Growth, rootLoc); //this will always occur correctly
        head.SetCenter(stem.stemTip);
        SetPetalPositions();
        //petals/blooming
        float bloomAmount = growth.bloomInfo.BloomBetweenGrowths(0, growth.Growth); //do all the bloom scaling
        if(bloomAmount > 0) {
            for(PetalGroup petalGroup : petals) {
                float xScale = petalGroup.sprite.getScaleX(); //was 1f
                if(petalGroup.sprite.getScaleY() > 0) {
                    petalsOutside = true;
                }
                float scaleY = petalGroup.sprite.getScaleY() + bloomAmount * petalGroup.bloomGrowthRate;
                petalGroup.sprite.setScale(xScale,
                        scaleY);
            }
        }
    }

    public void DebugChangeStem() {
        stem = new Stem(new Random().nextLong());
        head.SetCenter(rootLoc);
        head.SetCenter(stem.stemTip);
        SetPetalPositions();
    }

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

    public void Dispose() {
        for(PetalGroup petal : petals) {

        }
    }
}
