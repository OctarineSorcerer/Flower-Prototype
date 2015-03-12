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
    public List<PetalGroup> petals;
    public List<Integer> petalIndices;
    public Head head;
    public Stem stem;
    public Vector2 rootLoc;

    public GrowthHandling growth;

    private int count;
    private boolean petalsOutside = false;
    private PetalStyle style;

    public Flower(List<PetalGroup> petals, List<Integer> petalIndices, Head flowerHead, Stem flowerStem, PetalStyle petalArrangement, Point2D root) {
        rootLoc = new Vector2(root.x, root.y);
        head = flowerHead;
        stem = flowerStem;

        this.petals = petals;
        this.petalIndices = petalIndices;
        count = petalIndices.size();

        style = petalArrangement;

        head.SetCenter(rootLoc);
        growth = new GrowthHandling(1f, 6f, 4f);
        ArrangePetals(style);
    }

    void ArrangePetals(PetalStyle arrangement) {
        float sepAngle = 360f / petalIndices.size();
        switch (arrangement) {
            case Touching:
                //Set each petalGroup's sprite stuff
                for(PetalGroup petalGroup : petals) {
                    float petalWidth = FlowerMaths.GetPetalWidth(sepAngle, 1f, head.radius);
                    petalGroup.xGrowthAfter = Math.abs((petalWidth / petalGroup.sprite.getWidth())
                            / growth.bloomInfo.bloomLength);
                    float ratio = petalGroup.sprite.getHeight() / head.radius;
                    petalGroup.bloomGrowthRate = (ratio + petalGroup.sprite.getScaleX()) / growth.bloomInfo.bloomLength;
                    petalGroup.sprite.setOrigin(petalGroup.sprite.getWidth() / 2, 0); //origin at bottom thingy
                    petalGroup.sprite.scale(-ratio); //Sets top of petal to the middle of the head
                    petalGroup.sprite.setScale(1f /*(petalWidth / petalGroup.sprite.getWidth())*/, petalGroup.sprite.getScaleY());
                }

                SetPetalPositions();
                break;
        }
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
    public static List<Integer> GetIndexMix (int maxNumber, int amount) {
        List<Integer> output = new ArrayList<Integer>();
        for(int i = 0; i < amount; i++) {
            output.add(FlowerPrototype.rand.nextInt(maxNumber + 1));
        }
        return output;
    }

    public void ApplyGrowth() {
        float grown = growth.CheckTime();
        stem.stemTip = stem.curveInfo.GetPositionAtT(growth.Growth, rootLoc);
        head.SetCenter(stem.stemTip);
        SetPetalPositions();
        //petals/blooming
        float bloomAmount = growth.GetAmountLastBloomed();
        if(bloomAmount > 0) {
            for(PetalGroup petalGroup : petals) {
                float xScale = petalGroup.sprite.getScaleX(); //was 1f
                if(petalGroup.sprite.getScaleY() > 0) {
                    petalsOutside = true;
                    xScale = petalGroup.sprite.getScaleX() + bloomAmount * petalGroup.xGrowthAfter;
                }
                float scaleY = petalGroup.sprite.getScaleY() + bloomAmount * petalGroup.bloomGrowthRate;
                petalGroup.sprite.setScale(xScale,
                        scaleY);
                //System.out.println("Width: " + petalGroup.sprite.getWidth() + "| Height: " + petalGroup.sprite.getY());
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
        if(petalsOutside) {
            for (PetalGroup petalGroup : petals) {
                petalGroup.Draw(batch);
            }
            head.sprite.draw(batch);
        }
        else {
            head.sprite.draw(batch);
            for (PetalGroup petalGroup : petals) {
                petalGroup.Draw(batch);
            }
        }
    }

    public void DrawShapes(ShapeRenderer shapeRenderer) {
        stem.curveInfo.DrawSome(shapeRenderer, new Vector2(rootLoc.x, rootLoc.y), growth.Growth);
    }

    public enum PetalStyle {
        Overlapping,
        Touching,
    }
}
