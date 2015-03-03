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
    public Head head;
    public Stem stem;
    public Vector2 rootLoc;

    public GrowthHandling growth;

    private int count;
    private boolean petalsOutside = false;
    private PetalStyle style;

    public Flower(PetalGroup mainPetalGroup, Head flowerHead, Stem flowerStem, int petalCount, PetalStyle petalArrangement, Point2D root) {
        rootLoc = new Vector2(root.x, root.y);
        head = flowerHead;
        stem = flowerStem;

        petals = new ArrayList<PetalGroup>();
        petals.add(mainPetalGroup);
        count = petalCount;
        style = petalArrangement;
        //head.SetCenter(new Point2D(stem.stemTip.x + rootLoc.x, stem.stemTip.y + rootLoc.y));
        head.SetCenter(rootLoc);
        growth = new GrowthHandling(1f, 6f, 4f);
        ArrangePetals(style, count, 0);
    }

    void ArrangePetals(PetalStyle arrangement, int count, int petalIndex) {
        float sepAngle = 360f / (float) count;
        switch (arrangement) {
            case Touching:
                PetalGroup petalGroup = petals.get(petalIndex);
                float ratio = petalGroup.sprite.getHeight() / head.radius;
                float petalWidth = FlowerMaths.GetPetalWidth(sepAngle, 1f, head.radius);
                petalGroup.sprite.setScale((petalWidth/petalGroup.bottomWidth), petalGroup.sprite.getScaleY());
                petalGroup.xGrowthAfter = Math.abs((petalWidth-petalGroup.bottomWidth)/growth.bloomInfo.bloomLength);
                petalGroup.bloomGrowthRate = ratio/growth.bloomInfo.bloomLength;
                petalGroup.sprite.setOrigin(petalGroup.sprite.getWidth() / 2, 0); //origin at bottom thingy
                petalGroup.sprite.scale(-ratio); //Sets top of petal to the middle of the head


                SetPetalPositions(sepAngle, petalGroup);
                break;
        }
    }

    public void SetPetalPositions(float sepAngle, PetalGroup petalGroup) {
        float sagitta = (float)(head.radius - Math.sqrt(Math.pow(head.radius, 2)
                - Math.pow(0.5* petalGroup.sprite.getWidth(), 2)));
        //^That bit is the height of the arc. Yeah. Go maths. http://www.mathopenref.com/sagitta.html
        petalGroup.Clear();
        for (float i = 0; i < sepAngle * count; i += sepAngle) {
            Vector2 location = head.GetCenter().add(FlowerMaths.GetPetalPos(head.radius - sagitta, i));
            petalGroup.Add(location, i);
        }
    }

    public void ApplyGrowth() {
        float grown = growth.CheckTime();
        stem.stemTip = stem.curveInfo.GetPositionAtT(growth.Growth, rootLoc);
        head.SetCenter(stem.stemTip);
        for(PetalGroup petalGroup : petals) {
            SetPetalPositions(360f / (float) count, petalGroup);
        }
        //petals/blooming
        float bloomAmount = growth.GetAmountLastBloomed();
        if(bloomAmount > 0) {
            for(PetalGroup petalGroup : petals) {
                float xScale = 1f;
                if(petalGroup.sprite.getScaleY() > 0) {
                    petalsOutside = true;
                    xScale = petalGroup.sprite.getScaleX() + bloomAmount * petalGroup.xGrowthAfter;
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
        for(PetalGroup petalGroup : petals) {
            SetPetalPositions(360f / (float) count, petalGroup);
        }
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
